package pkgDatabase;

import java.util.ArrayList;

import pkgData.Game;
import pkgDatabase.pkgListener.OnGameRemovedListener;
import pkgException.CouldNotDeletePlayerException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class RemoveGameHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnGameRemovedListener> listeners;
    private Game game;

    protected RemoveGameHandler(ArrayList<OnGameRemovedListener> listeners, Game g) {
        this.listeners = listeners;
        this.game = g;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());

            if (!r.isSuccess()) {
                throw new CouldNotDeletePlayerException();
            }
        } catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnGameRemovedListener listener: listeners) {
            listener.removeGameFinished(this);
        }
    }

    public Game getGame() {
        return this.game;
    }
}
