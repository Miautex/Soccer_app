package group2.schoolproject.a02soccer;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Elias on 29.03.2017.
 */

public class Utilities {
    public static void showToast(String text, int duration, Context context) {
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
