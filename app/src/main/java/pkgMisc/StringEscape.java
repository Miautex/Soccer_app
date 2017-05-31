package pkgMisc;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Marco Wilscher
 */

public class StringEscape {
    public static String escapeString(String s) {
        String result = s.replace("\"", ":q:");
        result = StringEscapeUtils.escapeJava(result);
        result = result.replace("\\", "\\\\");
        return result.replace(":q:", "\"");
    }
    public static String unescapeString(String s) {
        return StringEscapeUtils.unescapeJava(s);
    }
}
