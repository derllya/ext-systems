package edu.javacourse.studentorder.domain;

public class Street {
    private Long street_code;
    private  String street_name;

    public Street() {
    }

    public Street(Long street_code, String street_name) {
        this.street_code = street_code;
        this.street_name = street_name;
    }

    public Long getStreet_code() {
        return street_code;
    }

    public void setStreet_code(Long street_code) {
        this.street_code = street_code;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    @Override
    public String toString() {
        return "Street{" +
                "street_code=" + street_code +
                ", street_name='" + street_name + '\'' +
                '}';
    }
}
