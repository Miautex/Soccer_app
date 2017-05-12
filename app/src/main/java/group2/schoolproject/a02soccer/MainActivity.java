package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import pkgDatabase.Database;
import pkgData.Player;
import pkgException.CouldNotDeletePlayerException;
import pkgDatabase.pkgListener.OnGamesUpdatedListener;
import pkgDatabase.pkgListener.OnPlayersUpdatedListener;
import pkgMenu.DynamicMenuActivity;

public class MainActivity extends DynamicMenuActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPlayersUpdatedListener,
        OnGamesUpdatedListener {
    ListView lsvEditableGames = null;
    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllViews();
        registrateEventHandlers();

        try {
            db = Database.getInstance();
            db.addOnPlayersUpdatedListener(this);
            db.addOnGamesUpdatedListener(this);
            displayPlayers();
        }
        catch (Exception ex) {
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mniAddPlayer) {
            openActivity(AddPlayerActivity.class);
        } else if (id == R.id.mniAddGame) {
            openActivity(AddGameSelectPlayersActivity.class);
        } else if (id == R.id.mniEditPlayer) {
            openActivity(EditPlayerOwnActivity.class);

        } else if (id == R.id.nav_manage) {
            openActivity(TeamManagmentActivity.class);

        } else if (id == R.id.mniLogin) {
            openActivity(LoginActivity.class);

        } else if (id == R.id.nav_send) {
            openActivity(LoginActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAllViews(){
        lsvEditableGames = (ListView) this.findViewById(R.id.lsvEditableGames);
    }

    private void registrateEventHandlers(){
        registerForContextMenu(lsvEditableGames);       //set up context-menu
    }

    public void displayPlayers() throws Exception {
        final ArrayAdapter<Player> lsvAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1);
        lsvAdapter.addAll(db.getAllPlayers());
        lsvEditableGames.setAdapter(lsvAdapter);

        /*//because otherwise crash if app is closed
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lsvEditableGames.setAdapter(lsvAdapter);
            }
        });*/
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lsvEditableGames) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Player selectedPlayer = (Player) lsvEditableGames.getAdapter().getItem(info.position);

        try {
            //Player cannot delete his own player
            if (db.getCurrentlyLoggedInPlayer().equals(selectedPlayer)) {
                throw new Exception(getString(R.string.msg_NoActionsOnOwnPlayer));
            }

            switch (item.getItemId()) {
                case R.id.edit:
                    onCtxMniEdit(selectedPlayer);
                    break;
                case R.id.delete:
                    onCtxMniDelete(selectedPlayer);
                    break;
            }
        }
        catch (Exception ex) {
            Toast.makeText(this, getString(R.string.Error) + ": " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void onCtxMniEdit(Player selectedPlayer) {
        Intent myIntent = new Intent(this, EditPlayerAdminActivity.class);
        myIntent.putExtra("player", selectedPlayer);

        this.startActivity(myIntent);
    }

    private void onCtxMniDelete(Player selectedPlayer) throws Exception {

        try {
            if (db.remove(selectedPlayer)) {
                ((ArrayAdapter<Player>) lsvEditableGames.getAdapter()).remove(selectedPlayer);
            }
        }
        catch (CouldNotDeletePlayerException ex) {
            throw new CouldNotDeletePlayerException(getString(R.string.msg_CouldNotDeletePlayer));
        }
    }

    @Override
    public void gamesChanged() {
    }

    @Override
    public void playersChanged() {
        try {
            displayPlayers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
