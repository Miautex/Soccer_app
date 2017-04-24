package pkgData;

import android.app.Application;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import pkgResult.GameResult;
import pkgResult.PlayerResult;
import pkgResult.Result;
import pkgResult.SinglePlayerResult;

public class Database extends Application {
    private static Database instance = null;
    private Player currentlyLoggedInPlayer = null;

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

    public ArrayList<Player> getPlayers() throws Exception {
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


    public ArrayList<Game> getAllGames() throws Exception {
        ArrayList<Game> listGames = null;
        GameResult gr = GsonSerializor.deserializeGameResult(TestAccessor.getAllGames());

        if (gr.isSuccess()) {
            listGames = gr.getContent();
        }
        else {
            throw new Exception(gr.getError().getMessage());
        }

        return listGames;
    }

    public void insert(Player p) throws Exception {
        SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(TestAccessor.insertPlayer(true));

        if (!spr.isSuccess()) {
            throw new Exception(spr.getError().getMessage());
        }
    }

    public void update(Player p) throws Exception {
        Result r = GsonSerializor.deserializeResult(TestAccessor.updatePlayer(true));

        if (!r.isSuccess()) {
            throw new Exception(r.getError().getMessage());
        }
    }

    public void remove(Player p) throws Exception {
        Result r = GsonSerializor.deserializeResult(TestAccessor.removePlayer(true));

        if (!r.isSuccess()) {
            throw new Exception(r.getError().getMessage());
        }
    }

    public void commit() {
        //TODO
    }

    public void rollback() {
        //TODO
    }

    /**
     * Checks whether the passed username and password are valid or not
     * If username and password are correct, this user is set to currentlyLoggedInUser
     *
     * @param  username the username
     * @param  pw_Unencrypted the unencrypted password
     * @return true, if the username and password are correct
     */
    public boolean login(String username, String pw_Unencrypted) throws Exception {
        //Temporary until webservice
        boolean isPWValid = true;
        String pwEnc = encryptPassword(pw_Unencrypted);

        if (isPWValid = (pwEnc.equals(TestAccessor.getPassword()))) {
            SinglePlayerResult spr = GsonSerializor.deserializeSinglePlayerResult(TestAccessor.getPlayerByUsername(username));
            this.currentlyLoggedInPlayer = spr.getContent();
        }
        else {
            this.currentlyLoggedInPlayer = null;
        }

        return isPWValid;
    }

    public void setPassword(Player p, String pw) {
        //TODO: Enc pw

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
