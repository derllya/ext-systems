package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exeption.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {
    private static final String SQL_REQUEST = "select temporal from cr_address_person ap " +
            "inner join cr_person pers on ap.person_id = pers.person_id " +
            "inner join cr_address adr on ap.address_id = adr.address_id " +
            "where CURRENT_DATE >= ap.start_date and (CURRENT_DATE <= ap.end_date or ap.end_date is null) " +
            "and upper(pers.sur_name) = upper(?) and upper(pers.given_name) = upper(?) and " +
            "upper(pers.patronymic) = upper(?) and pers.date_of_birth = ? and " +
            "adr.street_code = ? and upper(adr.building) = upper(?) ";

    private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    public PersonResponse checkPerson(PersonRequest personRequest) throws PersonCheckException {
        PersonResponse personResponse = new PersonResponse();

        String sql = SQL_REQUEST;

        if (personRequest.getExtension() != null){
            sql += "and upper(adr.extension) = upper(?) ";
        }
        else {
            sql += "and adr.extension is null ";
        }
        if (personRequest.getApartment() != null){
            sql += "and upper(adr.apartment) = upper(?) ";
        }
        else {
            sql += "and adr.apartment is null ";
        }

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            int count = 1;
            statement.setString(count++,personRequest.getSurname());
            statement.setString(count++,personRequest.getName());
            statement.setString(count++,personRequest.getPatronymic());
            statement.setDate(count++, java.sql.Date.valueOf(personRequest.getBirthDay()));
            statement.setInt(count++, personRequest.getStreetCode());
            statement.setString(count++, personRequest.getBuilding());
            if (personRequest.getExtension() != null) {
                statement.setString(count++, personRequest.getExtension());
            }
            if (personRequest.getApartment() != null) {
                statement.setString(count++, personRequest.getApartment());
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                personResponse.setRegistered(true);
                personResponse.setTemporal(resultSet.getBoolean("temporal"));
            }

        }catch (SQLException exception){
            throw new PersonCheckException(exception);
        }

        return personResponse;
    }

    private Connection getConnection() throws SQLException {
        return connectionBuilder.getConnection();
    }
}
