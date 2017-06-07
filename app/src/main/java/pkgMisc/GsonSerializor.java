package pkgMisc;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import pkgData.Game;
import pkgData.Participation;
import pkgData.Player;
import pkgResult.GameResult;
import pkgResult.ParticipationResult;
import pkgResult.PlayerResult;
import pkgResult.PositionResult;
import pkgResult.Result;
import pkgResult.SingleGameResult;
import pkgResult.SinglePlayerResult;

/**
 * @author Elias Santner
 */

public class GsonSerializor {
    private static Gson gson = new Gson();

    public static String serializeLoginCredentials(LoginCredentials credentials) {
        String retVal;

        retVal = gson.toJson(credentials);

        return retVal;
    }

    public static String serializePlayer(Player p) {
        String retVal = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(gson.toJson(p));
            jsonObject.remove("positions");
            jsonObject.remove("statistics");
            jsonObject.remove("isLocallySavedOnly");
            jsonObject.put("admin", jsonObject.get("isAdmin"));     //it's "admin" and not "isAdmin" for webservice for some reason...
            jsonObject.remove("isAdmin");
            retVal = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static String serializePlayerPositionRequest(PlayerPositionRequest ppr) {
        String retVal;

        retVal = gson.toJson(ppr);

        return retVal;
    }

    public static PositionResult deserializePositionResult(String pr) {
        PositionResult retVal;

        retVal = gson.fromJson(pr, PositionResult.class);

        return retVal;
    }

    public static String serializeGame(Game g) {
        String retVal = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(gson.toJson(g));
            jsonObject.remove("participations");
            jsonObject.remove("isLocallySavedOnly");
            retVal = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static String serializeParticipation(Participation p) {
        String retVal = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(gson.toJson(p));
            jsonObject.remove("player");
            retVal = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retVal;
    }


    public static Result deserializeResult(String strResult) {
        return gson.fromJson(strResult, Result.class);
    }

    public static GameResult deserializeGameResult(String strGameResult) {
        return gson.fromJson(strGameResult, GameResult.class);
    }

    public static SingleGameResult deserializeSingleGameResult(String strSingleGameResult) {
        return gson.fromJson(strSingleGameResult, SingleGameResult.class);
    }

    public static PlayerResult deserializePlayerResult(String strPlayerResult) {
        strPlayerResult = strPlayerResult.replaceAll("\"admin\":", "\"isAdmin\":");            //it's "admin" and not "isAdmin" for webservice for some reason...
        return gson.fromJson(strPlayerResult, PlayerResult.class);
    }

    public static SinglePlayerResult deserializeSinglePlayerResult(String strSinglePlayerResult) {
        strSinglePlayerResult = strSinglePlayerResult.replace("\"admin\":", "\"isAdmin\":");             //it's "admin" and not "isAdmin" for webservice for some reason...
        return gson.fromJson(strSinglePlayerResult, SinglePlayerResult.class);
    }

    public static ParticipationResult deserializeParticipationResult(String strParticipationResult) {
        return gson.fromJson(strParticipationResult, ParticipationResult.class);
    }

    private static class Password {
        private String password;

        public Password(String pw) {
            setPassword(pw);
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static String serializePassword(String password) {
        return gson.toJson(new Password(password), Password.class);
    }

    public static String deserializePassword(String password) {
        return (gson.fromJson(password, Password.class)).getPassword();
    }
}
