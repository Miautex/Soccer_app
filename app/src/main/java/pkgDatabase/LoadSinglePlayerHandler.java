package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnLoadSinglePlayerListener;
import pkgMisc.GsonSerializor;
import pkgResult.SinglePlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoadSinglePlayerHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoadSinglePlayerListener> listeners;
    private Player player = null;

    protected LoadSinglePlayerHandler(ArrayList<OnLoadSinglePlayerListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            if (response.getException() != null) {
                throw response.getException();
            } else if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            } else {
                SinglePlayerResult sps = GsonSerializor.deserializeSinglePlayerResult(response.getJson());
                player = sps.getContent();
            }
        } catch (Exception ex) {
            setException(ex);
        } finally {
            finished();
        }
    }

    private void finished() {
        for (OnLoadSinglePlayerListener listener : listeners) {
            listener.loadSinglePlayerFinished(this);
        }
    }

    public Player getPlayer() {
        return this.player;
    }
}