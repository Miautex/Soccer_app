package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Martin on 28.03.2017.
 */

public class EditPlayerActivity extends AppCompatActivity{
    Button btnSave = null;
    Button btnCancel = null;
    EditText edtName = null;
    EditText edtUsername = null;
    CheckBox ckbPosMid = null;
    CheckBox ckbPosGoal = null;
    CheckBox ckbPosDef = null;
    CheckBox ckbPosAtk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getAllViews();
        registrateEventHandlers();
    }

    private void getAllViews(){
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        ckbPosAtk = (CheckBox) findViewById(R.id.ckbPosAtk);
        ckbPosDef = (CheckBox) findViewById(R.id.ckbPosDef);
        ckbPosGoal = (CheckBox) findViewById(R.id.ckbPosGoal);
        ckbPosMid = (CheckBox) findViewById(R.id.ckbPosMid);
    }

    private void registrateEventHandlers(){

    }
}
