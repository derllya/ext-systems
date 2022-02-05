package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.student.AnswerStudent;
import edu.javacourse.studentorder.domain.StudentOrder;

public class StudentValidator {

    public String studentNumber;

    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("Студенты проверяются: " + studentNumber);
        return new AnswerStudent();
    }
}
