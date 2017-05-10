package group2.schoolproject.a02soccer;

import android.content.Context;
import android.widget.Toast;

public class ShowToastRunnable implements Runnable {
    private Context ctx = null;
    private String msg = null;

    public ShowToastRunnable(Context context, String message) {
        this.ctx = context;
        this.msg = message;
    }

    @Override
    public void run() {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
