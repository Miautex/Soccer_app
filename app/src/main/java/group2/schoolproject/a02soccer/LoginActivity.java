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
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;

import pkgDatabase.Database;
import pkgDatabase.LoadAllGamesHandler;
import pkgDatabase.LoadAllPlayersHandler;
import pkgDatabase.LoginHandler;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgException.InvalidLoginDataException;
import pkgException.NoLocalDataException;

public class LoginActivity extends BaseActivity
        implements OnClickListener, OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener {

    Button btnLogin = null;
    EditText edtPassword = null;
    EditText edtUsername = null;
    ProgressBar progressIndicator = null;

    Database db = null;
    Boolean arePlayersLoaded = false,
            areGamesLoaded = false,
            isOnline = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);
        getAllViews();
        registrateEventHandlers();
        try {
            db = Database.getInstance();

            /*setDefaultCredentials();
            if (db.isInitialLogin() && db.isAutologin()) {
                db.setInitialLogin(false);
                tryLogin();
            }*/

            String username = (String) this.getIntent().getSerializableExtra("username");
            String password = (String) this.getIntent().getSerializableExtra("password");
            Boolean doAutoLogin = (Boolean) this.getIntent().getSerializableExtra("doAutoLogin");
            isOnline = (Boolean) this.getIntent().getSerializableExtra("isOnline");

            if (isOnline == null) {
                isOnline = true;
            }

            if (username != null) {
                edtUsername.setText(username);
            }
            if (password != null) {
                edtPassword.setText(password);
            }

            if (doAutoLogin != null && doAutoLogin) {
                tryLogin(isOnline);
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
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    try {
                        tryLogin(isOnline);
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
                tryLogin(isOnline);
            }
        } catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
        }
    }

    private void tryLogin(Boolean isOnline) {
        try {
            if (edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
                throw new Exception(getString(R.string.msg_EnterUsernamePassword));
            }
            else {
                toggleLoginInputs(false);

                if (isOnline) {
                    db.login(edtUsername.getText().toString(), edtPassword.getText().toString(), this);
                }
                else {
                    toggleLoginInputs(true);
                    try {
                        if (db.loginLocal(edtUsername.getText().toString(), edtPassword.getText().toString(), this)) {
                            openMainActivity();
                        } else {
                            showMessage(getString(R.string.msg_UsernameOrPasswordInvalid));
                        }
                    }
                    catch (NoLocalDataException ex) {
                        showMessage(getString(R.string.msg_CannotConnectToWebservice));
                    }
                }
            }
        }
        catch (Exception ex) {
            toggleLoginInputs(true);
            showMessage(ex.getMessage());
            ex.printStackTrace();
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
                catch (InvalidLoginDataException e) {
                    msg = getString(R.string.msg_UsernameOrPasswordInvalid);
                }
                catch (ConnectException | FileNotFoundException | SocketTimeoutException | NoRouteToHostException e) {
                    tryLogin(false);
                }
                catch (Exception e) {
                    msg = getString(R.string.msg_CannotConnectToWebservice);
                }
                finally {
                    if (msg != null) {
                        showMessage(msg);
                    }
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
