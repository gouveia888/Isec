package validation;

public class UserValidator {

    public static boolean isEmail(String email){
        System.out.println(email);
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

}
