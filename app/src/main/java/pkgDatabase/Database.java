package pkgDatabase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.BuildConfig;
import pkgComparator.PlayerComparatorName;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.PlayerPositionRequest;
import pkgDatabase.pkgListener.OnGameInsertedListener;
import pkgDatabase.pkgListener.OnGameRemovedListener;
import pkgDatabase.pkgListener.OnGameUpdatedListener;
import pkgDatabase.pkgListener.OnGamesChangedListener;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgDatabase.pkgListener.OnLoadSinglePlayerListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgDatabase.pkgListener.OnParticipationInsertedListener;
import pkgDatabase.pkgListener.OnParticipationUpdatedListener;
import pkgDatabase.pkgListener.OnPlayerInsertedListener;
import pkgDatabase.pkgListener.OnPlayerRemovedListener;
import pkgDatabase.pkgListener.OnPlayerUpdatedListener;
import pkgDatabase.pkgListener.OnPlayersChangedListener;
import pkgDatabase.pkgListener.OnQRCodeGeneratedListener;
import pkgDatabase.pkgListener.OnSetPasswordListener;
import pkgDatabase.pkgListener.OnSetPlayerPosListener;
import pkgMisc.GsonSerializor;
import pkgWSA.Accessor;
import pkgWSA.HttpMethod;

public class Database extends Application implements OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener, OnLoadParticipationsListener, OnGameInsertedListener, OnPlayerInsertedListener, OnGameUpdatedListener, OnPlayerUpdatedListener, OnPlayerRemovedListener, OnGameRemovedListener, OnLoadSinglePlayerListener, OnQRCodeGeneratedListener {
    public static final int MIN_LENGTH_PASSWORD = 5;
    private static Database instance = null;
    private TreeSet<Game> allGames;
    private TreeSet<Player> allPlayers;
    private Context ctx;
    private Player currentlyLoggedInPlayer;
    private ArrayList<OnGamesChangedListener> gamesChangedListener;
    private boolean initialLogin;
    private boolean isQRCodeReady;
    private Locale locale;
    private ArrayList<OnPlayersChangedListener> playersChangedListener;
    private SharedPreferences preferences;
    private Bitmap qrCode;

    private Database() {
        this.ctx = null;
        this.currentlyLoggedInPlayer = null;
        this.preferences = null;
        this.initialLogin = true;
        this.qrCode = null;
        this.isQRCodeReady = false;
        this.allPlayers = new TreeSet<>();
        this.allGames = new TreeSet<>();
        this.playersChangedListener = new ArrayList<>();
        this.gamesChangedListener = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }


    /**********************************************************************************
     *                          LISTENER RELATED METHODS                              *
     *  Listeners which are called when the allPlayers/allGames changes               *
     *              (notify...) has to be called manually                             *
     **********************************************************************************/
    public void addOnPlayersUpdatedListener(OnPlayersChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (this.playersChangedListener.contains(listener)) {
            throw new IllegalArgumentException("listener already registered");
        } else {
            this.playersChangedListener.add(listener);
        }
    }

    public void addOnGamesUpdatedListener(OnGamesChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (this.gamesChangedListener.contains(listener)) {
            throw new IllegalArgumentException("listener already registered");
        } else {
            this.gamesChangedListener.add(listener);
        }
    }

    private void notifyOnPlayersChangedListener() {
        Iterator it = this.playersChangedListener.iterator();
        while (it.hasNext()) {
            OnPlayersChangedListener listener = (OnPlayersChangedListener) it.next();
            if (listener != null) {
                listener.playersChanged();
            }
        }
    }

    private void notifyOnGamesUpdatedListener() {
        Iterator it = this.gamesChangedListener.iterator();
        while (it.hasNext()) {
            OnGamesChangedListener listener = (OnGamesChangedListener) it.next();
            if (listener != null) {
                listener.gamesChanged();
            }
        }
    }


    /**********************************************************************************
     *                          PLAYER RELATED METHODS                                *
     **********************************************************************************/

    public Player getCurrentlyLoggedInPlayer() {
        return this.currentlyLoggedInPlayer;
    }

    /**
     * Sets the currently logged in player
     * and triggers the generation of the player specific QR-Code
     * (Generation Status can be checked via the isQRCodeReady() method and if ready be obtained using getQRCode())
     */
    private void setCurrentlyLoggedInPlayer(Player p) {
        //if currentlyLoggedInPlayer is changed to a different player and this player has an ID
        if (!(p == null || p.getId() == null || this.currentlyLoggedInPlayer.equals(p))) {
            generateQRBitmap();     //Start generation of QR-Code
        }
        this.currentlyLoggedInPlayer = p;
    }

    private void generateQRBitmap() {
        String content = String.valueOf(getCurrentlyLoggedInPlayer().getId());
        setQRCodeReady(false);
        //Generate QR-Code is separate AsyncTask
        new GenerateQRCodeTask().execute(content, this);
    }

    /**
     * Is called after AsyncTask has finished generating the QR-Code
     */
    @Override
    public void generateQRCodeFinished(Bitmap qrCode) {
        this.qrCode = qrCode;
        setQRCodeReady(true);
    }

    public Bitmap getQRCode() {
        return this.qrCode;
    }

    public boolean isQRCodeReady() {
        return this.isQRCodeReady;
    }

    private void setQRCodeReady(boolean isReady) {
        this.isQRCodeReady = isReady;
    }


    /**
     * Starts the loading (refreshing) of all players from the Webservice
     */
    public void loadAllPlayers(OnLoadAllPlayersListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoadAllPlayersListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch loading
        Accessor.runRequestAsync(HttpMethod.GET, "player", null, null, new LoadAllPlayersHandler(listeners));
    }

    /**
     * Is called after loading all players finishes (successfully or not)
     */
    @Override
    public void loadPlayersFinished(LoadAllPlayersHandler handler) {
        //If loading was successful
        if (handler.getException() == null) {
            //Refresh allPlayers using loaded players from LoadAllPlayersHandler
            this.allPlayers.clear();
            for (Player p : handler.getPlayers()) {
                this.allPlayers.add(p);
            }
            setCurrentlyLoggedInPlayer(getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()));
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Starts the loading (refreshing) of a single player from the Webservice
     */
    public void loadSinglePlayer(String username, OnLoadSinglePlayerListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoadSinglePlayerListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch loading
        Accessor.runRequestAsync(HttpMethod.GET, "player/" + username, null, null, new LoadSinglePlayerHandler(listeners));
    }

    /**
     * Is called after loading single player finishes (successfully or not)
     */
    @Override
    public void loadSinglePlayerFinished(LoadSinglePlayerHandler handler) {
        //If loading was successful
        if (handler.getException() == null) {
            //Update player loaded player in allPlayers
            this.allPlayers.remove(handler.getPlayer());
            this.allPlayers.add(handler.getPlayer());
            setCurrentlyLoggedInPlayer(getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()));
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Returns an ArrayList of all loaded Players ordered by name
     */
    public ArrayList<Player> getAllPlayers() {
        TreeSet<Player> sortingTs = new TreeSet<>(new PlayerComparatorName());
        sortingTs.addAll(this.allPlayers);
        return new ArrayList<>(sortingTs);
    }

    /**
     * Returns a Player by username
     * NOTE: Only the local collection is searched, if you want the most recent player from the Webservice,
     * please call loadSinglePlayer() first
     */
    public Player getPlayerByUsername(String username) {
        Player player = null;
        Iterator<Player> iteratorPlayers = this.allPlayers.iterator();
        while (iteratorPlayers.hasNext() && player == null) {
            Player tmpPl = iteratorPlayers.next();
            if (tmpPl.getUsername().equals(username)) {
                player = tmpPl;
            }
        }
        return player;
    }


    /**
     * Starts the insertion of passed player
     */
    public void insert(Player p, OnPlayerInsertedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnPlayerInsertedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch insertion
        Accessor.runRequestAsync(HttpMethod.POST, "player", null, GsonSerializor.serializePlayer(p), new InsertPlayerHandler(listeners, p));
    }

    /**
     * Is called after insertion of new player finishes (successfully or not)
     */
    @Override
    public void insertPlayerFinished(InsertPlayerHandler handler) {
        //If insertion was successful
        if (handler.getException() == null) {
            //Add new player to allPlayers (Player has already received an ID from the webservice)
            this.allPlayers.add(handler.getPlayer());
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Starts the update of passed player
     */
    public void update(Player p, OnPlayerUpdatedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnPlayerUpdatedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch update
        Accessor.runRequestAsync(HttpMethod.PUT, "player", null, GsonSerializor.serializePlayer(p), new UpdatePlayerHandler(listeners, p));
    }

    /**
     * Is called after update of player finishes (successfully or not)
     */
    @Override
    public void updatePlayerFinished(UpdatePlayerHandler handler) {
        //If update was successful
        if (handler.getException() == null) {
            //Update player in allPlayers
            this.allPlayers.remove(handler.getPlayer());
            this.allPlayers.add(handler.getPlayer());
            //If currently logged in player was updated, set currentlyLoggedInPlayer to updated player
            if (getCurrentlyLoggedInPlayer().equals(handler.getPlayer())) {
                setCurrentlyLoggedInPlayer(handler.getPlayer());
            }
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Starts the removal of passed player
     */
    public void remove(Player p, OnPlayerRemovedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnPlayerRemovedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch removal
        Accessor.runRequestAsync(HttpMethod.DELETE, "player/" + p.getId(), null, null, new RemovePlayerHandler(listeners, p));
    }

    /**
     * Is called after removal of player finishes (successfully or not)
     */
    @Override
    public void removePlayerFinished(RemovePlayerHandler handler) {
        //If removal was successful
        if (handler.getException() == null) {
            //Remove player from allPlayers
            this.allPlayers.remove(handler.getPlayer());
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Starts the update of PlayerPositions for passed player
     * (Positions are taken directly from passed player)
     * NOTE: This method is automatically called by UpdatePlayerHandler during update of player
     */
    protected void setPlayerPositions(Player p, OnSetPlayerPosListener listener) throws Exception {
        //Add passed listener (UpdatePlayerHandler) to list of listeners
        ArrayList<OnSetPlayerPosListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Create the PlayerPositionRequest
        PlayerPositionRequest ppr = new PlayerPositionRequest();
        ppr.setATTACK(p.getPositions().contains(PlayerPosition.ATTACK));
        ppr.setDEFENSE(p.getPositions().contains(PlayerPosition.DEFENSE));
        ppr.setGOAL(p.getPositions().contains(PlayerPosition.GOAL));
        ppr.setMIDFIELD(p.getPositions().contains(PlayerPosition.MIDFIELD));

        //Launch update
        Accessor.runRequestAsync(HttpMethod.PUT, "player/positions/" + p.getId(), null,
                GsonSerializor.serializePlayerPositionRequest(ppr), new SetPlayerPositionsHandler(listeners));
    }



    /**
     * Starts the login
     */
    public void login(String username, String local_pw_Unencrypted, OnLoginListener listener) throws Exception {
        //Encrypt the passed password
        String local_pwEnc = encryptPassword(local_pw_Unencrypted);
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoginListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch login
        Accessor.runRequestAsync(HttpMethod.GET, "player/security/" + username, null, null, new LoginHandler(username, local_pwEnc, listeners));
    }

    /**
     * Is called after login finishes (successfully or not)
     */
    @Override
    public void loginFinished(LoginHandler handler) {
        //If login was successful
        if (handler.getException() == null) {
            try {
                //Set currentlyLoggedInPlayer to save the username of the logged in player
                // so the setting of currentlyLoggedInPlayer in loadPlayersFinished works
                // (This method is called before loadAllPlayers is finished, so otherwise
                // getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()) wouldn't work)
                setCurrentlyLoggedInPlayer(new Player(handler.getUsername(), "tmpname", false));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Encrypts passed string using MD5 algorithm
     */
    private String encryptPassword(String pwInput) {
        String hash = null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pwInput.getBytes(), 0, pwInput.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    /**
     * Starts the update of a players password
     * NOTE: THis method is automatically called by InsertPlayerHandler and if updatePassword is checked by UpdatePlayerHandler
     */
    public void setPassword(Player p, String pw, OnSetPasswordListener listener) throws Exception {
        //Add passed listener to list of listeners
        ArrayList<OnSetPasswordListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Launch password update
        Accessor.runRequestAsync(HttpMethod.PUT, "player/security/" + p.getId(), null,
                GsonSerializor.serializePassword(encryptPassword(pw)), new SetPasswordHandler(listeners, p));
    }



    /**********************************************************************************
     *                              GAME RELATED METHODS                              *
     **********************************************************************************/

    /**
     * Starts the loading of all games from Webservice
     */
    public void loadAllGames(OnLoadAllGamesListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoadAllGamesListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch loading
        Accessor.runRequestAsync(HttpMethod.GET, "game", null, null, new LoadAllGamesHandler(listeners));
    }

    /**
     * Is called after loading all games finishes (successfully or not)
     */
    @Override
    public void loadGamesFinished(LoadAllGamesHandler handler) {
        //If loading was successful
        if (handler.getException() == null) {
            //Refresh allGames using loaded games from LoadAllGamesHandler
            this.allGames.clear();
            for (Game g : handler.getGames()) {
                this.allGames.add(g);
            }
            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Returns an ArrayList of all loaded Games
     */
    public ArrayList<Game> getAllGames() {
        return new ArrayList<>(this.allGames);
    }

    /**
     * Returns a Game by id
     * NOTE: Only the local collection is searched
     */
    public Game getGameByID(int id) {
        Game game = null;
        Iterator<Game> iteratorGames = this.allGames.iterator();
        while (iteratorGames.hasNext() && game == null) {
            Game tmpG = (Game) iteratorGames.next();
            if (tmpG.getId() == id) {
                game = tmpG;
            }
        }
        return game;
    }

    /**
     * Starts the insertion of passed Game
     */
    public void insert(Game g, OnGameInsertedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnGameInsertedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch insertion
        Accessor.runRequestAsync(HttpMethod.POST, "game", null, GsonSerializor.serializeGame(g), new InsertGameHandler(listeners, g));
    }

    /**
     * Is called after insertion of game finishes (successfully or not)
     */
    @Override
    public void insertGameFinished(InsertGameHandler handler) {
        //If insertion was successful
        if (handler.getException() == null) {
            //Add game to allGames
            this.allGames.add(handler.getGame());
            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Starts the insertion of passed participation
     * NOTE: This method is automatically called by InsertGameHandler during the insertion of a new Game
     */
    protected void insert(Participation p, OnParticipationInsertedListener listener) throws Exception {
        //Add passed listener to list of listeners
        ArrayList<OnParticipationInsertedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Launch insertion
        Accessor.runRequestAsync(HttpMethod.POST, "participation", "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId(), GsonSerializor.serializeParticipation(p), new InsertParticipationHandler(listeners));
    }

    /**
     * Starts the update of passed Game
     */
    public void update(Game g, OnGameUpdatedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnGameUpdatedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch update
        Accessor.runRequestAsync(HttpMethod.PUT, "game", null, GsonSerializor.serializeGame(g), new UpdateGameHandler(listeners, g));
    }

    /**
     * Is called after update of games finishes (successfully or not)
     */
    @Override
    public void updateGameFinished(UpdateGameHandler handler) {
        //If update was successful
        if (handler.getException() == null) {
            //Update game in allGames
            this.allGames.remove(handler.getGame());
            this.allGames.add(handler.getGame());
            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Starts the update of passed participation
     * NOTE: This method is automatically called by UpdateGameHandler during the update of a Game
     */
    public void update(Participation p, OnParticipationUpdatedListener listener) throws Exception {
        //Add passed listener to list of listeners
        ArrayList<OnParticipationUpdatedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Launch update
        Accessor.runRequestAsync(HttpMethod.PUT, "participation", "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId(), GsonSerializor.serializeParticipation(p), new UpdateParticipationHandler(listeners));
    }

    /**
     * Starts the removal of passed Game
     */
    public void remove(Game g, OnGameRemovedListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnGameRemovedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch removal
        Accessor.runRequestAsync(HttpMethod.DELETE, "game/" + g.getId(), null, null, new RemoveGameHandler(listeners, g));
    }

    /**
     * Is called after removal of games finishes (successfully or not)
     */
    @Override
    public void removeGameFinished(RemoveGameHandler handler) {
        //If removal was successful
        if (handler.getException() == null) {
            //Remove game from allGames
            this.allGames.remove(handler.getGame());
            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Starts the loading of all Participations of passed Game
     */
    public void getParticipationsOfGame(Game g, OnLoadParticipationsListener listener) throws Exception {
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoadParticipationsListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        //Launch loading
        Accessor.runRequestAsync(HttpMethod.GET, "participation/byGame/" + g.getId(), null, null, new LoadParticipationsHandler(listeners, g.getId()));
    }

    /**
     * Is called after loading of Participations of Game finishes (successfully or not)
     */
    @Override
    public void loadParticipationsFinished(LoadParticipationsHandler handler) {
        //If loading was successful
        if (handler.getException() == null) {
            //Get Game which the Participations belong to
            Game game = getGameByID(handler.getGameID());
            //Clear games participations
            game.removeAllParticipations();
            //Add all loaded participations to games collection
            for (Participation p : handler.getPatrticipations()) {
                game.addParticipation(p);
            }
        }
    }


    /**********************************************************************************
     *                                  MISC METHODS                                  *
     **********************************************************************************/

    //Locale: used for translations
    public void setLocale(Locale loc) {
        this.locale = loc;
    }

    public Locale getLocale() {
        return this.locale;
    }

    //Context: used to access PreferenceManager (Settings)
    public void setContext(Context ctx) throws Exception {
        if (ctx == null) {
            throw new Exception("Context may not be null");
        }
        this.ctx = ctx;
    }

    public Context getContext() {
        return this.ctx;
    }

    public void initPreferences(Context ctx) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public String getStoredUsername() {
        return this.preferences.getString("preference_user_username", BuildConfig.FLAVOR);
    }

    public String getStoredPassword() {
        return this.preferences.getString("preference_user_password", BuildConfig.FLAVOR);
    }

    public boolean isAutologin() {
        return this.preferences.getBoolean("preference_user_autologin", false);
    }

    public void setInitialLogin(boolean initialLogin) {
        this.initialLogin = initialLogin;
    }

    public boolean isInitialLogin() {
        return this.initialLogin;
    }

    public void logout() {
        setCurrentlyLoggedInPlayer(null);
    }

    public boolean isToast() {
        return !this.preferences.getBoolean("preference_usesnackbar", true);
    }
}