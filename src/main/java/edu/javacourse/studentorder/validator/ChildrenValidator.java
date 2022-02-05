package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.children.AnswerChildren;
import edu.javacourse.studentorder.domain.StudentOrder;

public class ChildrenValidator {

    public String childrenCount;

    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("Children check is running " + childrenCount);
        AnswerChildren ans = new AnswerChildren();
        return ans;
    }
}
