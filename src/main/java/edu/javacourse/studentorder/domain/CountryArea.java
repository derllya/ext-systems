package edu.javacourse.studentorder.domain;

public class CountryArea {
    private String area_id;
    private String area_name;

    public CountryArea() {

    }

    public CountryArea(String area_id, String area_name) {
        this.area_id = area_id;
        this.area_name = area_name;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    @Override
    public String toString() {
        return "CountryArea{" +
                "area_id='" + area_id + '\'' +
                ", area_name='" + area_name + '\'' +
                '}';
    }
}
