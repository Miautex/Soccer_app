package pkgData;

import com.google.gson.Gson;

import pkgResult.GameResult;
import pkgResult.ParticipationResult;
import pkgResult.PlayerResult;
import pkgResult.Result;
import pkgResult.SingleGameResult;
import pkgResult.SinglePlayerResult;

public class GsonSerializor {
    public static String serializePlayer(Player p) {
        String retVal = null;

        Gson gson = new Gson();
        retVal = gson.toJson(p);

        return retVal;
    }

    public static String serializeGame(Game g) {
        String retVal = null;

        Gson gson = new Gson();
        retVal = gson.toJson(g);

        return retVal;
    }

    public static String serializeParticipation(Participation p) {
        String retVal = null;

        Gson gson = new Gson();
        retVal = gson.toJson(p);

        return retVal;
    }


    public static Result deserializeResult(String strResult) {
        Gson gson = new Gson();
        Result r = gson.fromJson(strResult, Result.class);

        return r;
    }

    public static GameResult deserializeGameResult(String strGameResult) {
        Gson gson = new Gson();
        GameResult gr = gson.fromJson(strGameResult, GameResult.class);

        return gr;
    }

    public static SingleGameResult deserializeSingleGameResult(String strSingleGameResult) {
        Gson gson = new Gson();
        SingleGameResult sgr = gson.fromJson(strSingleGameResult, SingleGameResult.class);

        return sgr;
    }

    public static PlayerResult deserializePlayerResult(String strPlayerResult) {
        Gson gson = new Gson();
        PlayerResult pr = gson.fromJson(strPlayerResult, PlayerResult.class);

        return pr;
    }

    public static SinglePlayerResult deserializeSinglePlayerResult(String strSinglePlayerResult) {
        Gson gson = new Gson();
        SinglePlayerResult spr = gson.fromJson(strSinglePlayerResult, SinglePlayerResult.class);

        return spr;
    }

    public static ParticipationResult deserializeParticipationResult(String strParticipationResult) {
        Gson gson = new Gson();
        ParticipationResult pr = gson.fromJson(strParticipationResult, ParticipationResult.class);

        return pr;
    }

    public static String serializePassword(String password) {
        return "{ \"password\": \"" + password + "\"}";
    }
}
