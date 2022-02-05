package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.config.Config;
import edu.javacourse.studentorder.domain.*;
import edu.javacourse.studentorder.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDaoImpl implements StudentOrderDao{

    private static final Logger logger = LoggerFactory.getLogger(StudentDaoImpl.class);

    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, h_name, h_sur_name, h_patronymic, h_date_of_birth," +
            " h_passport_serial, h_passport_number, h_passport_date, h_passport_office_id, h_post_index, h_street_code," +
            " h_building, h_extension, h_apartment, h_university_id, h_student_number, w_name, w_sur_name, w_patronymic, w_date_of_birth," +
            " w_passport_serial, w_passport_number, w_passport_date, w_passport_office_id, w_post_index, w_street_code, w_building, w_extension," +
            " w_apartment, w_university_id, w_student_number, certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(student_order_id, c_name," +
            " c_sur_name, c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, c_post_index," +
            " c_street_code, c_building, c_extension, c_apartment) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String SELECT_ORDERS = "SELECT so.*, ro.r_office_name, ro.r_office_area_id, " +
            "po_h.p_office_area_id AS h_p_office_area_id, po_h.p_office_name AS h_p_office_name, " +
            "po_w.p_office_area_id AS w_p_office_area_id, po_w.p_office_name AS w_p_office_name " +
            "FROM jc_student_order so " +
            "INNER JOIN jc_register_office ro ON so.register_office_id = ro.r_office_id " +
            "INNER JOIN jc_passport_office po_h ON so.h_passport_office_id = po_h.p_office_id " +
            "INNER JOIN jc_passport_office po_w ON so.w_passport_office_id = po_w.p_office_id " +
            "WHERE student_order_status = ? ORDER BY student_order_date LIMIT ?;";

    public static final String SELECT_CHILD = "SELECT soc.*, ro.r_office_name, ro.r_office_area_id " +
            "FROM jc_student_child soc " +
            "INNER JOIN jc_register_office ro ON soc.c_register_office_id = ro.r_office_id " +
            "WHERE soc.student_order_id IN ";

    public static final String SELECT_ORDERS_FULL = "SELECT so.*, ro.r_office_name, ro.r_office_area_id, " +
            "po_h.p_office_area_id AS h_p_office_area_id, po_h.p_office_name AS h_p_office_name, " +
            "po_w.p_office_area_id AS w_p_office_area_id, po_w.p_office_name AS w_p_office_name, " +
            "soc.*, ro_c.r_office_name AS c_r_office_name, ro_c.r_office_area_id AS c_r_office_area_id " +
            "FROM jc_student_order so " +
            "INNER JOIN jc_register_office ro ON so.register_office_id = ro.r_office_id " +
            "INNER JOIN jc_passport_office po_h ON so.h_passport_office_id = po_h.p_office_id " +
            "INNER JOIN jc_passport_office po_w ON so.w_passport_office_id = po_w.p_office_id " +
            "INNER JOIN jc_student_child soc ON  soc.student_order_id = so.student_order_id " +
            "INNER JOIN jc_register_office ro_c ON soc.c_register_office_id = ro_c.r_office_id " +
            "WHERE student_order_status = ? ORDER BY so.student_order_id LIMIT ?;";

    private Connection getConnection() throws SQLException {
        return ConnectionBuilder.getConnection();
    }

    @Override
    public long saveStudentOrder(StudentOrder studentOrder) throws DaoException {

        Long result = -1L;

        logger.debug("StudentOrder:{}",studentOrder);

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(INSERT_ORDER, new String[] {"student_order_id"})) {

            con.setAutoCommit(false);
            try {
                //Student order info
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                //Husband and Wife params
                setParamsForAdult(stmt, 3, studentOrder.getHusband());
                setParamsForAdult(stmt, 18, studentOrder.getWife());


                //Marriage info
                stmt.setString(33, studentOrder.getMarriageCertificatedId());
                stmt.setLong(34, studentOrder.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(studentOrder.getMarriageDate()));

                stmt.executeUpdate();

                //Children
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    result = generatedKeys.getLong("student_order_id");
                }

                saveChildren(studentOrder, result, con);

                con.commit();
            }
            catch (SQLException ex){
                logger.error(ex.getMessage(), ex);
                con.rollback();
                throw ex;
            }
        }
        catch (SQLException ex){
            throw new DaoException(ex);
        }
        return result;
    }

    private void setParamsForAdult(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4, adult.getPassportSerial());
        stmt.setString(start + 5, adult.getPassportNumber());
        stmt.setDate(start + 6, java.sql.Date.valueOf(adult.getIssueDate()));
        stmt.setLong(start + 7,adult.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, start + 8, adult);
        stmt.setLong(start + 13,adult.getUniversity().getUniversityId());
        stmt.setString(start + 14, adult.getStudentId());
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt,2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7,java.sql.Date.valueOf(child.getIssueDate()));
        stmt.setLong(8,child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt,9,child);
    }

    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start, person.getName());
        stmt.setString(start + 1,person.getSurname());
        stmt.setString(start + 2, person.getPatronymic());
        stmt.setDate(start + 3, java.sql.Date.valueOf(person.getBirthDate()));
    }

    private void setParamsForAddress(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address husbandAddress = person.getAddress();
        stmt.setString(start, husbandAddress.getPostCode());
        stmt.setLong(start + 1, husbandAddress.getStreet().getStreet_code());
        stmt.setString(start + 2, husbandAddress.getBuilding());
        stmt.setString(start + 3,husbandAddress.getExtension());
        stmt.setString(start + 4,husbandAddress.getApartment());
    }

    private void saveChildren(StudentOrder studentOrder, Long studentOrderId, Connection con) throws SQLException {
        List<Child> children = studentOrder.getChildren();
        try (PreparedStatement stmt = con.prepareStatement(INSERT_CHILD)) {
            for (Child child : children) {
                stmt.setLong(1, studentOrderId);
                setParamsForChild(stmt, child);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        return getStudentOrdersOneSelect();
        //return getStudentOrdersTwoSelect();
    }

    private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS_FULL)){
            Map <Long, StudentOrder> maps = new HashMap<>();
            stmt.setLong(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            stmt.setInt(2, limit);
            int counter = 0;

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                Long studentOrderId = resultSet.getLong("student_order_id");
                if (!maps.containsKey(studentOrderId)) {
                    StudentOrder studentOrder = getFullStudentOrder(resultSet);

                    result.add(studentOrder);
                    maps.put(studentOrderId, studentOrder);
                }
                StudentOrder studentOrder = maps.get(studentOrderId);
                studentOrder.addChild(fillChild(resultSet));
                counter++;
            }
            if (counter >= limit){
                result.remove(result.size() - 1);
            }
            resultSet.close();

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection con = getConnection();
        PreparedStatement stmt = con.prepareStatement(SELECT_ORDERS)){
            stmt.setLong(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperty(Config.DB_LIMIT));
            stmt.setInt(2, limit);

            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()){
                StudentOrder studentOrder = getFullStudentOrder(resultSet);

                result.add(studentOrder);
            }
            findChildren(con, result);

            resultSet.close();

        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
            throw new DaoException(ex);
        }
        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet resultSet) throws SQLException {
        StudentOrder studentOrder = new StudentOrder();
        fillStudentOrder(resultSet, studentOrder);
        fillMarriage(resultSet, studentOrder);
        Adult husband = fillAdult(resultSet, "h_");
        Adult wife = fillAdult(resultSet, "w_");
        studentOrder.setHusband(husband);
        studentOrder.setWife(wife);
        return studentOrder;
    }

    private void fillStudentOrder(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setStudentOrderId(resultSet.getLong("student_order_id"));
        studentOrder.setStudentOrderDate(resultSet.getTimestamp("student_order_date").toLocalDateTime());
        studentOrder.setStudentOrderStatus(StudentOrderStatus.fromValue(resultSet.getInt("student_order_status")));
    }

    private Adult fillAdult(ResultSet resultSet, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setName(resultSet.getString(prefix + "name"));
        adult.setSurname(resultSet.getString(prefix + "sur_name"));
        adult.setPatronymic(resultSet.getString(prefix + "patronymic"));
        adult.setBirthDate(resultSet.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSerial(resultSet.getString(prefix + "passport_serial"));
        adult.setPassportNumber(resultSet.getString(prefix + "passport_number"));
        adult.setIssueDate(resultSet.getDate(prefix + "passport_date").toLocalDate());
        Long passportOfficeId = resultSet.getLong(prefix + "passport_office_id");
        String passportOfficeAreaId = resultSet.getString(prefix + "p_office_area_id");
        String passportOfficeName = resultSet.getString(prefix + "p_office_name");
        PassportOffice passportOffice = new PassportOffice(passportOfficeId, passportOfficeAreaId, passportOfficeName);
        adult.setIssueDepartment(passportOffice);
        Address address = new Address();
        Street street = new Street(resultSet.getLong(prefix + "street_code"), "");
        address.setStreet(street);
        address.setPostCode(resultSet.getString(prefix + "post_index"));
        address.setBuilding(resultSet.getString(prefix + "building"));
        address.setExtension(resultSet.getString(prefix + "extension"));
        address.setApartment(resultSet.getString(prefix + "apartment"));
        adult.setAddress(address);
        University university = new University(resultSet.getLong(prefix + "university_id"), "");
        adult.setUniversity(university);
        adult.setStudentId(resultSet.getString(prefix + "student_number"));
        return adult;
    }

    private void fillMarriage(ResultSet resultSet, StudentOrder studentOrder) throws SQLException {
        studentOrder.setMarriageCertificatedId(resultSet.getString("certificate_id"));
        studentOrder.setMarriageDate(resultSet.getDate("marriage_date").toLocalDate());
        int registerOfficeId = resultSet.getInt("register_office_id");
        String areaId = resultSet.getString("r_office_area_id");
        String officeName = resultSet.getString("r_office_name");
        RegisterOffice registerOffice = new RegisterOffice(registerOfficeId, areaId, officeName);
        studentOrder.setMarriageOffice(registerOffice);
    }

    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
        String collect = "(" + result.stream().map(studentOrder ->
                String.valueOf(studentOrder.getStudentOrderId())).collect(Collectors.joining(",")) + ")";
        Map<Long, StudentOrder> maps = result.stream().collect(Collectors
                .toMap(studentOrder -> studentOrder.getStudentOrderId(), studentOrder -> studentOrder));

        try(PreparedStatement preparedStatement = con.prepareStatement(SELECT_CHILD + collect)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Child child = fillChild(resultSet);
                StudentOrder studentOrder = maps.get(resultSet.getLong("student_order_id"));
                studentOrder.addChild(child);
            }

        }
    }

    private Child fillChild(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("c_name");
        String surname = resultSet.getString("c_sur_name");
        String patronymic = resultSet.getString("c_patronymic");
        LocalDate birthday = resultSet.getDate("c_date_of_birth").toLocalDate();
        Child child = new Child(name, surname, patronymic, birthday);

        child.setCertificateNumber(resultSet.getString("c_certificate_number"));
        child.setIssueDate(resultSet.getDate("c_certificate_date").toLocalDate());

        RegisterOffice registerOffice = new RegisterOffice();
        registerOffice.setOfficeId(resultSet.getLong("c_register_office_id"));
        registerOffice.setOfficeAreaId(resultSet.getString("r_office_area_id"));
        registerOffice.setOfficeName(resultSet.getString("r_office_name"));
        child.setIssueDepartment(registerOffice);

        Address address = new Address();
        Street street = new Street(resultSet.getLong("c_street_code"), "");
        address.setPostCode(resultSet.getString("c_post_index"));
        address.setStreet(street);
        address.setBuilding(resultSet.getString("c_building"));
        address.setExtension(resultSet.getString("c_extension"));
        address.setApartment(resultSet.getString("c_apartment"));
        child.setAddress(address);

        return child;
    }
}
