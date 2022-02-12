package edu.javacourse.studentorder.validator.register;

import edu.javacourse.studentorder.domain.Address;
import edu.javacourse.studentorder.domain.Adult;
import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.domain.Street;
import edu.javacourse.studentorder.domain.register.CityRegisterRequest;
import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.exception.TransportException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

public class RealCityRegisterChecker implements CityRegisterChecker {
    public static void main(String[] args) throws CityRegisterException, TransportException {
        Person person = new Adult("Ivan", "Pavlov", "Ivanovich", LocalDate.of(1995,03,18));
        Street street = new Street(1L,null);
        Address address = new Address(null, street, "10", "2", "121");
        person.setAddress(address);
        RealCityRegisterChecker realCityRegisterChecker = new RealCityRegisterChecker();
        realCityRegisterChecker.checkPerson(person);
    }
    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {
        CityRegisterRequest cityRegisterRequest = new CityRegisterRequest(person);
        Client client = ClientBuilder.newClient();

        CityRegisterResponse cityRegisterResponse = client.target("http://localhost:8080/city-register-1.0/rest/check")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(cityRegisterRequest, MediaType.APPLICATION_JSON))
                .readEntity(CityRegisterResponse.class);

        return cityRegisterResponse;
    }
}
