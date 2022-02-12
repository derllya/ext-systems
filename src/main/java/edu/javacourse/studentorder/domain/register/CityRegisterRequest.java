package edu.javacourse.studentorder.domain.register;

import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.util.LocalDateAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

public class CityRegisterRequest {
    private String name;
    private String surname;
    private String patronymic;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate birthDay;
    private Long streetCode;
    private String building;
    private String extension;
    private String apartment;

    public CityRegisterRequest() {
    }

    public CityRegisterRequest(Person person){
        name = person.getName();
        surname = person.getSurname();
        patronymic = person.getPatronymic();
        birthDay = person.getBirthDate();
        streetCode = person.getAddress().getStreet().getStreet_code();
        building = person.getAddress().getBuilding();
        extension = person.getAddress().getExtension();
        apartment = person.getAddress().getApartment();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Long getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(Long streetCode) {
        this.streetCode = streetCode;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    @Override
    public String toString() {
        return "CityRegisterRequest{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthDay=" + birthDay +
                ", streetCode=" + streetCode +
                ", building='" + building + '\'' +
                ", extension='" + extension + '\'' +
                ", apartment='" + apartment + '\'' +
                '}';
    }
}
