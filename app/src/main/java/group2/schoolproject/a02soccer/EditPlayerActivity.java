package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import pkgData.PlayerPosition;
import pkgDatabase.Database;
import pkgData.Player;
import pkgException.DuplicateUsernameException;
import pkgException.NameTooLongException;
import pkgException.NameTooShortException;
import pkgException.PasswordTooShortException;
import pkgException.UsernameTooLongException;
import pkgException.UsernameTooShortException;


public class EditPlayerActivity extends BaseActivity implements View.OnClickListener {
    Button btnSave = null,
            btnCancel = null;
    EditText edtName = null,
            edtUsername = null,
            edtPassword = null;
    CheckBox ckbIsAdmin = null,
            ckbUpdatePassword = null,
            ckbPosMid = null,
            ckbPosGoal = null,
            ckbPosDef = null,
            ckbPosAtk = null;

    Database db = null;
    Player playerToEdit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);
        setTitle(R.string.title_activity_edit_player_own);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();

            playerToEdit = (Player) this.getIntent().getSerializableExtra("player");

            if (playerToEdit == null) {
                throw new Exception("Please call activity with intent-extra 'player'");
            }

            initTextFields(playerToEdit);
            initCheckboxes(playerToEdit);
            initSetAdminCkb(playerToEdit);
        }
        catch (Exception ex) {
            showMessage( getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void getAllViews(){
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        ckbIsAdmin = (CheckBox) findViewById(R.id.ckbIsAdmin);
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

    private void initTextFields(Player player) {
        edtName.setText(player.getName());
        edtUsername.setText(player.getUsername());
        ckbIsAdmin.setChecked(player.isAdmin());
    }

    private void initSetAdminCkb(Player player) {
        if (db.getCurrentlyLoggedInPlayer().equals(player) || !db.getCurrentlyLoggedInPlayer().isAdmin()) {
            ckbIsAdmin.setVisibility(View.GONE);
        }
    }

    private void initCheckboxes(Player player) {
        ArrayList<PlayerPosition> playerPositions = new ArrayList<>();

        for (PlayerPosition pos: player.getPositions()) {
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
            showMessage( getString(R.string.Error) + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void toggleEdtPassword(boolean enabled) {
        edtPassword.setEnabled(enabled);
    }

    private void onBtnSaveClick() throws Exception {
        boolean isSuccess = false;
        String msg = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterNameUsername));
        }
        else if (ckbUpdatePassword.isChecked() && edtPassword.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterPassword));
        }
        else if (getCheckedPlayerPositions().size() == 0) {
            throw new Exception(getString(R.string.msg_SelectMinNumOfPositions));
        }

        try {

            if (ckbUpdatePassword.isChecked() && edtPassword.getText().length() < Database.MIN_LENGTH_PASSWORD) {
                throw new PasswordTooShortException(Database.MIN_LENGTH_PASSWORD);
            }
            else {
                playerToEdit.setName(edtName.getText().toString());
                playerToEdit.setUsername(edtUsername.getText().toString());
                playerToEdit.setAdmin(ckbIsAdmin.isChecked());

                playerToEdit.getPositions().clear();

                for (PlayerPosition pp : getCheckedPlayerPositions()) {
                    playerToEdit.addPosition(pp);
                }

                isSuccess = db.update(playerToEdit);

                if (isSuccess) {
                    msg = getString(R.string.msg_UpdatedUserData);

                    if (ckbUpdatePassword.isChecked()) {
                        db.setPassword(playerToEdit, edtPassword.getText().toString());
                    }
                } else {
                    msg = getString(R.string.msg_CouldNotUpdateUserData);
                }
            }
        }
        catch (DuplicateUsernameException ex) {
            msg = String.format(getString(R.string.msg_UsernameNotAvailable), playerToEdit.getUsername());
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
        catch (NameTooShortException ex) {
            throw new NameTooShortException(String.format(getString(R.string.msg_NameTooShort),
                    ex.getMinLenght()), ex.getMinLenght());
        }
        catch (UsernameTooShortException ex) {
            throw new UsernameTooShortException(String.format(getString(R.string.msg_UsernameTooShort),
                    ex.getMinLenght()), ex.getMinLenght());
        }
        catch (PasswordTooShortException ex) {
            throw new PasswordTooShortException(String.format(getString(R.string.msg_PasswordTooShort),
                    ex.getMinLenght()), ex.getMinLenght());
        }

        showMessage(msg);
    }
}
