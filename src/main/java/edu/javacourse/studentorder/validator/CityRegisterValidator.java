package edu.javacourse.studentorder.validator;

import edu.javacourse.studentorder.domain.Child;
import edu.javacourse.studentorder.domain.Person;
import edu.javacourse.studentorder.domain.StudentOrder;
import edu.javacourse.studentorder.domain.register.AnswerCityRegister;
import edu.javacourse.studentorder.domain.register.AnswerCityRegisterItem;
import edu.javacourse.studentorder.domain.register.CityRegisterResponse;
import edu.javacourse.studentorder.exception.CityRegisterException;
import edu.javacourse.studentorder.exception.TransportException;
import edu.javacourse.studentorder.validator.register.CityRegisterChecker;
import edu.javacourse.studentorder.validator.register.RealCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator {

    public static final String IN_CODE = "NO_GRN";
    public String hostName;
    protected int port;
    private String login;
    String password;
    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new RealCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();

            ans.addItem(checkPerson(so.getHusband())); // если ловим ошибку, то сразу идем в блок catch, в try не возвращаемся
            ans.addItem(checkPerson(so.getWife()));
            List<Child> children = so.getChildren();
            for (Child child : children){
                ans.addItem(checkPerson(child));
            }

        return ans;
    }
    private AnswerCityRegisterItem checkPerson(Person person){
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;
        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isRegistered() ?
                    AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException exception) {
            exception.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(exception.getCode(), exception.getMessage());
        } catch (TransportException exception) {
            exception.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, exception.getMessage());
        }
        catch (Exception exception){
            exception.printStackTrace(System.out);
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, exception.getMessage());
        }
        AnswerCityRegisterItem ans = new AnswerCityRegisterItem(status, person, error);

        return ans;
    }
}
