package group2.schoolproject.a02soccer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    ListView lsvEditableGames = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getAllViews();
        showToast("created");

    }

    private void getAllViews(){
        lsvEditableGames = (ListView) findViewById(R.id.lsvEditableGames);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniAddUser:
                showToast("Add User clicked");
                break;
            case R.id.mniEditUser:
                showToast("Edit User clicked");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showToast(String text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
