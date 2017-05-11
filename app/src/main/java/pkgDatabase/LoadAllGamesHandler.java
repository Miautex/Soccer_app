package pkgDatabase;

import java.util.ArrayList;

import pkgData.Game;
import pkgData.GsonSerializor;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgResult.GameResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;


public class LoadAllGamesHandler implements WebRequestTaskListener {
    private ArrayList<OnLoadAllGamesListener> listeners;

    protected LoadAllGamesHandler(ArrayList<OnLoadAllGamesListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        ArrayList<Game> games = null;

        try {
            if (response.getException() != null) {
                throw response.getException();
            }
            else if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            }
            else {
                GameResult gs = GsonSerializor.deserializeGameResult(response.getJson());
                games = gs.getContent();
                success(games);
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    private void success(ArrayList<Game> games) {
        for (OnLoadAllGamesListener listener: listeners) {
            listener.loadGamesSuccessful(games);
        }
    }

    private void failed(Exception ex) {
        for (OnLoadAllGamesListener listener: listeners) {
            listener.loadGamesFailed(ex);
        }
    }
}
