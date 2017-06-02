package group2.schoolproject.a02soccer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import pkgAdapter.MainGameListAdapter;
import pkgAdapter.MainPlayerListAdapter;
import pkgData.Game;
import pkgData.LocalUserData;
import pkgData.Player;
import pkgDatabase.Database;
import pkgDatabase.LoadAllGamesHandler;
import pkgDatabase.LoadAllPlayersHandler;
import pkgDatabase.RemoveGameHandler;
import pkgDatabase.RemovePlayerHandler;
import pkgDatabase.pkgListener.OnGameRemovedListener;
import pkgDatabase.pkgListener.OnGamesChangedListener;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnOnlineStatusChangedListener;
import pkgDatabase.pkgListener.OnPlayerRemovedListener;
import pkgDatabase.pkgListener.OnPlayersChangedListener;
import pkgException.CouldNotDeletePlayerException;
import pkgListeners.OnDeleteDialogButtonPressedListener;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnPlayersChangedListener,
        OnGamesChangedListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener, OnLoadAllPlayersListener, OnLoadAllGamesListener, View.OnClickListener,
        OnPlayerRemovedListener, OnGameRemovedListener, OnDeleteDialogButtonPressedListener,
        OnOnlineStatusChangedListener {

    private ListView lsvPlayersGames = null;
    private Spinner spPlayersGames = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private NavigationView navView = null;

    private ImageButton imgQRCode = null;
    private boolean arePlayersRefreshed, areGamesRefreshed, hasRefreshFailed;
    private Database db = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = Database.getInstance();
        setTitle();
        getAllViews();

        try {
            db.setContext(this);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            setupMenu(db.getCurrentlyLoggedInPlayer().isAdmin());
            registrateEventHandlers();
            displayPlayers();
            displayLoggedInUser();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            showMessage(getString(R.string.Error) + ": " + ex.getMessage());
        }
    }

    private void setupMenu(boolean isAdmin) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (!isAdmin) {
            MenuItem myMoveGroupItem = navigationView.getMenu().getItem(0);
            SubMenu subMenu = myMoveGroupItem.getSubMenu();
            subMenu.findItem(R.id.mniAddPlayer).setVisible(false);

            myMoveGroupItem = navigationView.getMenu().getItem(1);
            subMenu = myMoveGroupItem.getSubMenu();
            subMenu.findItem(R.id.mniAddGame).setVisible(false);
        }
    }

    public void setTitle() {
        if (db.isOnline()) {
            setTitle(R.string.title_activity_main);
        }
        else {
            setTitle(R.string.title_activity_main_offline);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (v.getId() == R.id.imgQRCode) {
                openQRCode();
            }
        } catch (Exception e) {
            showMessage(getString(R.string.Error) + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openQRCode(){
        Bitmap qr;
        try{
            if (db.isQRCodeReady()) {
                qr = db.getQRCode();
                ImageView image = new ImageView(this);
                image.setImageBitmap(qr);

                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ShowQRCode);
                builder.setView(image);

                builder.create().show();
            }
            else {
                showMessage("QR not ready yet");
            }

        } catch(Exception we){
            showMessage(getString(R.string.Error) + ": " + we.getMessage());
        }
    }


    private byte backPressedCount = 0;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressedCount++;
            if (backPressedCount >= 2) {
                super.onBackPressed();
            }
            else {
                showMessage(getString(R.string.msg_DoubleBackPress));
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedCount = 0;
                }
            }, 2000);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        try {
            switch (id) {
                case R.id.mniDisplayPlayer:
                    displayPlayers();
                    break;
                case R.id.mniEditPlayer:
                    Intent myIntent = new Intent(this, EditPlayerActivity.class);
                    myIntent.putExtra("player", db.getCurrentlyLoggedInPlayer());
                    startActivity(myIntent);
                    break;
                case R.id.mniScoreboard:
                    openActivity(ScoreboardActivity.class);
                    break;
                case R.id.mniAddPlayer:
                    openActivity(AddPlayerActivity.class);
                    break;
                case R.id.mniDisplayGames:
                    displayGames();
                    break;
                case R.id.mniAddGame:
                    openActivity(AddGameSelectPlayersActivity.class);
                    break;
                case R.id.mniSettings:
                    openActivity(SettingsActivity.class);
                    break;
                case R.id.mniLogout:
                    db.logout(this);
                    openLoginActivity();
                    break;
                case R.id.test:
                    openActivity(TeamDivision2.class);
                    break;
                case R.id.qr:
                    //openActivity(TeamDivision2.class);
                    break;
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    private void getAllViews(){
        lsvPlayersGames = (ListView) this.findViewById(R.id.lsvPlayersGames);
        spPlayersGames = (Spinner) this.findViewById(R.id.spinnerPlayerGame);
        swipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swiperefresh);
        navView = (NavigationView) this.findViewById(R.id.nav_view);
        View headerLayout = navView.getHeaderView(0);
        imgQRCode = (ImageButton) headerLayout.findViewById(R.id.imgQRCode);
    }

    private void registrateEventHandlers(){
        registerForContextMenu(lsvPlayersGames);       //set up context-menu
        lsvPlayersGames.setOnItemClickListener(this);
        spPlayersGames.setOnItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        db.addOnPlayersUpdatedListener(this);
        db.addOnGamesUpdatedListener(this);
        db.addOnOnlineStatusChangedListener(this);
        imgQRCode.setOnClickListener(this);
    }

    private void openLoginActivity() {
        LocalUserData localUserData = db.loadLocalUserData(this);

        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("username", localUserData.getPlayer().getUsername());
        intent.putExtra("password", localUserData.getPassword());
        intent.putExtra("doAutoLogin", false);
        startActivity(intent);
    }

    private void displayLoggedInUser() {
        if (db.getCurrentlyLoggedInPlayer() != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            TextView txvName = (TextView) header.findViewById(R.id.txvName);
            TextView txvUsername = (TextView) header.findViewById(R.id.txvUsername);

            txvName.setText(db.getCurrentlyLoggedInPlayer().getName());
            txvUsername.setText("@" + db.getCurrentlyLoggedInPlayer().getUsername());
        }
    }

    private void displayPlayers() throws Exception {
        final MainPlayerListAdapter lsvAdapter = new MainPlayerListAdapter(this, db.getAllPlayers(), this,
                db.getCurrentlyLoggedInPlayer().isAdmin());
        lsvPlayersGames.setAdapter(lsvAdapter);
    }

    private void displayGames() throws Exception {
        final MainGameListAdapter lsvAdapter = new MainGameListAdapter(this, db.getAllGames(), this,
                db.getCurrentlyLoggedInPlayer().isAdmin());
        lsvPlayersGames.setAdapter(lsvAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (db.getCurrentlyLoggedInPlayer().isAdmin()) {     //if user is admin
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
                            onEditPlayer(selectedPlayer);
                            break;
                        case R.id.delete:
                            onDeletePlayer(selectedPlayer);
                            break;
                    }
                }
                else {        //if games are displayed
                    Game selectedGame = (Game) lsvPlayersGames.getAdapter().getItem(info.position);

                    switch (item.getItemId()) {
                        case R.id.edit:
                            onEditGame(selectedGame);
                            break;
                        case R.id.delete:
                            onDeleteGame(selectedGame);
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

    public void onEditGame(Game selectedGame) throws Exception {
        Intent myIntent;
        myIntent = new Intent(this, EditGameActivity.class);
        myIntent.putExtra("game", selectedGame);
        this.startActivity(myIntent);
    }

    public void onDeleteGame(Game selectedGame) throws Exception {
        String msg = getString(R.string.msg_ConfirmGameDeletion);
        String title = String.format(getString(R.string.msg_ConfirmPlayerDeletion), msg);
        ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(selectedGame, this, title);
        cdd.show();
    }

    public void onEditPlayer(Player selectedPlayer) {
        Intent myIntent;
        myIntent = new Intent(this, EditPlayerActivity.class);
        myIntent.putExtra("player", selectedPlayer);
        this.startActivity(myIntent);
    }

    public void onDeletePlayer(Player selectedPlayer) throws Exception {

        try {
            if (db.getCurrentlyLoggedInPlayer().equals(selectedPlayer)) {
                //Player cannot delete his own player
                throw new Exception(getString(R.string.msg_CannotDeleteOwnPlayer));
            }
            else {
                String title = String.format(getString(R.string.msg_ConfirmPlayerDeletion), selectedPlayer.getName());
                ConfirmDeleteDialog cdd = new ConfirmDeleteDialog(selectedPlayer, this, title);
                cdd.show();
            }
        }
        catch (CouldNotDeletePlayerException ex) {
            throw new CouldNotDeletePlayerException(getString(R.string.msg_CouldNotDeletePlayer));
        }
    }

    @Override
    public void deleteDialogButtonPressed(Object selectedObject, boolean isPositive) {
        try {
            if (isPositive && selectedObject.getClass().equals(Player.class)) {
                db.remove((Player) selectedObject, this);
            }
            else if (isPositive && selectedObject.getClass().equals(Game.class)) {
                db.remove((Game) selectedObject, this);
            }
        }
        catch (Exception ex) {
            showMessage(ex.getMessage());
            ex.printStackTrace();
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
                displayLoggedInUser();
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
        else {
            try {
                Game selectedGame = (Game) lsvPlayersGames.getItemAtPosition(position);
                Intent myIntent = new Intent(this, ShowGameActivity.class);

                myIntent.putExtra("game", selectedGame);
                this.startActivity(myIntent);
            }
            catch (Exception ex) {
                showMessage(ex.getMessage());
                ex.printStackTrace();
            }
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

    @Override
    public void onRefresh() {
        try {
            arePlayersRefreshed = false;
            areGamesRefreshed = false;
            hasRefreshFailed = false;
            db.tryRefreshData(this, this, this);
        } catch (Exception e) {
            e.printStackTrace();
            swipeRefreshLayout.setRefreshing(false);
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotRefreshData));
        }
    }

    @Override
    public void loadGamesFinished(LoadAllGamesHandler handler) {
        if (handler.getException() == null) {
            areGamesRefreshed = true;
            if (arePlayersRefreshed) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            if (!hasRefreshFailed) {
                hasRefreshFailed = true;
                showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotRefreshData));
            }
        }
    }

    @Override
    public void loadPlayersFinished(LoadAllPlayersHandler handler) {
        if (handler.getException() == null) {
            arePlayersRefreshed = true;
            if (areGamesRefreshed) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            if (!hasRefreshFailed) {
                hasRefreshFailed = true;
                showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotRefreshData));
            }
        }
    }

    @Override
    public void removePlayerFinished(RemovePlayerHandler handler) {
        if (handler.getException() != null) {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotDeletePlayer));
        }
    }

    @Override
    public void removeGameFinished(RemoveGameHandler handler) {
        if (handler.getException() != null) {
            showMessage(getString(R.string.Error) + ": " + getString(R.string.msg_CouldNotDeleteGame));
        }
    }

    @Override
    public void onlineStatusChanged(boolean isOnline) {
        setTitle();
    }
}
