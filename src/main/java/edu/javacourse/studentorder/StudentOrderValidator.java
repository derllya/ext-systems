package edu.javacourse.studentorder;

import edu.javacourse.studentorder.dao.StudentDaoImpl;
import edu.javacourse.studentorder.domain.StudentOrder;
import edu.javacourse.studentorder.domain.children.AnswerChildren;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.student.AnswerStudent;
import edu.javacourse.studentorder.domain.wedding.AnswerWedding;
import edu.javacourse.studentorder.exception.DaoException;
import edu.javacourse.studentorder.mail.MailSender;
import edu.javacourse.studentorder.validator.ChildrenValidator;
import edu.javacourse.studentorder.validator.CityRegisterValidator;
import edu.javacourse.studentorder.validator.StudentValidator;
import edu.javacourse.studentorder.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private CityRegisterValidator cityRegisterValidator;
    private WeddingValidator weddingValidator;
    private StudentValidator studentValidator;
    private ChildrenValidator childrenValidator;
    private MailSender mailSender;

    public StudentOrderValidator(){
        cityRegisterValidator = new CityRegisterValidator();
        weddingValidator = new WeddingValidator();
        studentValidator = new StudentValidator();
        childrenValidator = new ChildrenValidator();
        mailSender = new MailSender();
    }

    public static void main(String[] args) {
        StudentOrderValidator sov = new StudentOrderValidator();
        sov.checkAll();
    }

    public void checkAll() {
        try {
            List<StudentOrder> soList = readStudentOrders();

            for (StudentOrder so : soList) { // присваиваем значение массива объекту и таким образом проходим весь массив
                checkOneOrder(so);
                System.out.println();
            }
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public List<StudentOrder> readStudentOrders() throws DaoException {
        return new StudentDaoImpl().getStudentOrders();
    }

    public void checkOneOrder (StudentOrder so){
        AnswerCityRegister cityAnswer = checkCityRegister(so);
        /*AnswerWedding weddingAnswer = checkWedding(so);
        AnswerChildren childrenAnswer = checkChildren(so);
        AnswerStudent studentAnswer = checkStudent(so);

        sendMail(so);*/
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        return cityRegisterValidator.checkCityRegister(so);
    }
    public AnswerWedding checkWedding(StudentOrder so) {
        return weddingValidator.checkWedding(so);
    }
    public AnswerChildren checkChildren(StudentOrder so) {
        return childrenValidator.checkChildren(so);
    }
    public AnswerStudent checkStudent(StudentOrder so) {
        return studentValidator.checkStudent(so);
        // можно еще так: return new edu.javacourse.studentorder.validator.StudentValidator().checkStudent(so)  - создали анонимный объект и вызвали его метод.
    }
    public void sendMail(StudentOrder so){
        mailSender.sendMail(so);
    }
}
