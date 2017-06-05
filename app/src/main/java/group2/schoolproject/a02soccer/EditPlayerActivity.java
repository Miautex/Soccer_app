package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import pkgData.Player;
import pkgData.PlayerPosition;
import pkgDatabase.Database;
import pkgDatabase.SetPasswordHandler;
import pkgDatabase.UpdatePlayerHandler;
import pkgDatabase.pkgListener.OnPlayerUpdatedListener;
import pkgDatabase.pkgListener.OnSetPasswordListener;
import pkgException.DuplicateUsernameException;
import pkgException.NameTooLongException;
import pkgException.NameTooShortException;
import pkgException.PasswordTooShortException;
import pkgException.UsernameTooLongException;
import pkgException.UsernameTooShortException;
import pkgMisc.NamePWValidator;

/**
 * @author Elias Santner
 */

public class EditPlayerActivity extends BaseActivity
        implements View.OnClickListener, OnPlayerUpdatedListener, OnSetPasswordListener {
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
    private ProgressBar pb = null;

    Database db = null;
    Player playerToEdit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();

            playerToEdit = (Player) this.getIntent().getSerializableExtra("player");

            if (playerToEdit == null) {
                throw new Exception("Please call activity with intent-extra 'player'");
            }

            if (db.getCurrentlyLoggedInPlayer().equals(playerToEdit)) {
                setTitle(R.string.title_activity_edit_player_own);
            }
            else {
                setTitle(R.string.title_activity_edit_player_admin);
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
        pb = (ProgressBar) findViewById(R.id.progressBar);
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


        if (!NamePWValidator.validate(edtName.getText().toString())) {
            throw new Exception(getString(R.string.msg_IllegalName));
        }

        if (!NamePWValidator.validate(edtPassword.getText().toString())) {
            throw new Exception(getString(R.string.msg_IllegalPassword));
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

                toggleProgressBar(true);

                if (!playerToEdit.isLocallySavedOnly()) {
                    db.update(playerToEdit, this);
                }
                else {
                    if (ckbUpdatePassword.isChecked()) {
                        db.updatePlayerLocally(playerToEdit, edtPassword.getText().toString());
                    }
                    else {
                        db.updatePlayerLocally(playerToEdit);
                    }
                    showMessage(getString(R.string.msg_DataSavedLocally));
                    toggleProgressBar(false);
                }
            }
        }
        catch (DuplicateUsernameException ex) {
            throw new DuplicateUsernameException(String.format(getString(R.string.msg_UsernameNotAvailable), playerToEdit.getUsername()));
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
        finally {
            toggleProgressBar(false);
        }
    }

    private void toggleProgressBar(boolean isEnabled) {
        if (isEnabled) {
            pb.setVisibility(View.VISIBLE);
        }
        else {
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPasswordFinished(SetPasswordHandler handler) {
        toggleProgressBar(false);
        if (handler.getException() == null) {
            showMessage(getString(R.string.msg_UpdatedUserData));
        }
        else {
            showMessage(getString(R.string.Error) + ": " + handler.getException().getMessage());
        }
    }

    @Override
    public void updatePlayerFinished(UpdatePlayerHandler handler) {
        try {
            if (handler.getException() == null) {
                if (ckbUpdatePassword.isChecked()) {
                    db.setPassword(playerToEdit, edtPassword.getText().toString(), this);
                } else {
                    toggleProgressBar(false);
                    showMessage(getString(R.string.msg_UpdatedUserData));
                }
            }
            else {
                toggleProgressBar(false);

                if (handler.getException().getClass().equals(DuplicateUsernameException.class)) {
                    showMessage(getString(R.string.Error) + ": " +
                            String.format(getString(R.string.msg_UsernameNotAvailable), handler.getPlayer().getUsername()));
                }
                else {
                    showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotUpdateUserData));
                }
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotUpdateUserData));
        }
    }
}
