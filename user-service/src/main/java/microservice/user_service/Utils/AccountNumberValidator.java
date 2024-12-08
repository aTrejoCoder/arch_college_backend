package microservice.user_service.Utils;

import java.util.regex.Pattern;

public class AccountNumberValidator {

    public static boolean isStudentAccountNumber(String accountNumber) {
        Pattern pattern = Pattern.compile("^\\d{8}-\\d$");

        return pattern.matcher(accountNumber).matches();
    }
}
