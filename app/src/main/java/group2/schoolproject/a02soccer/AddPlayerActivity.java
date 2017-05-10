package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pkgData.Database;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgException.DuplicateUsernameException;
import pkgMenu.DynamicMenuActivity;


public class AddPlayerActivity extends DynamicMenuActivity implements View.OnClickListener {
    Button btnAdd = null;
    Button btnCancel = null;
    EditText edtName = null;
    EditText edtUsername = null;
    EditText edtPassword = null;
    CheckBox ckbIsAdmin = null;

    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplayer);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
        }
        catch (Exception ex) {
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllViews(){
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtNewPassword);
        ckbIsAdmin = (CheckBox) findViewById(R.id.ckbIsAdmin);
    }

    private void registrateEventHandlers(){
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void onBtnAddClick() throws Exception {
        Player newPlayer = null;

        if (edtName.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterNameUsernamePassword));
        }

        try {
            newPlayer = new Player(edtUsername.getText().toString(), edtName.getText().toString(), ckbIsAdmin.isChecked());
            newPlayer.addPosition(PlayerPosition.ATTACK);
            newPlayer.addPosition(PlayerPosition.DEFENSE);
            newPlayer.addPosition(PlayerPosition.MIDFIELD);
            newPlayer.addPosition(PlayerPosition.GOAL);

            Player remote_newPlayer = db.insert(newPlayer);
            db.setPassword(remote_newPlayer, edtPassword.getText().toString());
            Toast.makeText(this, String.format(getString(R.string.msg_PlayerAdded), remote_newPlayer.getName()), Toast.LENGTH_SHORT).show();
        }
        catch (DuplicateUsernameException ex) {
            throw new DuplicateUsernameException(String.format(getString(R.string.msg_UsernameNotAvailable), newPlayer.getUsername()));
        }
        catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(getString(R.string.msg_IllegalUsername));
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnAdd) {
                onBtnAddClick();
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.Error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

