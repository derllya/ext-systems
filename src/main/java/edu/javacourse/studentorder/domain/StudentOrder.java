package edu.javacourse.studentorder.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentOrder {
    private long studentOrderId;
    private StudentOrderStatus studentOrderStatus;
    private LocalDateTime studentOrderDate;
    private Adult husband;
    private Adult wife;
    private List<Child> children;
    private String MarriageCertificatedId;
    private RegisterOffice MarriageOffice;
    private LocalDate MarriageDate;

    public long getStudentOrderId() {
        return studentOrderId;
    }

    public void setStudentOrderId(long studentOrderId) {
        this.studentOrderId = studentOrderId;
    }

    public StudentOrderStatus getStudentOrderStatus() {
        return studentOrderStatus;
    }

    public void setStudentOrderStatus(StudentOrderStatus studentOrderStatus) {
        this.studentOrderStatus = studentOrderStatus;
    }

    public LocalDateTime getStudentOrderDate() {
        return studentOrderDate;
    }

    public void setStudentOrderDate(LocalDateTime studentOrderDate) {
        this.studentOrderDate = studentOrderDate;
    }

    public Adult getHusband() {
        return husband;
    }

    public void setHusband(Adult husband) {
        this.husband = husband;
    }

    public Adult getWife() {
        return wife;
    }

    public void setWife(Adult wife) {
        this.wife = wife;
    }

    public void addChild (Child child){
        if (children == null){
            children = new ArrayList<>(5);
        }
        children.add(child);
    }

    public List<Child> getChildren() {
        return children;
    }

    public String getMarriageCertificatedId() {
        return MarriageCertificatedId;
    }

    public void setMarriageCertificatedId(String marriageCertificatedId) {
        MarriageCertificatedId = marriageCertificatedId;
    }

    public RegisterOffice getMarriageOffice() {
        return MarriageOffice;
    }

    public void setMarriageOffice(RegisterOffice marriageOffice) {
        MarriageOffice = marriageOffice;
    }

    public LocalDate getMarriageDate() {
        return MarriageDate;
    }

    public void setMarriageDate(LocalDate marriageDate) {
        MarriageDate = marriageDate;
    }

    @Override
    public String toString() {
        return "StudentOrder{" +
                "studentOrderId=" + studentOrderId +
                ", studentOrderStatus=" + studentOrderStatus +
                ", studentOrderDate=" + studentOrderDate +
                ", husband=" + husband +
                ", wife=" + wife +
                ", children=" + children +
                ", MarriageCertificatedId='" + MarriageCertificatedId + '\'' +
                ", MarriageOffice=" + MarriageOffice +
                ", MarriageDate=" + MarriageDate +
                '}';
    }
}
