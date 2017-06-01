package pkgMisc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Elias Santner
 */

public class LocalizedDateFormatter {
    public static String format(Date date, Locale loc) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", loc);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        return sdf.format(date) + " - " + df.format(date);
    }
}
