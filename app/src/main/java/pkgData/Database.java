package pkgData;

import android.app.Application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import group2.schoolproject.a02soccer.R;
import pkgException.CouldNotDeletePlayerException;
import pkgException.CouldNotSetPlayerPositionsException;
import pkgException.DuplicateUsernameException;
import pkgException.InvalidLoginDataException;
import pkgHandlers.LoadAllGamesHandler;
import pkgHandlers.LoadAllPlayersHandler;
import pkgListeners.OnLoadAllGamesListener;
import pkgListeners.OnLoadAllPlayersListener;
import pkgListeners.OnLoginListener;
import pkgResult.PositionResult;
import pkgResult.Result;
import pkgResult.SingleGameResult;
import pkgResult.SinglePlayerResult;
import pkgWSA.Accessor;
import pkgWSA.AccessorResponse;
import pkgWSA.AccessorRunListener;
import pkgWSA.HttpMethod;

public class Database extends Application implements OnLoadAllPlayersListener, OnLoadAllGamesListener {
    private static Database instance = null;
    private Player currentlyLoggedInPlayer = null;
    private Locale locale;
    private ArrayList<Player> allPlayers = new ArrayList<>();
    private ArrayList<Game> allGames = new ArrayList<>();

    public void setLocale(Locale loc){
        locale = loc;
    }

    public Locale getLocale(){
        return locale;
    }

    private Database()  {

    }

    public static Database getInstance()  {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Returns a copy of the currently logged in player
     * @return a COPY of the currently logged in player
     */
    public Player getCurrentlyLoggedInPlayer() {
        return currentlyLoggedInPlayer;
    }

    public void loadAllPlayers(OnLoadAllPlayersListener listener) throws Exception {
        ArrayList<OnLoadAllPlayersListener> listeners = new ArrayList<>();
        listeners.add(listener);
        listeners.add(this);

        Accessor.requestJSONAsync(HttpMethod.GET, "player",
                null, null, new LoadAllPlayersHandler(listeners));
    }

    public ArrayList<Player> getAllPlayers() throws Exception {
        return allPlayers;
    }

    public Player getPlayerByUsername(String username) throws Exception {
        Player player = null;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "player/" + username, null, null);

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(response.getJson());
            player = spr.getContent();
            for (PlayerPosition pp : getPlayerPositions(player.getId())) {
                player.addPosition(pp);
            }
        }

        return player;
    }

    public ArrayList<PlayerPosition> getPlayerPositions(int playerId) throws Exception {
        ArrayList<PlayerPosition> positions = new ArrayList<>();

        AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "player/positions/" + playerId, null, null);

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            PositionResult pr = GsonSerializor.deserializePositionResult(response.getJson());
            if (pr.getContent() != null) {
                positions = pr.getContent();
            }
        }

        return positions;
    }

    public void loadAllGames(OnLoadAllGamesListener listener) throws Exception {
        ArrayList<OnLoadAllGamesListener> listeners = new ArrayList<>();
        listeners.add(listener);
        listeners.add(this);

        Accessor.requestJSONAsync(HttpMethod.GET, "game",
                null, null, new LoadAllGamesHandler(listeners));
    }

    public ArrayList<Game> getAllGames() throws Exception {
        return allGames;
    }


    public Player insert(Player p) throws Exception {
        Player player = null;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.POST, "player", null, GsonSerializor.serializePlayer(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(response.getJson());

            if (!spr.isSuccess() && spr.getError() != null && spr.getError().getErrorMessage().contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException();
            }

            if (!spr.isSuccess()) {
                throw new Exception(spr.getError().getErrorMessage());
            }
            else {
                player = spr.getContent();

                //set Positions
                p.setId(player.getId());        //set id for old player for setPlayerPositions to work
                Result r = setPlayerPositions(p);

                if (!r.isSuccess()) {
                    throw new CouldNotSetPlayerPositionsException();
                }
            }
        }

        return player;
    }

    public Game insert(Game g) throws Exception {
        Game game = null;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.POST, "game", null, GsonSerializor.serializeGame(g));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SingleGameResult sgr = GsonSerializor.deserializeSingleGameResult(response.getJson());

            if (!sgr.isSuccess()) {
                throw new Exception(sgr.getError().getErrorMessage());
            }
            else {
                game = sgr.getContent();
            }
        }
        return game;
    }

    public boolean insert(Participation p) throws Exception {
        Result r = null;

        AccessorResponse response = Accessor.requestJSON(HttpMethod.POST, "participation",
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId(),
                GsonSerializor.serializeParticipation(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
             r = GsonSerializor.deserializeResult(response.getJson());
        }

        return r.isSuccess();
    }

    public boolean update(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.PUT, "player", null, GsonSerializor.serializePlayer(p));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();

            if (!isSuccess && r.getError() != null && r.getError().getErrorMessage().contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException();
            }

            r = setPlayerPositions(p);

            if (!r.isSuccess()) {
                throw new Exception(getApplicationContext().getString(R.string.msg_CouldNotSetPositions));
            }

            if (isSuccess && p.equals(currentlyLoggedInPlayer)) {
                currentlyLoggedInPlayer = getPlayerByUsername(p.getUsername());
            }
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

        response = Accessor.requestJSON(HttpMethod.PUT, "player/positions/" + p.getId(),
                null, GsonSerializor.serializePlayerPositionRequest(ppr));

        return GsonSerializor.deserializeResult(response.getJson());
    }

    public boolean remove(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.DELETE, "player/" + p.getId(), null, null);

        if (response.getResponseCode() == 500) {
            throw new CouldNotDeletePlayerException();
        }
        else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();
        }

        return isSuccess;
    }

    /**
     * Checks whether the passed username and password are valid or not
     * If username and password are correct, this user is set to currentlyLoggedInUser
     *
     * @param  username the username
     * @param  local_pw_Unencrypted the unencrypted password
     * @return true, if the username and password are correct
     */
    public void login(String username, String local_pw_Unencrypted, OnLoginListener listener) throws Exception {
        String local_pwEnc = encryptPassword(local_pw_Unencrypted);

        Accessor.requestJSONAsync(HttpMethod.GET, "player/security/" + username,
                null, null, new LoginHandler(username, local_pwEnc, listener));
    }

    public boolean setPassword(Player p, String pw) throws Exception {
        boolean isSuccess = false;
        String local_pwEnc = encryptPassword(pw);
        AccessorResponse response = Accessor.requestJSON(HttpMethod.PUT,
                "player/security/" + Integer.toString(p.getId()), null, GsonSerializor.serializePassword(local_pwEnc));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();
        }

        return isSuccess;
    }

    private String encryptPassword(String pwInput) {
        String hash = null;
        MessageDigest m= null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(pwInput.getBytes(), 0, pwInput.length());
            hash = new BigInteger(1,m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        return hash;
    }

    @Override
    public void loadPlayersSuccessful(Collection<Player> players) {
        allPlayers.clear();

        for (Player p: players) {
            allPlayers.add(p);
        }
    }

    @Override
    public void loadPlayersFailed(Exception ex) {
        System.out.println("-------------LOAD PLAYERS FAILED");
    }

    @Override
    public void loadGamesSuccessful(Collection<Game> games) {
        allGames.clear();

        for (Game g: games) {
            allGames.add(g);
        }
    }

    @Override
    public void loadGamesFailed(Exception ex) {
        System.out.println("-------------LOAD GAMES FAILED");
    }




    private class LoginHandler implements AccessorRunListener {
        private OnLoginListener listener;
        private String local_pwEnc,
                username;

        public LoginHandler(String username, String local_pwEnc, OnLoginListener listener) {
            this.listener = listener;
            this.local_pwEnc = local_pwEnc;
            this.username = username;
        }

        @Override
        public void done(AccessorResponse response) {
            try {
                if (response.getResponseCode() == 500) {
                    throw new Exception(response.getJson());
                } else {
                    if (!response.getJson().isEmpty()) {
                        String remote_pwEnc = GsonSerializor.deserializePassword(response.getJson());

                        if (local_pwEnc.equals(remote_pwEnc)) {
                            //Class is in Database-class because otherwise the bottom line would be much more complicated
                            currentlyLoggedInPlayer = getPlayerByUsername(username);
                            listener.loginSuccessful();
                        }
                        else {
                            failed(new InvalidLoginDataException());
                        }
                    }
                }
            }
            catch (Exception ex) {
                failed(ex);
            }
        }

        @Override
        public void failed(Exception ex) {
            listener.loginFailed(ex);
        }
    }
}
