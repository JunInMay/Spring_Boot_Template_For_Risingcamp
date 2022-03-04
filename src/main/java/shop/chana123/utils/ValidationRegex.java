package shop.chana123.utils;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationRegex {
    public static boolean isRegexEmail(String target) {
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }
    public static boolean isRegexDate(String target) {
        String regex = "(((?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:(?:0?[13578]|1[02])(-)31)|(?:(?:0?[1,3-9]|1[0-2])(-)(?:29|30))))|(((?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))(-)(?:0?2(-)29))|((?:(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(-)(?:(?:0?[1-9])|(?:1[0-2]))(-)(?:0[1-9]|1\\d|2[0-8]))))";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.find();
    }

}

