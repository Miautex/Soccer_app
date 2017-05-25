package pkgMisc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamePWValidator {
    private static Pattern usernamePattern = Pattern.compile("\\A\\p{ASCII}*\\z");
    private static Matcher matcher;

    public static boolean validate(String str) {

        matcher = usernamePattern.matcher(str);
        return true;    //matcher.matches();
    }
}
