package pkgDatabase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

import group2.schoolproject.a02soccer.R;
import pkgComparator.PlayerComparatorName;
import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgData.PlayerPositionRequest;
import pkgDatabase.pkgListener.OnGamesUpdatedListener;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgDatabase.pkgListener.OnLoginListener;
import pkgDatabase.pkgListener.OnPlayersUpdatedListener;
import pkgException.CouldNotDeleteGameException;
import pkgException.CouldNotDeletePlayerException;
import pkgException.CouldNotSetPlayerPositionsException;
import pkgException.DuplicateUsernameException;
import pkgException.PasswordTooShortException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgResult.SingleGameResult;
import pkgResult.SinglePlayerResult;
import pkgWSA.Accessor;
import pkgWSA.AccessorResponse;
import pkgWSA.HttpMethod;

public class Database extends Application implements OnLoginListener, OnLoadAllPlayersListener,
        OnLoadAllGamesListener, OnLoadParticipationsListener {

    public static final int MIN_LENGTH_PASSWORD = 5;
    private Context ctx = null;

    private static Database instance = null;
    private Player currentlyLoggedInPlayer = null;
    private Locale locale;
    private TreeSet<Player> allPlayers = null;
    private TreeSet<Game> allGames = null;
    private SharedPreferences preferences = null;
    private boolean initialLogin = true;

    private ArrayList<OnPlayersUpdatedListener> playersChangedListener = null;
    private ArrayList<OnGamesUpdatedListener> gamesChangedListener = null;

    public void setLocale(Locale loc) {
        locale = loc;
    }

    public Locale getLocale() {
        return locale;
    }

    private Database() {
        allPlayers = new TreeSet<>();
        allGames = new TreeSet<>();
        playersChangedListener = new ArrayList<>();
        gamesChangedListener = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addOnPlayersUpdatedListener(OnPlayersUpdatedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        if (playersChangedListener.contains(listener)) {
            throw new IllegalArgumentException("listener already registered");
        }

        playersChangedListener.add(listener);
    }

    public void addOnGamesUpdatedListener(OnGamesUpdatedListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        if (gamesChangedListener.contains(listener)) {
            throw new IllegalArgumentException("listener already registered");
        }

        gamesChangedListener.add(listener);
    }

    private void notifyOnPlayersUpdatedListener() {
        for (OnPlayersUpdatedListener listener : playersChangedListener) {
            if (listener != null) {
                listener.playersChanged();
            }
        }
    }

    private void notifyOnGamesUpdatedListener() {
        for (OnGamesUpdatedListener listener : gamesChangedListener) {
            if (listener != null) {
                listener.gamesChanged();
            }
        }
    }

    public void setContext(Context ctx) throws Exception {
        if (ctx == null) {
            throw new Exception("Context may not be null");
        }
        else {
            this.ctx = ctx;
        }
    }

    public Context getContext() {
        return this.ctx;
    }

    /**
     * Returns a copy of the currently logged in player
     *
     * @return a COPY of the currently logged in player
     */
    public Player getCurrentlyLoggedInPlayer() {
        return currentlyLoggedInPlayer;
    }

    public void loadAllPlayers(OnLoadAllPlayersListener listener) throws Exception {
        ArrayList<OnLoadAllPlayersListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        Accessor.runRequestAsync(HttpMethod.GET, "player",
                null, null, new LoadAllPlayersHandler(listeners));
    }

    public ArrayList<Player> getAllPlayers() {
        TreeSet<Player> sortingTs = new TreeSet<Player>(new PlayerComparatorName());
        sortingTs.addAll(allPlayers);

        return new ArrayList<>(sortingTs);
    }

    public Player getPlayerByUsername(String username) {
        Player player = null;
        Iterator<Player> iteratorPlayers = allPlayers.iterator();
        Player tmpPl = null;

        while (iteratorPlayers.hasNext() && player==null) {
            tmpPl = iteratorPlayers.next();

            if (tmpPl.getUsername().equals(username)) {
                player = tmpPl;
            }
        }

        return player;

    }

    public Game getGameByID(int id) {
        Game game = null;
        Iterator<Game> iteratorGames = allGames.iterator();
        Game tmpG = null;

        while (iteratorGames.hasNext() && game==null) {
            tmpG = iteratorGames.next();

            if (tmpG.getId() == id) {
                game = tmpG;
            }
        }

        return game;

    }

    public void loadAllGames(OnLoadAllGamesListener listener) throws Exception {
        ArrayList<OnLoadAllGamesListener> listeners = new ArrayList<>();
        if (listener != null) {
            listeners.add(listener);
        }
        listeners.add(this);

        Accessor.runRequestAsync(HttpMethod.GET, "game",
                null, null, new LoadAllGamesHandler(listeners));
    }

    public ArrayList<Game> getAllGames() {
        return new ArrayList<>(allGames);
    }


    public Player insert(Player p) throws Exception {
        Player player = null;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.POST, "player", null, GsonSerializor.serializePlayer(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(response.getJson());

            if (!spr.isSuccess() && spr.getError() != null && spr.getError().getErrorMessage().
                    contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException();
            }

            if (!spr.isSuccess()) {
                throw new Exception(spr.getError().getErrorMessage());
            } else {
                player = spr.getContent();

                for (PlayerPosition pos : p.getPositions()) {
                    player.addPosition(pos);
                }

                //set Positions
                Result r = setPlayerPositions(player);

                allPlayers.add(player);
                notifyOnPlayersUpdatedListener();

                if (!r.isSuccess()) {
                    throw new CouldNotSetPlayerPositionsException();
                }
            }
        }

        return player;
    }

    public Game insert(Game g) throws Exception {
        Game game = null;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.POST, "game", null, GsonSerializor.serializeGame(g));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else {
            SingleGameResult sgr = GsonSerializor.deserializeSingleGameResult(response.getJson());

            if (!sgr.isSuccess()) {
                throw new Exception(sgr.getError().getErrorMessage());
            } else {
                game = sgr.getContent();
                allGames.add(game);
                notifyOnGamesUpdatedListener();
            }
        }
        return game;
    }

    public boolean insert(Participation p) throws Exception {
        Result r = null;

        AccessorResponse response = Accessor.runRequestSync(HttpMethod.POST, "participation",
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId(),
                GsonSerializor.serializeParticipation(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else {
            r = GsonSerializor.deserializeResult(response.getJson());
        }

        return r.isSuccess();
    }

    public boolean update(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.PUT, "player", null, GsonSerializor.serializePlayer(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();

            if (!isSuccess && r.getError() != null && r.getError().getErrorMessage().contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException();
            }
            else {
                allPlayers.remove(p);
                allPlayers.add(p);

                r = setPlayerPositions(p);

                if (!r.isSuccess()) {
                    throw new Exception(ctx.getString(R.string.msg_CouldNotSetPositions));
                }

                if (isSuccess && p.equals(currentlyLoggedInPlayer)) {
                    currentlyLoggedInPlayer = getPlayerByUsername(p.getUsername());
                }

                notifyOnPlayersUpdatedListener();
            }

        }

        return isSuccess;
    }

    public boolean update(Game g) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.PUT, "game", null, GsonSerializor.serializeGame(g));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();

            if (isSuccess) {
                allGames.remove(g);
                allGames.add(g);

                notifyOnGamesUpdatedListener();
            }

        }
        return isSuccess;
    }

    public boolean update(Participation p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.PUT, "participation",
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId(),
                GsonSerializor.serializeParticipation(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        } else if (response.getJson() != null && !response.getJson().isEmpty()) {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();
        }

        return isSuccess;
    }

    private Result setPlayerPositions(Player p) throws Exception {
        AccessorResponse response;
        PlayerPositionRequest ppr = new PlayerPositionRequest();

        ppr.setATTACK(p.getPositions().contains(PlayerPosition.ATTACK));
        ppr.setDEFENSE(p.getPositions().contains(PlayerPosition.DEFENSE));
        ppr.setGOAL(p.getPositions().contains(PlayerPosition.GOAL));
        ppr.setMIDFIELD(p.getPositions().contains(PlayerPosition.MIDFIELD));

        response = Accessor.runRequestSync(HttpMethod.PUT, "player/positions/" + p.getId(),
                null, GsonSerializor.serializePlayerPositionRequest(ppr));

        return GsonSerializor.deserializeResult(response.getJson());
    }

    public boolean remove(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.DELETE, "player/" + p.getId(), null, null);

        if (response.getResponseCode() == 500) {
            throw new CouldNotDeletePlayerException();
        } else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();
            allPlayers.remove(p);
            notifyOnPlayersUpdatedListener();
        }

        return isSuccess;
    }

    public boolean remove(Game g) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.runRequestSync(HttpMethod.DELETE, "game/" + g.getId(), null, null);

        if (response.getResponseCode() == 500) {
            throw new CouldNotDeleteGameException();
        } else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            if (!r.isSuccess()) {
                throw new CouldNotDeleteGameException();
            }
            else {
                allGames.remove(g);
                notifyOnGamesUpdatedListener();
            }
        }

        return isSuccess;
    }

    /**
     * Loads all participations of given game and adds them to the game
     */
    public void getParticipationsOfGame(Game g, OnLoadParticipationsListener listener) throws Exception {
        boolean isSuccess = false;
        ArrayList<OnLoadParticipationsListener> listeners = new ArrayList<>();
        listeners.add(listener);
        listeners.add(this);

        Accessor.runRequestAsync(HttpMethod.GET, "participation/byGame/" + g.getId(),
                        null, null, new LoadParticipationsHandler(listeners, g.getId()));
    }

    /**
     * Checks whether the passed username and password are valid or not
     * If username and password are correct, this user is set to currentlyLoggedInUser
     *
     * @param username             the username
     * @param local_pw_Unencrypted the unencrypted password
     * @param listener             listener, which is called once the login is done or failed
     */
    public void login(String username, String local_pw_Unencrypted, OnLoginListener listener) throws Exception {
        String local_pwEnc = encryptPassword(local_pw_Unencrypted);
        ArrayList<OnLoginListener> listeners = new ArrayList<>();
        listeners.add(listener);
        listeners.add(this);

        Accessor.runRequestAsync(HttpMethod.GET, "player/security/" + username,
                null, null, new LoginHandler(username, local_pwEnc, listeners));
    }

    public boolean setPassword(Player p, String pw) throws Exception {
        boolean isSuccess = false;

        if (pw.length() < MIN_LENGTH_PASSWORD) {
            throw new PasswordTooShortException(MIN_LENGTH_PASSWORD);
        }
        else {
            String local_pwEnc = encryptPassword(pw);
            AccessorResponse response = Accessor.runRequestSync(HttpMethod.PUT,
                    "player/security/" + Integer.toString(p.getId()), null, GsonSerializor.serializePassword(local_pwEnc));

            if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            } else {
                Result r = GsonSerializor.deserializeResult(response.getJson());
                isSuccess = r.isSuccess();
            }
        }
        return isSuccess;
    }

    private String encryptPassword(String pwInput) {
        String hash = null;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(pwInput.getBytes(), 0, pwInput.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }





    @Override
    public void loadPlayersSuccessful(Collection<Player> players) {
        allPlayers.clear();

        for (Player p : players) {
            allPlayers.add(p);
        }

        currentlyLoggedInPlayer = getPlayerByUsername(currentlyLoggedInPlayer.getUsername());

        notifyOnPlayersUpdatedListener();
    }

    @Override
    public void loadPlayersFailed(Exception ex) {
        System.out.println("-------------LOAD PLAYERS FAILED");
        ex.printStackTrace();
    }

    @Override
    public void loadGamesSuccessful(Collection<Game> games) {
        allGames.clear();

        for (Game g : games) {
            allGames.add(g);
        }

        notifyOnGamesUpdatedListener();
    }

    @Override
    public void loadGamesFailed(Exception ex) {
        System.out.println("-------------LOAD GAMES FAILED");
        ex.printStackTrace();
    }

    @Override
    public void loginSuccessful(String username) {
        try {
            currentlyLoggedInPlayer = new Player(username, "tmpname", false);       //remember username of logged in player
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loginFailed(Exception ex) {
        System.out.println("-------------LOGIN FAILED");
        ex.printStackTrace();
    }

    public boolean isToast(){
        return !preferences.getBoolean("preference_usesnackbar", true);
    }

    public void initPreferences (Context ctx) {
        preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    @Override
    public void loadParticipationsSuccessful(Collection<Participation> participations, int gameID) {
        Game game = getGameByID(gameID);
        game.removeAllParticipations();
        for (Participation p: participations) {
            game.addParticipation(p);
        }
    }

    @Override
    public void loadParticipationsFailed(Exception ex) {
        System.out.println("-------------LOAD PARTICIPATIONS FAILED");
        ex.printStackTrace();
    }

    public String getStoredUsername() {
        return preferences.getString("preference_user_username", "");
    }

    public String getStoredPassword() {
        return preferences.getString("preference_user_password", "");
    }

    public boolean isAutologin() {
        return preferences.getBoolean("preference_user_autologin", false);
    }

    public void setInitialLogin (boolean initialLogin) {
        this.initialLogin = initialLogin;
    }

    public boolean isInitialLogin () {
        return initialLogin;
    }

    public void logout () {
        currentlyLoggedInPlayer = null;
    }
}