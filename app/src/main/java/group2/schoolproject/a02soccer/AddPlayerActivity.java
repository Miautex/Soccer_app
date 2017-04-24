package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pkgData.Database;
import pkgData.Player;


public class AddPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd = null;
    Button btnCancel = null;
    EditText edtName = null;
    EditText edtUsername = null;
    EditText edtPassword = null;
    CheckBox ckbIsAdmin = null;

    Database db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayer);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
        }
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllViews(){
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtPassword);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        ckbIsAdmin = (CheckBox) findViewById(R.id.ckbIsAdmin);
    }

    private void registrateEventHandlers(){
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnAdd) {
                onBtnAddClick();
                Toast.makeText(this, "Player added", Toast.LENGTH_SHORT).show();
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void onBtnAddClick() throws Exception {
        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            throw new Exception("Name, Username and Password may not be empty");
        }

        Player newPlayer = new Player(edtName.getText().toString(), edtUsername.getText().toString(), ckbIsAdmin.isChecked());

        db.insert(newPlayer);
        db.setPassword(newPlayer, edtPassword.getText().toString());
    }
}
