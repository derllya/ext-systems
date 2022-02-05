package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.domain.*;
import edu.javacourse.studentorder.exception.DaoException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StudentDaoImplTest {

    @BeforeClass
    public static void StartUp() throws Exception {
        DBInit.StartUp();
    }
    @Test
    public void saveStudentOrder() throws DaoException {
        StudentOrder studentOrder = buildStudentOrder(10);
        long id = new StudentDaoImpl().saveStudentOrder(studentOrder);
    }

    @Test(expected = DaoException.class)
    public void saveStudentOrderError() throws DaoException {
        StudentOrder studentOrder = buildStudentOrder(10);
        studentOrder.getHusband().setSurname(null);
        long id = new StudentDaoImpl().saveStudentOrder(studentOrder);
    }

    @Test
    public void getStudentOrders() throws DaoException {
        List<StudentOrder> studentOrders = new StudentDaoImpl().getStudentOrders();
    }

    public StudentOrder buildStudentOrder(long id){
        StudentOrder so = new StudentOrder();
        so.setStudentOrderId(id);
        so.setMarriageCertificatedId("" + (123456000 + id));
        so.setMarriageDate(LocalDate.of(2015,06,14));
        RegisterOffice registerOffice = new RegisterOffice(1L,"","");
        so.setMarriageOffice(registerOffice);

        Street street = new Street(1L, "Невский проспект");
        Address address = new Address("195000", street, "6", "1", "15");

        //Муж
        Adult husband = new Adult("Василий", "Позен", "Иванович", LocalDate.of(1995,07,30));
        husband.setPassportSerial("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017,9,15));
        PassportOffice passportOffice1 = new PassportOffice(1L,"","");
        husband.setIssueDepartment(passportOffice1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2,""));
        husband.setStudentId("HH12345");
        //Жена
        Adult wife = new Adult("Злата", "Позена", "Петровна", LocalDate.of(1998, 05, 16));
        wife.setPassportSerial("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2019,6,13));
        PassportOffice passportOffice2 = new PassportOffice(2L,"","");
        wife.setIssueDepartment(passportOffice2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1,""));
        wife.setStudentId("WW12345");
        //Дети
        Child child1 = new Child("Иван","Позен","Васильевич", LocalDate.of(2020,04,05));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2020,02,01));
        RegisterOffice registerOffice2 = new RegisterOffice(2L,"","");
        child1.setIssueDepartment(registerOffice2);
        child1.setAddress(address);

        Child child2 = new Child("Алеша","Позен","Васильевич", LocalDate.of(2021,01,04));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2021,02,01));
        RegisterOffice registerOffice3 = new RegisterOffice(3L,"","");
        child2.setIssueDepartment(registerOffice3);
        child2.setAddress(address);

        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);

        return so;
    }
}