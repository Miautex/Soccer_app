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
import pkgResult.SingleGameResult;
import pkgResult.SinglePlayerResult;
import pkgWSA.Accessor;
import pkgWSA.AccessorResponse;
import pkgWSA.HttpMethod;

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
        ArrayList<Player> listPlayers = null;
        //AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "player", null, null);

        //Temporary for testing purposes
        AccessorResponse response = new AccessorResponse(202, "{\n" +
                "   \"type\": \"playerResult\",\n" +
                "   \"success\": true,\n" +
                "   \"content\":    [\n" +
                "            {\n" +
                "         \"admin\": false,\n" +
                "         \"goalDifference\": 56,\n" +
                "         \"id\": 1,\n" +
                "         \"name\": \"Martin\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 1,\n" +
                "         \"username\": \"martin\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": false,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 2,\n" +
                "         \"name\": \"Eliass\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"elias\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 3,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 4,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco1\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 5,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco2\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 6,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco3\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 7,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco4\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 8,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco5\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 9,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco6\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 10,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco7\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 11,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco8\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 12,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco9\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 13,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco10\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 14,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco11\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 15,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco12\"\n" +
                "      },\n" +
                "            {\n" +
                "         \"admin\": true,\n" +
                "         \"goalDifference\": 0,\n" +
                "         \"id\": 16,\n" +
                "         \"name\": \"Marco\",\n" +
                "         \"numDefeats\": 0,\n" +
                "         \"numDraws\": 0,\n" +
                "         \"numWins\": 0,\n" +
                "         \"username\": \"marco13\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n");


        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            PlayerResult ps = GsonSerializor.deserializePlayerResult(response.getJson());
            listPlayers = ps.getContent();
        }

        return listPlayers;
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
        }

        return player;
    }


    public ArrayList<Game> getAllGames() throws Exception {
        ArrayList<Game> listGames = null;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "game", null, null);

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
        AccessorResponse response = Accessor.requestJSON(HttpMethod.POST, "player", null, GsonSerializor.serializePlayer(p));

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

    public Game insert(Game g) throws Exception {
        Game game = null;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.POST, "game", null, GsonSerializor.serializeGame(g));

        if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else {
            SingleGameResult sgr = GsonSerializor.deserializeSingleGameResult(response.getJson());

            if (!sgr.isSuccess()) {
                throw new Exception(sgr.getError());
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
                "idGame=" + p.getGame().getId() + "&idPlayer=" + p.getPlayer().getId()
                , GsonSerializor.serializeParticipation(p));

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

            if (isSuccess && p.equals(currentlyLoggedInPlayer)) {
                currentlyLoggedInPlayer = p;
            }
        }

        return isSuccess;
    }

    public boolean remove(Player p) throws Exception {
        boolean isSuccess = false;
        AccessorResponse response = Accessor.requestJSON(HttpMethod.DELETE, "player/" + p.getId(), null, null);

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
        AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "player/security/" + username, null, null);

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
        AccessorResponse response = Accessor.requestJSON(HttpMethod.GET, "player/security/" + Integer.toString(p.getId()), null, null);

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
}
