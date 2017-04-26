package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pkgData.Database;
import pkgData.Game;

public class MainActivity extends AppCompatActivity {
    ListView lsvEditableGames = null;
    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllViews();

        try {
            db = Database.getInstance();
            displayGames();
        }
        catch (Exception ex) {
            Toast.makeText(this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllViews(){
            lsvEditableGames = (ListView) this.findViewById(R.id.lsvEditableGames);
    }

    public void displayGames() throws Exception {
        ArrayAdapter<Game> lsvAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_1);
        lsvAdapter.addAll(db.getAllGames());
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
                openAddPlayerActivity();
                break;
            case R.id.mniEditUser:
                openEditPlayerActivity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void openAddPlayerActivity(){
        Intent myIntent = new Intent(MainActivity.this, AddPlayerActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private void openEditPlayerActivity(){
        Intent myIntent = new Intent(MainActivity.this, EditPlayerActivity.class);
        MainActivity.this.startActivity(myIntent);
    }
}
