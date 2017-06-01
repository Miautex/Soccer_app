package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnPlayerRemovedListener;
import pkgException.CouldNotDeletePlayerException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class RemovePlayerHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnPlayerRemovedListener> listeners;
    private Player player;

    protected RemovePlayerHandler(ArrayList<OnPlayerRemovedListener> listeners, Player p) {
        this.listeners = listeners;
        this.player = p;
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
            System.out.println("-------------- IN FINISH | ex: " + getException());
            finished();
        }
    }

    private void finished() {
        for (OnPlayerRemovedListener listener: listeners) {
            listener.removePlayerFinished(this);
        }
    }

    public Player getPlayer() {
        return this.player;
    }
}