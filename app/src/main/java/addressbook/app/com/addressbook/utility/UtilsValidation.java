package addressbook.app.com.addressbook.utility;

import androidx.appcompat.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsValidation {

    public static boolean validateEmptyEditText(AppCompatEditText et_advertise) {
        return et_advertise.getText().toString().trim().isEmpty();
    }

    public static boolean validateEmail(AppCompatEditText et_email) {
        String email = et_email.getText().toString().trim();
        return (email.isEmpty() || !isValidEmail(email));
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidWebUrl(String url) {
        return !TextUtils.isEmpty(url) && !Patterns.WEB_URL.matcher(url).matches();
    }


    public static boolean validatePassword(AppCompatEditText et_password) {
        String password = et_password.getText().toString().trim();
        return (password.isEmpty() || !isValidPassword(password));
    }


    public static boolean validateConfirmPassword(AppCompatEditText et_password, AppCompatEditText et_confirm_password) {
        return !et_password.getText().toString().trim().equals(et_confirm_password.getText().toString().trim());
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^.*(?=.{8,})(?=.*\\d)(?=.*[a-zA-Z]).*$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean validatePhoneNumber(AppCompatEditText et_phone_number) {
        String cardNumber = et_phone_number.getText().toString().trim();
        return (cardNumber.isEmpty() || cardNumber.length() != 10);
    }


}
