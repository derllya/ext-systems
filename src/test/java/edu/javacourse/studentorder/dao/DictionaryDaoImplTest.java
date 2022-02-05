package edu.javacourse.studentorder.dao;

import edu.javacourse.studentorder.domain.CountryArea;
import edu.javacourse.studentorder.domain.PassportOffice;
import edu.javacourse.studentorder.domain.RegisterOffice;
import edu.javacourse.studentorder.domain.Street;
import edu.javacourse.studentorder.exception.DaoException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

public class DictionaryDaoImplTest {
    public static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImplTest.class);

    @BeforeClass
    public static void StartUp() throws Exception {
        DBInit.StartUp();
    }

    @Test
    public void testStreet() throws DaoException {
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("TEST {}", dateTime);
        List<Street> streets = new DictionaryDaoImpl().findStreets("про");
        Assert.assertTrue(streets.size() == 2);
    }

    @Test
    public void testPassportOffice() throws DaoException {
        List<PassportOffice> passportOffices = new DictionaryDaoImpl().findPassportOffices("010020000000");
        Assert.assertTrue(passportOffices.size() == 2);
    }

    @Test
    public void testRegisterOffice() throws DaoException {
        List<RegisterOffice> registerOffices = new DictionaryDaoImpl().findRegisterOffices("010010000000");
        Assert.assertTrue(registerOffices.size() == 2);
    }

    @Test
    public void testArea() throws DaoException {
        List<CountryArea> countryAreas1 = new DictionaryDaoImpl().findAreas("");
        Assert.assertTrue(countryAreas1.size() == 2);
        List<CountryArea> countryAreas2 = new DictionaryDaoImpl().findAreas("020000000000");
        Assert.assertTrue(countryAreas2.size() == 2);
        List<CountryArea> countryAreas3 = new DictionaryDaoImpl().findAreas("020010000000");
        Assert.assertTrue(countryAreas3.size() == 2);
        List<CountryArea> countryAreas4 = new DictionaryDaoImpl().findAreas("020010010000");
        Assert.assertTrue(countryAreas4.size() == 2);
    }
}