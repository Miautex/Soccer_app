package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Collection;

import pkgDatabase.Database;
import pkgData.Game;
import pkgData.Player;
import pkgException.InvalidLoginDataException;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoginListener;

public class LoginActivity extends AppCompatActivity implements OnClickListener, OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener {

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
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
        }
        catch (Exception ex) {
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            ExceptionNotification.notify(this, ex);
        }
    }

    /**
     * set all views by id
     */
    private void getAllViews(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
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
            Toast.makeText(this, getString(R.string.Error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
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
        catch (Exception e) {
            msg = e.getMessage();
        }
        finally {
            runOnUiThread(new ShowToastRunnable(getApplicationContext(), msg));
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
        runOnUiThread(new ShowToastRunnable(getApplicationContext(), ex.getMessage()));
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
        runOnUiThread(new ShowToastRunnable(getApplicationContext(), ex.getMessage()));
        ex.printStackTrace();
    }
}
