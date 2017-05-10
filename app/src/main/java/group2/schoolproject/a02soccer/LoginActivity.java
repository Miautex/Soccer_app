package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pkgData.Database;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    Button btnLogin = null;
    EditText edtPassword = null;
    EditText edtUsername = null;

    Database db = null;

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
            else if (db.login(edtUsername.getText().toString(), edtPassword.getText().toString())) {
                openMainActivity();
            }
            else {
                throw new Exception(getString(R.string.msg_UsernameOrPasswordInvalid));
            }
        }
        catch (Exception ex) {
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
}
