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
import pkgException.DuplicateUsernameException;
import pkgMenu.DynamicMenuActivity;

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
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        ArrayList<PlayerPosition> playerPositions = new ArrayList<>();

        for (PlayerPosition pos: currPlayer.getPositions()) {
            playerPositions.add(pos);
        }

        if (playerPositions.contains(PlayerPosition.ATTACK)) {
            ckbPosAtk.setChecked(true);
        }
        if (playerPositions.contains(PlayerPosition.DEFENSE)) {
            ckbPosDef.setChecked(true);
        }
        if (playerPositions.contains(PlayerPosition.GOAL)) {
            ckbPosGoal.setChecked(true);
        }
        if (playerPositions.contains(PlayerPosition.MIDFIELD)) {
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
            Toast.makeText(this, getString(R.string.Error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void onBtnSaveClick() throws Exception {
        Player currPlayer = db.getCurrentlyLoggedInPlayer();
        boolean isSuccess = false;
        String msg = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterNameUsername));
        }

        if (!ckbPosMid.isChecked() && !ckbPosGoal.isChecked() && !ckbPosAtk.isChecked() && !ckbPosDef.isChecked()) {
            throw new Exception(getString(R.string.msg_SelectMinNumOfPositions));
        }

        Player updatedPlayer = new Player(currPlayer.getId(), edtUsername.getText().toString(),
                edtName.getText().toString(), currPlayer.isAdmin());

        for (PlayerPosition pp : getCheckedPlayerPositions()) {
            updatedPlayer.addPosition(pp);
        }

        try {
            isSuccess = db.update(updatedPlayer);

            if (isSuccess) {
                msg = getString(R.string.msg_UpdatedUserData);
            }
            else {
                msg = getString(R.string.msg_CouldNotUpdateUserData);
            }
        }
        catch (DuplicateUsernameException ex) {
            msg = ex.getMessage();
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
