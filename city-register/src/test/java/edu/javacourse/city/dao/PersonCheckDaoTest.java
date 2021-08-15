package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exeption.PersonCheckException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class PersonCheckDaoTest {

    @Test
    public void checkPerson() throws PersonCheckException {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("Павел");
        personRequest.setSurname("Васильев");
        personRequest.setPatronymic("Николаевич");
        personRequest.setBirthDay(LocalDate.of(1995, 3,18));
        personRequest.setStreetCode(1);
        personRequest.setBuilding("10");
        personRequest.setExtension("2");
        personRequest.setApartment("121");

        PersonCheckDao personCheckDao = new PersonCheckDao();
        PersonResponse personResponse = personCheckDao.checkPerson(personRequest);
        Assert.assertTrue(personResponse.isRegistered());
        Assert.assertFalse(personResponse.isTemporal());
    }
    @Test
    public void checkPerson2() throws PersonCheckException {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("Ирина");
        personRequest.setSurname("Васильева");
        personRequest.setPatronymic("Петровна");
        personRequest.setBirthDay(LocalDate.of(1997, 8,21));
        personRequest.setStreetCode(1);
        personRequest.setBuilding("271");
        personRequest.setApartment("4");

        PersonCheckDao personCheckDao = new PersonCheckDao();
        PersonResponse personResponse = personCheckDao.checkPerson(personRequest);
        Assert.assertTrue(personResponse.isRegistered());
        Assert.assertFalse(personResponse.isTemporal());
    }
}