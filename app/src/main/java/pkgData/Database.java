package pkgData;

import android.app.Application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Locale;

import pkgResult.GameResult;
import pkgResult.PlayerResult;
import pkgResult.Result;
import pkgResult.SinglePlayerResult;
import pkgTasks.DeletePlayerTask;
import pkgTasks.GetPasswordTask;
import pkgTasks.GetPlayerByUsernameTask;
import pkgTasks.InsertPlayerTask;
import pkgTasks.LoadAllGamesTask;
import pkgTasks.SetPasswordTask;
import pkgTasks.UpdatePlayerTask;
import pkgWSA.AccessorResponse;

public class Database extends Application {
    private static Database instance = null;
    private Player currentlyLoggedInPlayer = null;
    private Locale locale;

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
        Player returnPlayer = null;

        try {
            returnPlayer = (Player) currentlyLoggedInPlayer.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return returnPlayer;
    }

    public ArrayList<Player> getAllPlayers() throws Exception {
        //TODO
        ArrayList<Player> listPlayers = null;
        PlayerResult pr = GsonSerializor.deserializePlayerResult("A");

        if (pr.isSuccess()) {
            listPlayers = pr.getContent();
        }
        else {
            throw new Exception(pr.getError().getMessage());
        }

        return listPlayers;
    }

    public Player getPlayerByUsername(String username) throws Exception {
        Player player = null;
        AccessorResponse response = null;

        GetPlayerByUsernameTask task = new GetPlayerByUsernameTask();
        task.execute(username);
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(response.getJson());
            player = spr.getContent();
        }

        return player;
    }


    public ArrayList<Game> getAllGames() throws Exception {
        ArrayList<Game> listGames = null;
        AccessorResponse response = null;

        LoadAllGamesTask task = new LoadAllGamesTask();
        task.execute();
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            GameResult gs = GsonSerializor.deserializeGameResult(response.getJson());
            listGames = gs.getContent();
        }

        return listGames;
    }

    public Player insert(Player p) throws Exception {
        Player player = null;
        AccessorResponse response = null;

        InsertPlayerTask task = new InsertPlayerTask();
        task.execute(GsonSerializor.serializePlayer(p));
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(response.getJson());

            if (!spr.isSuccess()) {
                throw new Exception(spr.getError());
            }
            else {
                player = spr.getContent();
            }
        }

        return player;
    }

    public boolean update(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = null;

        UpdatePlayerTask task = new UpdatePlayerTask();
        task.execute(GsonSerializor.serializePlayer(p));
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            Result r = GsonSerializor.deserializeResult(response.getJson());
            isSuccess = r.isSuccess();

            if (isSuccess && p.equals(currentlyLoggedInPlayer)) {
                currentlyLoggedInPlayer = p;
            }
        }

        return isSuccess;
    }

    public boolean remove(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = null;

        DeletePlayerTask task = new DeletePlayerTask();
        task.execute(Integer.toString(p.getId()));
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
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
    public boolean login(String username, String local_pw_Unencrypted) throws Exception {
        boolean isPWValid = false;
        String local_pwEnc = encryptPassword(local_pw_Unencrypted);
        AccessorResponse response = null;

        GetPasswordTask task = new GetPasswordTask();
        task.execute(username);
        response = task.get();

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            String remote_pwEnc = response.getJson();
            if (local_pwEnc.equals(remote_pwEnc)) {
                isPWValid = true;

                currentlyLoggedInPlayer = getPlayerByUsername(username);
            }
        }

        return isPWValid;
    }

    public boolean setPassword(Player p, String pw) throws Exception {
        boolean isSuccess = false;
        String local_pwEnc = encryptPassword(pw);
        AccessorResponse response = null;

        SetPasswordTask task = new SetPasswordTask();
        task.execute(Integer.toString(p.getId()));
        response = task.get();

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
        } catch (NoSuchAlgorithmException e) { }

        return hash;
    }
}
