package pkgMisc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator {
    private static Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]*$");
    private static Matcher matcher;

    public static boolean validate(String username) {

        matcher = usernamePattern.matcher(username);
        return matcher.matches();
    }
}
