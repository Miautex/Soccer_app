package group2.schoolproject.a02soccer;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import pkgListeners.OnDeleteDialogButtonPressedListener;

public class ConfirmDeleteDialog extends Dialog implements
        android.view.View.OnClickListener {

    private OnDeleteDialogButtonPressedListener listener;
    private Dialog dialog;
    private Button btnYes, btnNo;
    private TextView msg;
    private Object selectedObject;
    private String text;

    public ConfirmDeleteDialog(Object selectedObject, OnDeleteDialogButtonPressedListener listener, String text) {
        super((Activity) listener);
        this.listener = listener;
        this.selectedObject = selectedObject;
        this.text = text;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirmdelete);

        msg = (TextView) findViewById(R.id.txvMessage);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnNo = (Button) findViewById(R.id.btnNo);

        msg.setText(text);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnYes:
                listener.deleteDialogButtonPressed(selectedObject, true);
                break;
            case R.id.btnNo:
                listener.deleteDialogButtonPressed(selectedObject, false);
                break;
            default:
                break;
        }
        dismiss();
    }
}