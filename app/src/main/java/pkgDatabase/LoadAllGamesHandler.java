package pkgDatabase;

import java.util.ArrayList;
import java.util.Collection;

import pkgData.Game;
import pkgDatabase.pkgListener.OnLoadAllGamesListener;
import pkgMisc.GsonSerializor;
import pkgResult.GameResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;


public class LoadAllGamesHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoadAllGamesListener> listeners;
    private ArrayList<Game> games = null;

    protected LoadAllGamesHandler(ArrayList<OnLoadAllGamesListener> listeners) {
        this.listeners = listeners;
        this.games = new ArrayList<>();
    }

    @Override
    public void done(AccessorResponse response) {
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
            }
        }
        catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnLoadAllGamesListener listener: listeners) {
            listener.loadGamesFinished(this);
        }
    }

    public Collection<Game> getGames() {
        return this.games;
    }
}
