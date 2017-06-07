package pkgDatabase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import pkgComparator.PlayerComparatorName;
import pkgComparator.PlayerComparatorUsername;
import pkgData.Game;
import pkgDatabase.pkgHandler.InsertGameHandler;
import pkgDatabase.pkgHandler.InsertParticipationHandler;
import pkgDatabase.pkgHandler.InsertPlayerHandler;
import pkgDatabase.pkgHandler.LoadAllGamesHandler;
import pkgDatabase.pkgHandler.LoadAllPlayersHandler;
import pkgDatabase.pkgHandler.LoadParticipationsHandler;
import pkgDatabase.pkgHandler.LoadSinglePlayerHandler;
import pkgDatabase.pkgHandler.LoginHandler;
import pkgDatabase.pkgHandler.RemoveGameHandler;
import pkgDatabase.pkgHandler.RemovePlayerHandler;
import pkgDatabase.pkgHandler.SetPasswordHandler;
import pkgDatabase.pkgHandler.SetPlayerPositionsHandler;
import pkgDatabase.pkgHandler.UpdateGameHandler;
import pkgDatabase.pkgHandler.UpdateParticipationHandler;
import pkgDatabase.pkgHandler.UpdatePlayerHandler;
import pkgMisc.LocalData;
import pkgMisc.LocalUserData;
import pkgMisc.LoginCredentials;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgMisc.PlayerPositionRequest;
import pkgMisc.PlayerWithPassword;
import pkgDatabase.pkgListener.OnGameInsertedListener;
import pkgDatabase.pkgListener.OnGameRemovedListener;
import pkgDatabase.pkgListener.OnGameUpdatedListener;
import pkgDatabase.pkgListener.OnGamesChangedListener;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgDatabase.pkgListener.OnLoadSinglePlayerListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgDatabase.pkgListener.OnOnlineStatusChangedListener;
import pkgDatabase.pkgListener.OnParticipationInsertedListener;
import pkgDatabase.pkgListener.OnParticipationUpdatedListener;
import pkgDatabase.pkgListener.OnPlayerInsertedListener;
import pkgDatabase.pkgListener.OnPlayerRemovedListener;
import pkgDatabase.pkgListener.OnPlayerUpdatedListener;
import pkgDatabase.pkgListener.OnPlayersChangedListener;
import pkgDatabase.pkgListener.OnQRCodeGeneratedListener;
import pkgDatabase.pkgListener.OnSetPasswordListener;
import pkgDatabase.pkgListener.OnSetPlayerPosListener;
import pkgException.CannotDeletePlayerOfLocalGameException;
import pkgException.CouldNotUpdateGameException;
import pkgException.CouldNotUpdatePlayerException;
import pkgException.DuplicateUsernameException;
import pkgException.NoLocalDataException;
import pkgMisc.GsonSerializor;
import pkgWSA.Accessor;
import pkgWSA.HttpMethod;

/**
 * @author Elias Santner
 */

public class Database extends Application implements OnLoginListener, OnLoadAllPlayersListener, OnLoadAllGamesListener, OnLoadParticipationsListener, OnGameInsertedListener, OnPlayerInsertedListener, OnGameUpdatedListener, OnPlayerUpdatedListener, OnPlayerRemovedListener, OnGameRemovedListener, OnLoadSinglePlayerListener, OnQRCodeGeneratedListener {
    public static final int MIN_LENGTH_PASSWORD = 5;
    private final String USERDATA_FILE = "localUser.dat",
                         DATA_FILE = "localData.dat";

    private static Database instance = null;
    private TreeSet<Game> cachedGames;
    private TreeSet<Player> cachedPlayers;
    //Collections for saving local games/players
    private TreeSet<Game> localGames;
    private TreeSet<PlayerWithPassword> localPlayers;

    private Context ctx;
    private Player currentlyLoggedInPlayer;
    private ArrayList<OnGamesChangedListener> gamesChangedListener;
    private boolean isQRCodeReady;
    private Locale locale;
    private ArrayList<OnPlayersChangedListener> playersChangedListener;
    private ArrayList<OnOnlineStatusChangedListener> onlineStatusChangedListeners;
    private SharedPreferences preferences;
    private Bitmap qrCode;
    private String loginKey;
    private boolean isOnline;
    //For uploading data of offline mode to webservice (see uploadLocallySavedPlayers() and onUploadPlayerFinished())
    private int numPlayersToUpload, numGamesToUpload;
    private boolean isLocalDataUploadInProgress;

    private Database() {
        this.loginKey = null;
        this.ctx = null;
        this.currentlyLoggedInPlayer = null;
        this.preferences = null;
        this.qrCode = null;
        this.isQRCodeReady = false;
        this.cachedPlayers = new TreeSet<>();
        this.cachedGames = new TreeSet<>();
        this.localPlayers = new TreeSet<>();
        this.localGames = new TreeSet<>();
        this.playersChangedListener = new ArrayList<>();
        this.gamesChangedListener = new ArrayList<>();
        onlineStatusChangedListeners = new ArrayList<>();
        this.isOnline = true;
        this.isLocalDataUploadInProgress = false;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }


    /**********************************************************************************
     *                          LISTENER RELATED METHODS                              *
     *  Listeners which are called when the cachedPlayers/cachedGames changes               *
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
        for (OnPlayersChangedListener listener: playersChangedListener) {
            if (listener != null) {
                listener.playersChanged();
            }
        }

        saveLocalData(new LocalData(cachedPlayers, cachedGames, localPlayers, localGames), getContext());
    }

    private void notifyOnGamesUpdatedListener() {
        for (OnGamesChangedListener listener: gamesChangedListener) {
            if (listener != null) {
                listener.gamesChanged();
            }
        }

        saveLocalData(new LocalData(cachedPlayers, cachedGames, localPlayers, localGames), getContext());
    }

    public void addOnOnlineStatusChangedListener(OnOnlineStatusChangedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        } else if (this.onlineStatusChangedListeners.contains(listener)) {
            throw new IllegalArgumentException("listener already registered");
        } else {
            this.onlineStatusChangedListeners.add(listener);
        }
    }

    private void notifyOnOnlineStatusChangedListener() {
        for(OnOnlineStatusChangedListener listener: onlineStatusChangedListeners) {
            if (listener != null) {
                listener.onlineStatusChanged(isOnline());
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
        if (p != null && p.getId() != null && !p.equals(this.currentlyLoggedInPlayer)) {
            generateQRBitmap(p.getId());     //Start generation of QR-Code
        }
        this.currentlyLoggedInPlayer = p;
    }

    private void generateQRBitmap(int id) {
        setQRCodeReady(false);
        //Generate QR-Code is separate AsyncTask
        new GenerateQRCodeTask().execute(Integer.toString(id), this);
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
        Accessor.runRequestAsync(HttpMethod.GET, "player", "loginKey="+loginKey,
                null, new LoadAllPlayersHandler(listeners));
    }

    /**
     * Is called after loading all players finishes (successfully or not)
     */
    @Override
    public void loadPlayersFinished(LoadAllPlayersHandler handler) {
        //If loading was successful
        if (handler.getException() == null) {
            //Refresh cachedPlayers using loaded players from LoadAllPlayersHandler
            this.cachedPlayers.clear();
            for (Player p : handler.getPlayers()) {
                this.cachedPlayers.add(p);
            }
            setCurrentlyLoggedInPlayer(getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()));
            notifyOnPlayersChangedListener();

            //Store logged in player locally
            LocalUserData lud = loadLocalUserData(getContext());
            String pw = "";
            if (lud != null) {
                pw = lud.getPassword();
            }

            saveLocalUserData(new LocalUserData(currentlyLoggedInPlayer, pw, true),
                    getContext());
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
        Accessor.runRequestAsync(HttpMethod.GET, "player/" + username, "loginKey="+loginKey,
                null, new LoadSinglePlayerHandler(listeners));
    }

    /**
     * Is called after loading single player finishes (successfully or not)
     */
    @Override
    public void loadSinglePlayerFinished(LoadSinglePlayerHandler handler) {
        //If loading was successful
        if (handler.getException() == null && handler.getPlayer() != null) {
            //Update player loaded player in cachedPlayers
            this.cachedPlayers.remove(handler.getPlayer());
            this.cachedPlayers.add(handler.getPlayer());
            setCurrentlyLoggedInPlayer(getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()));
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Returns an ArrayList of all loaded Players ordered by name
     */
    public ArrayList<Player> getCachedPlayers() {
        TreeSet<Player> sortingTs = new TreeSet<>(new PlayerComparatorName());

        sortingTs.addAll(this.cachedPlayers);
        for (PlayerWithPassword pl: localPlayers) {
            sortingTs.add(pl.getPlayer());
        }

        //Put logged in player first, then the rest ordered by name
        ArrayList<Player> returnList = new ArrayList<>();
        returnList.add(getCurrentlyLoggedInPlayer());
        sortingTs.remove(getCurrentlyLoggedInPlayer());
        returnList.addAll(sortingTs);

        return returnList;
    }

    /**
     * Returns a Player by username
     * NOTE: Only the local collection is searched, if you want the most recent player from the Webservice,
     * please call loadSinglePlayer() first
     */
    public Player getPlayerByUsername(String username) {
        Player player = null;
        ArrayList<Player> localAndAllPlayers = new ArrayList<>();
        localAndAllPlayers.addAll(cachedPlayers);
        for (PlayerWithPassword pl: localPlayers) {
            localAndAllPlayers.add(pl.getPlayer());
        }

        Iterator<Player> iteratorPlayers = localAndAllPlayers.iterator();
        while (iteratorPlayers.hasNext() && player == null) {
            Player tmpPl = iteratorPlayers.next();
            if (tmpPl.getUsername().equals(username)) {
                player = tmpPl;
            }
        }
        return player;
    }

    /**
     * Returns a Player by id
     * NOTE: Only the local collection is searched, if you want the most recent player from the Webservice,
     * please call loadSinglePlayer() first
     */
    public Player getPlayerByID(int id) {
        Player player = null;
        Iterator<Player> iteratorPlayers = this.cachedPlayers.iterator();
        while (iteratorPlayers.hasNext() && player == null) {
            Player tmpPl = iteratorPlayers.next();
            if (tmpPl.getId().equals(id)) {
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
        Accessor.runRequestAsync(HttpMethod.POST, "player", "loginKey=" + loginKey,
                GsonSerializor.serializePlayer(p), new InsertPlayerHandler(listeners, p));
    }

    /**
     * Is called after insertion of new player finishes (successfully or not)
     */
    @Override
    public void insertPlayerFinished(InsertPlayerHandler handler) {
        //If insertion was successful
        if (handler.getException() == null) {
            //Add new player to cachedPlayers (Player has already received an ID from the webservice)
            this.cachedPlayers.add(handler.getPlayer());
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
        Accessor.runRequestAsync(HttpMethod.PUT, "player", "loginKey="+loginKey,
                GsonSerializor.serializePlayer(p), new UpdatePlayerHandler(listeners, p));
    }

    /**
     * Is called after update of player finishes (successfully or not)
     */
    @Override
    public void updatePlayerFinished(UpdatePlayerHandler handler) {
        //If update was successful
        if (handler.getException() == null) {
            //Update player in cachedPlayers
            this.cachedPlayers.remove(handler.getPlayer());
            this.cachedPlayers.add(handler.getPlayer());
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
        Accessor.runRequestAsync(HttpMethod.DELETE, "player/" + p.getId(), "loginKey="+loginKey,
                null, new RemovePlayerHandler(listeners, p));
    }

    /**
     * Is called after removal of player finishes (successfully or not)
     */
    @Override
    public void removePlayerFinished(RemovePlayerHandler handler) {
        //If removal was successful
        if (handler.getException() == null) {
            //Remove player from cachedPlayers
            this.cachedPlayers.remove(handler.getPlayer());
            notifyOnPlayersChangedListener();
        }
    }

    /**
     * Starts the update of PlayerPositions for passed player
     * (Positions are taken directly from passed player)
     * NOTE: This method is automatically called by UpdatePlayerHandler during update of player
     */
    public void setPlayerPositions(Player p, OnSetPlayerPosListener listener) throws Exception {
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
        Accessor.runRequestAsync(HttpMethod.PUT, "player/positions/" + p.getId(), "loginKey="+loginKey,
                GsonSerializor.serializePlayerPositionRequest(ppr), new SetPlayerPositionsHandler(listeners));
    }



    /**
     * Starts the login
     */
    public void login(String username, String local_pw_Unencrypted, OnLoginListener listener) throws Exception {
        //Encrypt the passed password
        String local_pwEnc = encryptPassword(local_pw_Unencrypted);
        //Add passed listener and Database to list of listeners
        ArrayList<OnLoginListener> listenersToInform = new ArrayList<>(),
                                    listenersToStore = new ArrayList<>();
        if (listener != null) {
            listenersToStore.add(listener);
        }
        listenersToInform.add(this);

        //Launch login
        Accessor.runRequestAsync(HttpMethod.POST, "player/security/login", null,
                GsonSerializor.serializeLoginCredentials(new LoginCredentials(username, local_pwEnc)),
                new LoginHandler(username, local_pw_Unencrypted, listenersToInform, listenersToStore), 2000);
    }

    /**
     * Is called after login finishes (successfully or not)
     */
    @Override
    public void loginFinished(LoginHandler handler) {
        //If login was successful
        if (handler.getException() == null) {
            try {
                //set loginKey (loginKey is used for all further access to webservice)
                loginKey = handler.getLoginKey();

                //Set currentlyLoggedInPlayer to save the username of the logged in player
                // so the setting of currentlyLoggedInPlayer in loadPlayersFinished works
                // (This method is called before loadAllPlayers is finished, so otherwise
                // getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()) wouldn't work)
                setCurrentlyLoggedInPlayer(new Player(handler.getUsername(), "tmpname", false));

                //save password as it's not available in loadPlayersFinished()
                LocalUserData lud = loadLocalUserData(getContext());
                if (lud != null) {
                    lud.setPassword(handler.getPassword());
                    lud.setLoggedIn(true);
                }
                else {
                    lud = new LocalUserData(null, handler.getPassword(), true);
                }

                saveLocalUserData(lud, getContext());
                initOfflineSavedData(getContext());
                setOnline(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (OnLoginListener listener: handler.getStoredListeners()) {
            listener.loginFinished(handler);
        }
    }

    public void logout(Context ctx) {
        setCurrentlyLoggedInPlayer(null);
        loginKey = null;

        LocalUserData localUserData = loadLocalUserData(ctx);
        saveLocalUserData(new LocalUserData(localUserData.getPlayer(), localUserData.getPassword(), false), ctx);
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
     * NOTE: This method is automatically called by InsertPlayerHandler and if updatePassword is checked by UpdatePlayerHandler
     */
    public void setPassword(Player p, String pw, OnSetPasswordListener listener) throws Exception {
        //Add passed listener to list of listeners
        ArrayList<OnSetPasswordListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Launch password update
        Accessor.runRequestAsync(HttpMethod.PUT, "player/security/" + p.getId(), "loginKey="+loginKey,
                GsonSerializor.serializeLoginCredentials(new LoginCredentials(null, encryptPassword(pw))),
                new SetPasswordHandler(listeners, p));
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
        Accessor.runRequestAsync(HttpMethod.GET, "game", "loginKey="+loginKey, null, new LoadAllGamesHandler(listeners));
    }

    /**
     * Is called after loading all games finishes (successfully or not)
     */
    @Override
    public void loadGamesFinished(LoadAllGamesHandler handler) {
        ArrayList<Game> tmpListGames = new ArrayList<>();

        //If loading was successful
        if (handler.getException() == null) {
            //Refresh cachedGames using loaded games from LoadAllGamesHandler
            for (Game g : handler.getGames()) {
                //if game was already loaded, copy over participations
                Game tmpGame = this.cachedGames.ceiling(g);
                if (tmpGame != null && tmpGame.equals(g)) {
                    for (Participation part: tmpGame.getParticipations()) {
                        g.addParticipation(part);
                    }
                }
                tmpListGames.add(g);
            }

            this.cachedGames.clear();
            this.cachedGames.addAll(tmpListGames);

            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Returns an ArrayList of all loaded Games
     */
    public ArrayList<Game> getCachedGames() {
        TreeSet<Game> localAndOnlineGames = new TreeSet<>();
        localAndOnlineGames.addAll(cachedGames);
        localAndOnlineGames.addAll(localGames);

        return new ArrayList<>(localAndOnlineGames);
    }

    /**
     * Returns a Game by id
     * NOTE: Only the local collection is searched
     */
    public Game getGameByID(int id) {
        Game game = null;
        Iterator<Game> iteratorGames = this.cachedGames.iterator();
        while (iteratorGames.hasNext() && game == null) {
            Game tmpG = iteratorGames.next();
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
        Accessor.runRequestAsync(HttpMethod.POST, "game", "loginKey="+loginKey,
                GsonSerializor.serializeGame(g), new InsertGameHandler(listeners, g));
    }

    /**
     * Is called after insertion of game finishes (successfully or not)
     */
    @Override
    public void insertGameFinished(InsertGameHandler handler) {
        //If insertion was successful
        if (handler.getException() == null) {
            //Add game to cachedGames
            this.cachedGames.add(handler.getGame());
            notifyOnGamesUpdatedListener();
        }
    }

    /**
     * Starts the insertion of passed participation
     * NOTE: This method is automatically called by InsertGameHandler during the insertion of a new Game
     */
    public void insert(Participation p, OnParticipationInsertedListener listener) throws Exception {
        //Add passed listener to list of listeners
        ArrayList<OnParticipationInsertedListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }

        //Launch insertion
        Accessor.runRequestAsync(HttpMethod.POST, "participation",
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId() + "&loginKey=" + loginKey,
                GsonSerializor.serializeParticipation(p), new InsertParticipationHandler(listeners));
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
        Accessor.runRequestAsync(HttpMethod.PUT, "game", "loginKey="+loginKey,
                GsonSerializor.serializeGame(g), new UpdateGameHandler(listeners, g));
    }

    /**
     * Is called after update of games finishes (successfully or not)
     */
    @Override
    public void updateGameFinished(UpdateGameHandler handler) {
        //If update was successful
        if (handler.getException() == null) {
            //Update game in cachedGames
            this.cachedGames.remove(handler.getGame());
            this.cachedGames.add(handler.getGame());
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
        Accessor.runRequestAsync(HttpMethod.PUT, "participation",
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId() + "&loginKey=" + loginKey,
                GsonSerializor.serializeParticipation(p), new UpdateParticipationHandler(listeners));
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
        Accessor.runRequestAsync(HttpMethod.DELETE, "game/" + g.getId(), "loginKey="+loginKey,
                null, new RemoveGameHandler(listeners, g));
    }

    /**
     * Is called after removal of games finishes (successfully or not)
     */
    @Override
    public void removeGameFinished(RemoveGameHandler handler) {
        //If removal was successful
        if (handler.getException() == null) {
            //Remove game from cachedGames
            this.cachedGames.remove(handler.getGame());
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
        Accessor.runRequestAsync(HttpMethod.GET, "participation/byGame/" + g.getId(), "loginKey="+loginKey,
                null, new LoadParticipationsHandler(listeners, g.getId()));
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

            notifyOnGamesUpdatedListener();
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

    public boolean isToast() {
        return !this.preferences.getBoolean("preference_usesnackbar", true);
    }

    /**********************************************************************************
     *                              OFFLINE MODE METHODS                              *
     **********************************************************************************/


    /**
     * Loads the locally saved userdata (for offline login)
     * @return Local User Data of logged in player or null if data is not found
     */
    public LocalUserData loadLocalUserData(Context ctx) {
        LocalUserData userData;

        try {
            FileInputStream fis = ctx.openFileInput(USERDATA_FILE);
            ObjectInputStream is = new ObjectInputStream(fis);
            userData = (LocalUserData) is.readObject();
            is.close();
            fis.close();
        }
        catch (Exception ex) {
            userData = null;
        }

        return userData;
    }

    /**
     * Saves local userdata (for offline login)
     */
    private void saveLocalUserData(LocalUserData data, Context ctx) {

        try {
            FileOutputStream fos = ctx.openFileOutput(USERDATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.flush();
            fos.flush();
            os.close();
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Offline login:
     * Allows the user to log in without internet/server connection based on their last logged in user account
     */
    public boolean loginLocal(String username, String password, Context ctx) throws Exception {
        boolean isSuccess = false;
        LocalUserData lud = loadLocalUserData(ctx);

        setOnline(false);

        if (lud != null) {
            if (lud.getPlayer().getUsername().equals(username) && lud.getPassword().equals(password)) {
                setCurrentlyLoggedInPlayer(lud.getPlayer());
                isSuccess = true;

                initLocallySavedData(ctx);
                initOfflineSavedData(ctx);

                lud.setLoggedIn(true);
                saveLocalUserData(lud, ctx);
            }
        }
        else {
            throw new NoLocalDataException();
        }
        return isSuccess;
    }

    /**
     * Loads the offline saved data (players and games created in offline mode)
     * and set the collection in this class if load was successful
     */
    private void initOfflineSavedData(Context ctx) {
        LocalData localData = loadLocalData(ctx);

        if (localData.getLocalPlayers() != null) {
            localPlayers = localData.getLocalPlayers();
        }
        if (localData.getLocalGames() != null) {
            localGames = localData.getLocalGames();
        }
    }

    /**
     * Loads the locally saved data of players and games which are cached
     * and set the collection in this class if load was successful
     */
    private void initLocallySavedData(Context ctx) {
        LocalData localData = loadLocalData(ctx);

        if (localData.getLocalPlayers() != null) {
            cachedPlayers = localData.getAllPlayers();
        }
        if (localData.getLocalGames() != null) {
            cachedGames = localData.getAllGames();
        }
    }

    /**
     * Loads the locally saved data (players and games created in offline mode)
     * @return Local Data or null if file doesn't exist / cannot be loaded
     */
    private LocalData loadLocalData(Context ctx) {
        LocalData data;

        try {
            FileInputStream fis = ctx.openFileInput(DATA_FILE);
            ObjectInputStream is = new ObjectInputStream(fis);
            data = (LocalData) is.readObject();
            is.close();
            fis.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            data = null;
        }

        return data;
    }

    /**
     * Saves the Local Data (players and games created in offline mode)
     */
    private void saveLocalData(LocalData data, Context ctx) {
        try {
            FileOutputStream fos = ctx.openFileOutput(DATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.flush();
            fos.flush();
            os.close();
            fos.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return Connection status of the app (online/offline)
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * Sets the connection status of the app,
     * notifies listeners and
     * tires to upload local data to webservice if app is online
     */
    private void setOnline(boolean online) {
        notifyOnOnlineStatusChangedListener();

        //if was offline and is now online, try to upload locally saved data
        if (online && isLocallySavedDataAvailable() && !isLocalDataUploadInProgress) {
            uploadLocallySavedData();
        }

        isOnline = online;
    }

    /**
     * Uploads local data to webservice
     */
    private void uploadLocallySavedData() {
        try {
            isLocalDataUploadInProgress = true;
            uploadLocallySavedPlayers();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Uploads local players to webservice (sets password as well)
     */
    private void uploadLocallySavedPlayers() throws Exception {
        numPlayersToUpload = localPlayers.size();

        if (numPlayersToUpload == 0) {
            onUploadPlayerFinished();
        }
        else {
            //Upload locally saved players
            for (final PlayerWithPassword p : localPlayers) {
                insert(p.getPlayer(), new OnPlayerInsertedListener() {
                    @Override
                    public void insertPlayerFinished(InsertPlayerHandler handler) {
                        try {
                            if (handler.getException() == null) {
                                setIdForPlayersOfLocalGames(handler.getPlayerLocal(), handler.getPlayer());
                                setPassword(handler.getPlayer(), p.getPassword(), null);
                                removePlayerLocallyForce(handler.getPlayerLocal());
                                onUploadPlayerFinished();
                            } else if (handler.getException().getClass().equals(DuplicateUsernameException.class)) {
                                setIdForPlayersOfLocalGames(handler.getPlayerLocal(), handler.getPlayer());
                                removePlayerLocallyForce(handler.getPlayerLocal());
                                onUploadPlayerFinished();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * replaces the player with a temp. local id with the real one after webservice insertion
     */
    private void setIdForPlayersOfLocalGames(Player oldPlayer, Player newPlayer) {
        for (Game g: localGames) {
            for (Participation part: g.getParticipations()) {
                if (part.getPlayer().equals(oldPlayer)) {
                    part.setPlayer(newPlayer);
                }
            }
        }
    }

    /**
     * Checks if a player is participating in any local games
     */
    private boolean isPlayerParticipationInLocalGame(Player player) {
        boolean isParticipating = false;

        Iterator<Game> itLocalGames = localGames.iterator();
        Iterator<Participation> itParticipations;

        while (itLocalGames.hasNext() && !isParticipating) {
            itParticipations = itLocalGames.next().getParticipations().iterator();
            while (itParticipations.hasNext() && !isParticipating) {
                if (itParticipations.next().getPlayer().equals(player)) {
                    isParticipating = true;
                }
            }
        }

        return isParticipating;
    }

    /**
     * Uploads local games to webservice once all players are inserted (otherwise problems with foreign keys in DB)
     */
    private void onUploadPlayerFinished() {
        try {
            numPlayersToUpload--;

            if (numPlayersToUpload <= 0) {
                uploadLocallySavedGames();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * sets isLocalDataUploadInProgress to false once all games are uploaded
     */
    private void onUploadGameFinished() {
        try {
            numGamesToUpload--;

            if (numGamesToUpload <= 0) {
                isLocalDataUploadInProgress = false;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Uploads local games to webservice (inserts participations as well)
     */
    private void uploadLocallySavedGames() throws Exception {
        numGamesToUpload = localGames.size();

        if (numGamesToUpload == 0) {
            onUploadGameFinished();
        }

        //Upload locally saved games
        for (Game g : localGames) {
            insert(g, new OnGameInsertedListener() {
                @Override
                public void insertGameFinished(InsertGameHandler handler) {
                    try {
                        if (handler.getException() == null) {
                            removeGameLocally(handler.getGameLocal());
                            onUploadGameFinished();
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
    }


    /*
     * If app is online, try to load all players and games
     * If offline, tries to log in and then load data
     */
    public void tryRefreshData(Context ctx, final OnLoadAllPlayersListener loadPListener,
                                final OnLoadAllGamesListener loadGListener) throws Exception {

        if (!isOnline()) {
            LocalUserData lud = loadLocalUserData(ctx);
            if (lud != null) {
                login(lud.getPlayer().getUsername(), lud.getPassword(), new OnLoginListener() {
                    @Override
                    public void loginFinished(LoginHandler handler) {
                    try {
                        if (handler.getException() == null) {
                            //set loginKey (loginKey is used for all further access to webservice)
                            loginKey = handler.getLoginKey();
                            setOnline(true);

                            //Set currentlyLoggedInPlayer to save the username of the logged in player
                            // so the setting of currentlyLoggedInPlayer in loadPlayersFinished works
                            // (This method is called before loadAllPlayers is finished, so otherwise
                            // getPlayerByUsername(getCurrentlyLoggedInPlayer().getUsername()) wouldn't work)
                            setCurrentlyLoggedInPlayer(new Player(handler.getUsername(), "tmpname", false));
                        }

                        //causes exception if login failed
                        loadAllPlayers(loadPListener);
                        loadAllGames(loadGListener);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                });
            }
        }
        else {
            //if db is online but there is locally saved data available for upload, upload data
            setOnline(true);

            loadAllPlayers(loadPListener);
            loadAllGames(loadGListener);
        }
    }

    private boolean isLocallySavedDataAvailable() {
        return !localGames.isEmpty() || !localPlayers.isEmpty();
    }


    /**
     * Inserts a new player locally (in offline mode)
     */
    public void insertPlayerLocally(Player p, String password) throws DuplicateUsernameException {
        TreeSet<Player> onlineAndLocalPlayers = new TreeSet<>(new PlayerComparatorUsername());
        onlineAndLocalPlayers.addAll(getCachedPlayers());

        if (!onlineAndLocalPlayers.contains(p)) {
            p.setLocallySavedOnly(true);
            //set id (negative for local players)
            if (localPlayers.size() > 0) {
                p.setId(localPlayers.first().getPlayer().getId() - 1);
            } else {
                p.setId(-1);
            }
            localPlayers.add(new PlayerWithPassword(p, password));
            notifyOnPlayersChangedListener();
        }
        else {
            throw new DuplicateUsernameException();
        }
    }

    /**
     * Updates an player (has to exist locally) locally
     */
    public void updatePlayerLocally(Player p, String password) throws CouldNotUpdatePlayerException, DuplicateUsernameException {
        PlayerWithPassword updatedPlayer = new PlayerWithPassword(p, password);

        if (localPlayers.contains(updatedPlayer)) {
            TreeSet<Player> onlineAndLocalPlayers = new TreeSet<>(new PlayerComparatorUsername());
            onlineAndLocalPlayers.addAll(getCachedPlayers());

            //if username is not used or user with same username equals this user (happens when username is not changed in update)
            if (!onlineAndLocalPlayers.contains(p) ||
                    onlineAndLocalPlayers.ceiling(updatedPlayer.getPlayer()).getId().equals(updatedPlayer.getPlayer().getId())) {

                //if password should not be updated
                if (password == null) {
                    PlayerWithPassword originalPl = localPlayers.ceiling(updatedPlayer);
                    updatedPlayer.setPassword(originalPl.getPassword());
                }

                //remove old player and add new one (player is identified by id)
                localPlayers.remove(updatedPlayer);
                localPlayers.add(updatedPlayer);
                notifyOnPlayersChangedListener();
            }
            else {
                throw new DuplicateUsernameException(p.getUsername());
            }
        }
        else {
            throw new CouldNotUpdatePlayerException("Player not found locally");
        }
    }

    /**
     * Updates an player (has to exist locally) locally
     * Does not change password
     */
    public void updatePlayerLocally(Player p) throws CouldNotUpdatePlayerException, DuplicateUsernameException {
        updatePlayerLocally(p, null);
    }

    /**
     * Removes locally saved player (checks if player is participating in local game)
     */
    public void removePlayerLocally(Player p) throws CannotDeletePlayerOfLocalGameException {
        if (!isPlayerParticipationInLocalGame(p)) {
            removePlayerLocallyForce(p);
        }
        else {
            throw new CannotDeletePlayerOfLocalGameException();
        }
    }

    /**
     * Removes locally saved player (does NOT check if player is participating in local game)
     */
    private void removePlayerLocallyForce(Player p) {
        boolean removed = false;

        Iterator<PlayerWithPassword> iterator = localPlayers.iterator();
        while (iterator.hasNext() && !removed) {
            PlayerWithPassword currPl = iterator.next();

            if (currPl.getPlayer().getUsername().equals(p.getUsername())) {
                localPlayers.remove(currPl);
                removed = true;
            }
        }

        notifyOnPlayersChangedListener();
    }

    /**
     * Inserts a new game locally (in offline mode)
     */
    public void insertGameLocally(Game g) {
        g.setLocallySavedOnly(true);
        //set id (negative for local players)
        if (localGames.size() > 0) {
            g.setId(localGames.first().getId() - 1);
        } else {
            g.setId(-1);
        }
        localGames.add(g);
        notifyOnGamesUpdatedListener();
    }

    /**
     * Updates a game locally (in offline mode)
     */
    public void updateGameLocally(Game g) throws CouldNotUpdateGameException {
        if (localGames.contains(g)) {
            //remove old game and add new one (game is identified by date and id)
            localGames.remove(g);
            localGames.add(g);
            notifyOnGamesUpdatedListener();
        }
        else {
            throw new CouldNotUpdateGameException("Game not found locally");
        }
    }

    /**
     * Removes locally saved game
     */
    public void removeGameLocally(Game g) {
        localGames.remove(g);
        notifyOnGamesUpdatedListener();
    }
}