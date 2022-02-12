package edu.javacourse.city.web;

import edu.javacourse.city.dao.PersonCheckDao;
import edu.javacourse.city.dao.PoolConnectionBuilder;
import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exeption.PersonCheckException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

@Path("/check")
@Singleton
public class CheckPersonService
{
    private static final Logger logger = LoggerFactory.getLogger(CheckPersonService.class);

    private PersonCheckDao dao;

    @PostConstruct
    public void init() {
        logger.info("SERVICE is created");
        dao = new PersonCheckDao();
        dao.setConnectionBuilder(new PoolConnectionBuilder());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {
        logger.info(request.toString());
        return dao.checkPerson(request);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PersonRequest respose(){
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("Павел");
        personRequest.setSurname("Васильев");
        personRequest.setPatronymic("Николаевич");
        personRequest.setStreetCode(1);
        personRequest.setBuilding("10");
        personRequest.setExtension("2");
        personRequest.setApartment("121");
        personRequest.setBirthDay(LocalDate.of(1995,03,18));
        return personRequest;
    }
}