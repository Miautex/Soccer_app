package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pkgData.Database;
import pkgData.Player;
import pkgData.PlayerPosition;

/**
 * Created by Martin on 28.03.2017.
 */

public class EditPlayerActivity extends DynamicMenuActivity implements View.OnClickListener {
    Button btnSave = null;
    Button btnCancel = null;
    EditText edtName = null;
    EditText edtUsername = null;
    CheckBox ckbPosMid = null;
    CheckBox ckbPosGoal = null;
    CheckBox ckbPosDef = null;
    CheckBox ckbPosAtk = null;

    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
            initTextFields();
            initCheckboxes();
        }
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void initTextFields() {
        Player currPlayer = db.getCurrentlyLoggedInPlayer();

        edtName.setText(currPlayer.getName());
        edtUsername.setText(currPlayer.getUsername());
    }

    private void initCheckboxes() {
        Player currPlayer = db.getCurrentlyLoggedInPlayer();

        if (currPlayer.getPositions().contains(PlayerPosition.ATTACK)) {
            ckbPosAtk.setChecked(true);
        }
        if (currPlayer.getPositions().contains(PlayerPosition.DEFENSE)) {
            ckbPosDef.setChecked(true);
        }
        if (currPlayer.getPositions().contains(PlayerPosition.GOAL)) {
            ckbPosGoal.setChecked(true);
        }
        if (currPlayer.getPositions().contains(PlayerPosition.MIDFIELD)) {
            ckbPosMid.setChecked(true);
        }
    }

    private ArrayList<PlayerPosition> getCheckedPlayerPositions() {
        ArrayList<PlayerPosition> positions = new ArrayList<>();

        if (ckbPosAtk.isChecked()) {
            positions.add(PlayerPosition.ATTACK);
        }
        if (ckbPosDef.isChecked()) {
            positions.add(PlayerPosition.DEFENSE);
        }
        if (ckbPosGoal.isChecked()) {
            positions.add(PlayerPosition.GOAL);
        }
        if (ckbPosMid.isChecked()) {
            positions.add(PlayerPosition.MIDFIELD);
        }

        return  positions;
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnSave) {
                onBtnSaveClick();
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onBtnSaveClick() throws Exception {
        Player currPlayer = db.getCurrentlyLoggedInPlayer();
        boolean isSuccess = false;
        String msg = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()) {
            throw new Exception("Name and Username may not be empty");
        }

        currPlayer.setUsername(edtUsername.getText().toString());
        currPlayer.setName(edtName.getText().toString());

        for (PlayerPosition pp : currPlayer.getPositions()) {
            currPlayer.removePosition(pp);
        }

        for (PlayerPosition pp : getCheckedPlayerPositions()) {
            currPlayer.addPosition(pp);
        }

        isSuccess = db.update(currPlayer);

        if (isSuccess) {
            msg = "Successfully updated userdata";
        }
        else {
            msg = "Could not update userdata";
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
