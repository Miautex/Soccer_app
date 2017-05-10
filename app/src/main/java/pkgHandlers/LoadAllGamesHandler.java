package pkgHandlers;

import java.util.ArrayList;

import pkgData.Game;
import pkgData.GsonSerializor;
import pkgListeners.OnLoadAllGamesListener;
import pkgResult.GameResult;
import pkgWSA.AccessorResponse;
import pkgWSA.AccessorRunListener;


public class LoadAllGamesHandler implements AccessorRunListener {
    private ArrayList<OnLoadAllGamesListener> listeners;

    public LoadAllGamesHandler(ArrayList<OnLoadAllGamesListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        ArrayList<Game> games = new ArrayList<>();

        try {
            if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            }
            else {
                GameResult gs = GsonSerializor.deserializeGameResult(response.getJson());
                games = gs.getContent();

                for (OnLoadAllGamesListener listener: listeners) {
                    listener.loadGamesSuccessful(games);
                }
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    @Override
    public void failed(Exception ex) {
        for (OnLoadAllGamesListener listener: listeners) {
            listener.loadGamesFailed(ex);
        }
    }
}
