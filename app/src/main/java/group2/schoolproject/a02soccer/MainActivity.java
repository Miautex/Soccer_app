package group2.schoolproject.a02soccer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pkgData.Database;
import pkgData.Game;

import static group2.schoolproject.a02soccer.Utilities.showToast;

public class MainActivity extends Activity {
    ListView lsvEditableGames = null;
    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllViews();

        try {
            db = Database.getInstance();
            showGames();
        }
        catch (Exception ex) {
            showToast("Error: " + ex.getMessage(), Toast.LENGTH_SHORT, getApplicationContext());
        }
    }



    private void getAllViews(){
            lsvEditableGames = (ListView) this.findViewById(R.id.lsvEditableGames);
    }

    private void showGames() {
        ArrayAdapter<Game> lsvAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_1);
        lsvAdapter.addAll(db.getGames());
        lsvEditableGames.setAdapter(lsvAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mniAddUser:
                showToast("Add User clicked", Toast.LENGTH_SHORT, getApplicationContext());
                break;
            case R.id.mniEditUser:
                openEditPlayerActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openEditPlayerActivity(){
        Intent myIntent = new Intent(MainActivity.this, EditPlayerActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}
