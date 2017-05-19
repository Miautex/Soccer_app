package pkgMisc;

import android.content.Context;

public class PxDpConverter {
    public static int toDp(int px, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return  (int)(px * density);
    }
}
