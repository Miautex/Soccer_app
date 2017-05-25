package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnPlayerRemovedListener;
import pkgException.CouldNotDeletePlayerException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

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
            if (response.getException() != null) {
                throw response.getException();
            } else if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            } else {
                Result r = GsonSerializor.deserializeResult(response.getJson());

                if (!r.isSuccess()) {
                    throw new CouldNotDeletePlayerException();
                }
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