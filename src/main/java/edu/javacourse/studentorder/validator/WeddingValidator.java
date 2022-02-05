package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.wedding.AnswerWedding;
import edu.javacourse.studentorder.domain.StudentOrder;

public class WeddingValidator {

    public String passport;

    public AnswerWedding checkWedding(StudentOrder so) {
        System.out.println("Wedding запущен, " + "Паспорт = " + passport);
        AnswerWedding ans = new AnswerWedding();
        return ans;
    }
}
