package group2.schoolproject.a02soccer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import pkgData.Database;
import pkgData.Player;
import pkgException.DuplicateUsernameException;
import pkgMenu.DynamicMenuActivity;


public class EditPlayerAdminActivity extends DynamicMenuActivity implements View.OnClickListener {
    Button btnSave = null,
            btnCancel = null;
    EditText edtName = null,
            edtUsername = null,
            edtPassword = null;
    CheckBox ckbIsAdmin = null,
            ckbUpdatePassword = null;

    Database db = null;
    Player playerToEdit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editplayer_admin);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();

            playerToEdit = (Player) this.getIntent().getSerializableExtra("player");

            if (playerToEdit == null) {
                throw new Exception("Please call activity with intent-extra 'player'");
            }

            initTextFields(playerToEdit);
        }
        catch (Exception ex) {
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllViews(){
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        edtName = (EditText) findViewById(R.id.edtName);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtNewPassword);
        ckbIsAdmin = (CheckBox) findViewById(R.id.ckbIsAdmin);
        ckbUpdatePassword = (CheckBox) findViewById(R.id.ckbUpdatePassword);
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

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.btnSave) {
                onBtnSaveClick();
            }
            else if (v.getId() == R.id.btnCancel) {
                this.finish();
            }
            else if (v.getId() == R.id.ckbUpdatePassword) {
                toggleEdtPassword(ckbUpdatePassword.isChecked());
            }
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.Error) + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (ckbUpdatePassword.isChecked() && edtPassword.getText().toString().isEmpty()) {
            throw new Exception(getString(R.string.msg_EnterPassword));
        }

        playerToEdit.setName(edtName.getText().toString());
        playerToEdit.setUsername(edtUsername.getText().toString());
        playerToEdit.setAdmin(ckbIsAdmin.isChecked());

        try {
            isSuccess = db.update(playerToEdit);

            if (isSuccess) {
                msg = getString(R.string.msg_UpdatedUserData);

                if (ckbUpdatePassword.isChecked()) {
                    db.setPassword(playerToEdit, edtPassword.getText().toString());
                }
            }
            else {
                msg = getString(R.string.msg_CouldNotUpdateUserData);
            }
        }
        catch (DuplicateUsernameException ex) {
            msg = String.format(getString(R.string.msg_UsernameNotAvailable), playerToEdit.getUsername());
        }

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
