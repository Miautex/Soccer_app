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
    private Button yes,
            no;
    private TextView txt_dia;
    private Object selectedObject;
    private String title;

    public ConfirmDeleteDialog(Object selectedObject, OnDeleteDialogButtonPressedListener listener, String title) {
        super((Activity) listener, R.style.dlgConfirmDeleteStyle);
        this.listener = listener;
        this.selectedObject = selectedObject;
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm_delete);

        txt_dia = (TextView) findViewById(R.id.txt_dia);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);

        txt_dia.setText(title);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                listener.deleteDialogButtonPressed(selectedObject, true);
                break;
            case R.id.btn_no:
                listener.deleteDialogButtonPressed(selectedObject, false);
                break;
            default:
                break;
        }
        dismiss();
    }
}