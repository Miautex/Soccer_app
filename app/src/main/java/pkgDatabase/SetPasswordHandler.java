package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnSetPasswordListener;
import pkgException.CouldNotSetPasswordException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class SetPasswordHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnSetPasswordListener> listeners;
    private Player player = null;

    protected SetPasswordHandler(ArrayList<OnSetPasswordListener> listeners, Player p) {
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
                throw new CouldNotSetPasswordException();
            }
        } catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnSetPasswordListener listener: listeners) {
            listener.setPasswordFinished(this);
        }
    }

    public Player getPlayer() {
        return this.player;
    }
}