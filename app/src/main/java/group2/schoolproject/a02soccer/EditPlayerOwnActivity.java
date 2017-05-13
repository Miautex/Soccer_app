package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pkgDatabase.Database;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgException.CouldNotSetPlayerPositionsException;
import pkgException.DuplicateUsernameException;
import pkgException.NameTooLongException;
import pkgException.UsernameTooLongException;
import pkgMenu.DynamicMenuActivity;

/**
 * Created by Martin on 28.03.2017.
 */

public class EditPlayerOwnActivity extends DynamicMenuActivity implements View.OnClickListener {
    Button btnSave = null,
            btnCancel = null;
    EditText edtName = null,
             edtUsername = null,
            edtPassword = null;
    CheckBox ckbPosMid = null,
            ckbPosGoal = null,
            ckbPosDef = null,
            ckbPosAtk = null,
            ckbUpdatePassword = null;

    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer_own);
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
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        ckbUpdatePassword = (CheckBox) findViewById(R.id.txvPassword);
        ckbPosAtk = (CheckBox) findViewById(R.id.ckbPosAtk);
        ckbPosDef = (CheckBox) findViewById(R.id.ckbPosDef);
        ckbPosGoal = (CheckBox) findViewById(R.id.ckbPosGoal);
        ckbPosMid = (CheckBox) findViewById(R.id.ckbPosMid);
    }

    private void registrateEventHandlers(){
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        ckbUpdatePassword.setOnClickListener(this);
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
            else if (v.getId() == R.id.txvPassword) {
                toggleEdtPassword(ckbUpdatePassword.isChecked());
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.Error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void toggleEdtPassword(boolean enabled) {
        edtPassword.setEnabled(enabled);
    }

    private void onBtnSaveClick() throws Exception {
        Player currPlayer = db.getCurrentlyLoggedInPlayer(),
                updatedPlayer = null;
        boolean isSuccess = false;
        String msg = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterNameUsername));
        }
        else if (ckbUpdatePassword.isChecked() && edtPassword.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterPassword));
        }
        else if (!ckbPosMid.isChecked() && !ckbPosGoal.isChecked() && !ckbPosAtk.isChecked() && !ckbPosDef.isChecked()) {
            throw new Exception(getString(R.string.msg_SelectMinNumOfPositions));
        }

        try {
            updatedPlayer = new Player(currPlayer.getId(), edtUsername.getText().toString(),
                    edtName.getText().toString(), currPlayer.isAdmin());

            for (PlayerPosition pp : getCheckedPlayerPositions()) {
                updatedPlayer.addPosition(pp);
            }
            isSuccess = db.update(updatedPlayer);

            if (isSuccess) {
                msg = getString(R.string.msg_UpdatedUserData);

                if (ckbUpdatePassword.isChecked()) {
                    db.setPassword(updatedPlayer, edtPassword.getText().toString());
                }
            }
            else {
                msg = getString(R.string.msg_CouldNotUpdateUserData);
            }
        }
        catch (DuplicateUsernameException ex) {
            msg = String.format(getString(R.string.msg_UsernameNotAvailable), updatedPlayer.getUsername());
        }
        catch (CouldNotSetPlayerPositionsException ex) {
            msg = getApplicationContext().getString(R.string.msg_CouldNotSetPositions);
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(getString(R.string.msg_IllegalUsername));
        }
        catch (NameTooLongException ex) {
            throw new NameTooLongException(String.format(getString(R.string.msg_NameTooLong),
                    ex.getMaxLenght()), ex.getMaxLenght());
        }
        catch (UsernameTooLongException ex) {
            throw new UsernameTooLongException(String.format(getString(R.string.msg_UsernameTooLong),
                    ex.getMaxLenght()), ex.getMaxLenght());
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
