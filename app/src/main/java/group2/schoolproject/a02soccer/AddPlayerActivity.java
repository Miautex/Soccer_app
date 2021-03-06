package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import pkgData.Player;
import pkgData.PlayerPosition;
import pkgDatabase.Database;
import pkgDatabase.pkgHandler.InsertPlayerHandler;
import pkgDatabase.pkgHandler.SetPasswordHandler;
import pkgDatabase.pkgListener.OnPlayerInsertedListener;
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

public class AddPlayerActivity extends BaseActivity
        implements View.OnClickListener, OnPlayerInsertedListener, OnSetPasswordListener {
    private Button btnAdd = null;
    private Button btnCancel = null;
    private EditText edtName = null;
    private EditText edtUsername = null;
    private EditText edtPassword = null;
    private CheckBox ckbIsAdmin = null;
    private ProgressBar pb = null;

    private Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayer);
        setTitle(R.string.title_activity_add_player);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
            db.setContext(this);


            //Exit activity when user is not an admin (shouldn't happen)
            if (!db.getCurrentlyLoggedInPlayer().isAdmin()) {
                this.finish();
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void getAllViews(){
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        ckbIsAdmin = (CheckBox) findViewById(R.id.ckbIsAdmin);
        pb = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void registrateEventHandlers(){
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void onBtnAddClick(boolean isOnline) throws Exception {
        Player newPlayer = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterNameUsernamePassword));
        }

        if (!NamePWValidator.validate(edtName.getText().toString())) {
            throw new Exception(getString(R.string.msg_IllegalName));
        }

        if (!NamePWValidator.validate(edtPassword.getText().toString())) {
            throw new Exception(getString(R.string.msg_IllegalPassword));
        }

        try {
            if (edtPassword.getText().length() < Database.MIN_LENGTH_PASSWORD) {
                throw new PasswordTooShortException(Database.MIN_LENGTH_PASSWORD);
            }
            else {
                newPlayer = new Player(edtUsername.getText().toString(), edtName.getText().toString(), ckbIsAdmin.isChecked());
                newPlayer.addPosition(PlayerPosition.ATTACK);
                newPlayer.addPosition(PlayerPosition.DEFENSE);
                newPlayer.addPosition(PlayerPosition.MIDFIELD);
                newPlayer.addPosition(PlayerPosition.GOAL);

                toggleProgressBar(true);

                if (isOnline) {
                    db.insert(newPlayer, this);
                }
                else {
                    db.insertPlayerLocally(newPlayer, edtPassword.getText().toString());
                    showMessage(getString(R.string.msg_DataSavedLocally));
                    toggleProgressBar(false);
                    openMainActivity();
                }
            }

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
        catch (DuplicateUsernameException ex) {
            toggleProgressBar(false);
            throw new DuplicateUsernameException(String.format(getString(R.string.msg_UsernameNotAvailable),
                    newPlayer.getUsername()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
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

    private void openMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnAdd) {
                onBtnAddClick(db.isOnline());
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
        } catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setPasswordFinished(SetPasswordHandler handler) {
        toggleProgressBar(false);
        if (handler.getException() == null) {
            showMessage(String.format(getString(R.string.msg_PlayerAdded), handler.getPlayer().getName()));
            openMainActivity();
        }
        else {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotSetPassword));
        }
    }

    @Override
    public void insertPlayerFinished(InsertPlayerHandler handler) {
        try {
            if (handler.getException() == null) {
                db.setPassword(handler.getPlayer(), edtPassword.getText().toString(), this);
            }
            else {
                toggleProgressBar(false);

                if (handler.getException().getClass().equals(DuplicateUsernameException.class)) {
                    showMessage(getString(R.string.Error) + ": " +
                            String.format(getString(R.string.msg_UsernameNotAvailable), handler.getPlayer().getUsername()));
                }
                else {
                    //if insert fails because of connection issues insert player locally
                    onBtnAddClick(false);
                }
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }
}

