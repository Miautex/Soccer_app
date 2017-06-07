package group2.schoolproject.a02soccer;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import pkgListeners.OnOkDialogButtonPressedListener;

/**
 * @author Martin Sonnberger
 */

public class OkDialog extends Dialog implements
        android.view.View.OnClickListener {

    private OnOkDialogButtonPressedListener listener;
    private Button btnOk;
    private TextView msg;
    private String text;

    public OkDialog(OnOkDialogButtonPressedListener listener, String text) {
        super((Activity) listener);
        this.listener = listener;
        this.text = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_ok);

        msg = (TextView) findViewById(R.id.txvMessage);
        btnOk = (Button) findViewById(R.id.btnOk);

        msg.setText(text);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOk:
                listener.okDialogButtonPressed();
                break;
        }
        dismiss();
    }
}