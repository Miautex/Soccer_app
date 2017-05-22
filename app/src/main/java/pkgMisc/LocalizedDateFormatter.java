package pkgMisc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalizedDateFormatter {
    public static String format(Date date, Locale loc) {
        /*SimpleDateFormat f = new SimpleDateFormat("EEE, dd.MM.yyyy", Locale.ENGLISH);
        return  f.format(date);*/
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", loc);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, loc);
        return sdf.format(date) + " - " + df.format(date);
    }
}
