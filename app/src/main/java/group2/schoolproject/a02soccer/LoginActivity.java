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
import java.util.Collection;

import pkgData.Game;
import pkgData.Player;
import pkgDatabase.Database;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgException.InvalidLoginDataException;

public class LoginActivity extends BaseActivity implements OnClickListener, OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener {

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
            db = Database.getInstance();
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
            ExceptionNotification.notify(this, ex);
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
                    }catch(Exception x){
                        ExceptionNotification.notify(getApplicationContext(), x);
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
    public void loginSuccessful(String username) {
        try {
            db.loadAllPlayers(this);
            db.loadAllGames(this);
        }
        catch (Exception ex) {
            loadPlayersFailed(ex);
        }
    }

    @Override
    public void loginFailed(Exception ex) {
        String msg = null;

        toggleLoginInputs(true);

        try {
            throw ex;
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

    @Override
    public void loadPlayersSuccessful(Collection<Player> players) {
        if (areGamesLoaded) {
            toggleLoginInputs(true);
            openMainActivity();
        }
        else {
            arePlayersLoaded = true;
        }
    }

    @Override
    public void loadPlayersFailed(Exception ex) {
        toggleLoginInputs(true);
        showMessage(ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public void loadGamesSuccessful(Collection<Game> games) {
        if (arePlayersLoaded) {
            toggleLoginInputs(true);
            openMainActivity();
        }
        else {
            areGamesLoaded = true;
        }
    }

    @Override
    public void loadGamesFailed(Exception ex) {
        toggleLoginInputs(true);
        showMessage(ex.getMessage());
        ex.printStackTrace();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
