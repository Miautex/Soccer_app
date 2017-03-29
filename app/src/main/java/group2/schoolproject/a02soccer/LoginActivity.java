package group2.schoolproject.a02soccer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getAllViews();
        registrateEventHandlers();
    }

    /**
     * set all views by id
     */
    private void getAllViews(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
    }

    /**
     * registrate all EventHandlers
     */
    private void registrateEventHandlers(){
        btnLogin.setOnClickListener(this);
    }

    public void onClick(View arg0) {
        try {
            if (arg0.getId() == R.id.btnLogin) {
                tryLogin();
            }
        } catch (Exception e) {
            showToast("Error with onClick");
        }
    }

    /**
     * try to login and if successful open the MainActivity
     */
    private void tryLogin() throws Exception {
        //Datenbank Abfrage
        boolean success = false;
        String msg = "";

        success = Database.getInstance().login(edtUsername.getText().toString(), edtPassword.getText().toString());

        if(success){
            msg = "Login successful";
            openMainActivity();
        }
        else {
            msg = "Username or Password is wrong";
        }
        showToast(msg);
    }

    /**
     * opens MainActivity
     */
    private void openMainActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * shows Toast == MessageLine
     * @param text Message you want to display
     */
    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
