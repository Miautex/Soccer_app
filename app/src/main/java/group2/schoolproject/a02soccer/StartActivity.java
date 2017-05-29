package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;

import pkgDatabase.Database;

public class StartActivity extends BaseActivity {
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_start);
            setTitle(R.string.app_name);

            db = Database.getInstance();

            if (db.loadUserLocally() == null) {
                openLogin();
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void openLogin() {
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}