package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import pkgDatabase.Database;
import pkgDatabase.LoadAllGamesHandler;
import pkgDatabase.LoadAllPlayersHandler;
import pkgDatabase.LoginHandler;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgException.InvalidLoginDataException;
import pkgWSA.Accessor;

public class LoginActivity extends BaseActivity
        implements OnClickListener, OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener {

    Button btnLogin = null;
    EditText edtPassword = null;
    EditText edtUsername = null;
    ProgressBar progressIndicator = null;

    Database db = null;
    Boolean arePlayersLoaded = false,
            areGamesLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        getAllViews();
        registrateEventHandlers();
        try {
            Accessor.init(getApplicationContext());
            db = Database.getInstance();
            db.initPreferences(this);

            setDefaultCredentials();
            if (db.isInitialLogin() && db.isAutologin()) {
                db.setInitialLogin(false);
                tryLogin();
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }


    /**
     * set all views by id
     */
    private void getAllViews(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUsername = (EditText) findViewById(R.id.edtName);
        progressIndicator = (ProgressBar) findViewById(R.id.progressIndicator);
    }

    /**
     * registrate all EventHandlers
     */
    private void registrateEventHandlers(){
        btnLogin.setOnClickListener(this);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_ENTER){
                    try {
                        tryLogin();
                    }catch(Exception ex){
                        showMessage(getString(R.string.Error) + ": " + ex.getMessage());
                    }
                }
                return false;
            }
        });
    }

    public void onClick(View arg0) {
        try {
            if (arg0.getId() == R.id.btnLogin) {
                tryLogin();
            }
        } catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
        }
    }

    private void tryLogin() {
        try {
            if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                throw new Exception(getString(R.string.msg_EnterUsernamePassword));
            }
            else {
                toggleLoginInputs(false);
                db.login(edtUsername.getText().toString(), edtPassword.getText().toString(), this);
            }
        }
        catch (Exception ex) {
            toggleLoginInputs(true);
            showMessage(ex.getMessage());
        }
    }

    /**
     * opens MainActivity
     */
    private void openMainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void toggleLoginInputs(final boolean setEnabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnLogin.setEnabled(setEnabled);
                edtUsername.setEnabled(setEnabled);
                edtPassword.setEnabled(setEnabled);
                if (setEnabled) {
                    progressIndicator.setVisibility(View.GONE);
                }
                else {
                    progressIndicator.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setDefaultCredentials() {
        edtUsername.setText(db.getStoredUsername());
        edtPassword.setText(db.getStoredPassword());
    }

    @Override
    public void loginFinished(LoginHandler handler) {
        try {
            if (handler.getException() == null) {
                db.loadAllPlayers(this);
                db.loadAllGames(this);
            }
            else {
                String msg = null;
                toggleLoginInputs(true);

                try {
                    throw handler.getException();
                }
                catch (SocketTimeoutException e) {
                    msg = getString(R.string.msg_ConnectionTimeout);
                }
                catch (ConnectException e) {
                    msg = getString(R.string.msg_NetworkUnreachable);
                }
                catch (InvalidLoginDataException e) {
                    msg = getString(R.string.msg_UsernameOrPasswordInvalid);
                }
                catch (FileNotFoundException e) {
                    msg = getString(R.string.msg_NetworkUnreachable);
                }
                catch (Exception e) {
                    msg = getString(R.string.msg_CannotConnectToWebservice);
                }
                finally {
                    showMessage(msg);
                }
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CannotConnectToWebservice));
        }
    }

    @Override
    public void loadGamesFinished(LoadAllGamesHandler handler) {
        if (handler.getException() == null) {
            if (arePlayersLoaded) {
                toggleLoginInputs(true);
                openMainActivity();
            }
            else {
                areGamesLoaded = true;
            }
        }
        else {
            toggleLoginInputs(true);
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CannotConnectToWebservice));
        }
    }

    @Override
    public void loadPlayersFinished(LoadAllPlayersHandler handler) {
        if (handler.getException() == null) {
            if (areGamesLoaded) {
                toggleLoginInputs(true);
                openMainActivity();
            }
            else {
                arePlayersLoaded = true;
            }
        }
        else {
            toggleLoginInputs(true);
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CannotConnectToWebservice));
        }
    }
}
