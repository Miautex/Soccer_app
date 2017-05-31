package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnLoadSinglePlayerListener;
import pkgMisc.GsonSerializor;
import pkgResult.SinglePlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

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
            //throws Exception if error happened
            handleResponse(response);

            SinglePlayerResult sps = GsonSerializor.deserializeSinglePlayerResult(response.getJson());
            player = sps.getContent();
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