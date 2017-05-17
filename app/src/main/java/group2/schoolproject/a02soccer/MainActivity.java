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
import android.widget.Spinner;

import pkgData.Game;
import pkgData.Player;
import pkgDatabase.Database;
import pkgDatabase.pkgListener.OnGamesUpdatedListener;
import pkgDatabase.pkgListener.OnPlayersUpdatedListener;
import pkgException.CouldNotDeletePlayerException;
import pkgWSA.Accessor;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPlayersUpdatedListener,
        OnGamesUpdatedListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    ListView lsvPlayersGames = null;
    Spinner spPlayersGames = null;

    Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllViews();

        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            Accessor.init(getApplicationContext());
            db = Database.getInstance();
            db.initPreferences(this);

            if (db.getCurrentlyLoggedInPlayer() == null || !db.getCurrentlyLoggedInPlayer().isAdmin()) {
                navigationView.getMenu().setGroupVisible(R.id.menuGroupAdmin, false);
            }
            else {
                navigationView.getMenu().setGroupVisible(R.id.menuGroupAdmin, true);
            }

            registrateEventHandlers();
            displayPlayers();
            displayLoggedInUser();
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
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
            Intent myIntent = new Intent(this, EditPlayerActivity.class);
            myIntent.putExtra("player", db.getCurrentlyLoggedInPlayer());
            startActivity(myIntent);
        } else if (id == R.id.nav_manage) {
            openActivity(ScoreBoardActivity.class);
        } else if (id == R.id.mniLogin) {
            openActivity(LoginActivity.class);
        } else if (id == R.id.nav_settings) {
            openActivity(SettingsActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAllViews(){
        lsvPlayersGames = (ListView) this.findViewById(R.id.lsvPlayersGames);
        spPlayersGames = (Spinner) this.findViewById(R.id.spinnerPlayerGame);
    }

    private void registrateEventHandlers(){
        registerForContextMenu(lsvPlayersGames);       //set up context-menu
        lsvPlayersGames.setOnItemClickListener(this);
        spPlayersGames.setOnItemSelectedListener(this);
        db.addOnPlayersUpdatedListener(this);
        db.addOnGamesUpdatedListener(this);

    }

    private void displayLoggedInUser() {
        //TODO
        /*if (db.getCurrentlyLoggedInPlayer() != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            TextView txvName = (TextView) header.findViewById(R.id.txvName);
            TextView txvUsername = (TextView) header.findViewById(R.id.txvUsername);

            txvName.setText(db.getCurrentlyLoggedInPlayer().getName());
            txvUsername.setText(db.getCurrentlyLoggedInPlayer().getUsername());
        }*/
    }

    private void displayPlayers() throws Exception {
        final ArrayAdapter<Player> lsvAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1);
        lsvAdapter.addAll(db.getAllPlayers());
        lsvPlayersGames.setAdapter(lsvAdapter);
    }

    private void displayGames() throws Exception {
        final ArrayAdapter<Game> lsvAdapter = new ArrayAdapter<Game>(this, android.R.layout.simple_list_item_1);
        lsvAdapter.addAll(db.getAllGames());
        lsvPlayersGames.setAdapter(lsvAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.lsvPlayersGames && db.getCurrentlyLoggedInPlayer().isAdmin()) {     //if user is admin
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        try {
            if (db.getCurrentlyLoggedInPlayer().isAdmin()) {        //if user is admin
                if (spPlayersGames.getSelectedItemPosition() == 0) {        //if players are displayed
                    Player selectedPlayer = (Player) lsvPlayersGames.getAdapter().getItem(info.position);

                    switch (item.getItemId()) {
                        case R.id.edit:
                            onCtxMniEdit(selectedPlayer);
                            break;
                        case R.id.delete:
                            onCtxMniDelete(selectedPlayer);
                            break;
                    }
                }
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }

    private void onCtxMniEdit(Player selectedPlayer) {
        Intent myIntent;
        myIntent = new Intent(this, EditPlayerActivity.class);
        myIntent.putExtra("player", selectedPlayer);
        this.startActivity(myIntent);
    }

    private void onCtxMniDelete(Player selectedPlayer) throws Exception {

        try {
            if (db.getCurrentlyLoggedInPlayer().equals(selectedPlayer)) {
                //Player cannot delete his own player
                throw new Exception(getString(R.string.msg_CannotDeleteOwnPlayer));
            }
            else {
                if (db.remove(selectedPlayer)) {
                    ((ArrayAdapter<Player>) lsvPlayersGames.getAdapter()).remove(selectedPlayer);
                }
            }
        }
        catch (CouldNotDeletePlayerException ex) {
            throw new CouldNotDeletePlayerException(getString(R.string.msg_CouldNotDeletePlayer));
        }
    }

    @Override
    public void gamesChanged() {
        try {
            if (spPlayersGames.getSelectedItemPosition() == 1) {
                displayGames();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void playersChanged() {
        try {
            if (spPlayersGames.getSelectedItemPosition() == 0) {
                displayPlayers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (spPlayersGames.getSelectedItemPosition() == 0) {        //only if players are displayed
            Player selectedPlayer = (Player) lsvPlayersGames.getItemAtPosition(position);
            Intent myIntent = new Intent(this, ShowPlayerStatsActivity.class);

            myIntent.putExtra("player", selectedPlayer);
            this.startActivity(myIntent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            //Players
            if (position == 0) {
                displayPlayers();
            }
            //Games
            else {
                displayGames();
            }
        }
        catch (Exception ex) {
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
