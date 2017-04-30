package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Locale;

import pkgData.Database;
import pkgData.Game;

public class MainActivity extends AppCompatActivity {
    ListView lsvEditableGames = null;
    Database db = null;

    @Override
    @Deprecated
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllViews();

        try {
            db = Database.getInstance();
            Locale locale = new Locale("en", "US");
            db.setLocale(locale);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;
            res.updateConfiguration(conf, dm);
            //displayGames();
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
        return DynamicMenu.onPrepareOptionsMenu(menu, this.getClass());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retval;
        Class toOpen;
        toOpen = DynamicMenu.onOptionsItemSelected(item);
        if(toOpen != null){
            openActivity(toOpen);
        }
        retval = super.onOptionsItemSelected(item);
        return retval;
    }

    private void openActivity(Class toOpen){
        Intent myIntent = new Intent(this, toOpen);
        this.startActivity(myIntent);
    }

}
