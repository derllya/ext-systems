package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exeption.PersonCheckException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonCheckDao {
    private static final String SQL_REQUEST = "select temporal from cr_address_person ap" +
            "inner join cr_person pers on ap.person_id = pers.person_id" +
            "inner join cr_address adr on ap.address_id = adr.address_id" +
            "where upper(pers.sur_name) = upper(?) and upper(pers.given_name) = upper(?) and" +
            "upper(pers.patronymic) = upper(?) and pers.date_of_birth = ? and" +
            "adr.street_code = ? and upper(adr.building) = upper(?) and upper(adr.extension) = upper(?)" +
            "and upper(adr.apartment) = upper(?);";

    public PersonResponse checkPerson(PersonRequest personRequest) throws PersonCheckException {
        PersonResponse personResponse = new PersonResponse();

        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_REQUEST)){

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

    private Connection getConnection() {
        return null;
    }
}
