package pkgDatabase;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnSetPlayerPosListener;
import pkgException.CouldNotSetPlayerPositionsException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class SetPlayerPositionsHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnSetPlayerPosListener> listeners;

    protected SetPlayerPositionsHandler(ArrayList<OnSetPlayerPosListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());

            if (!r.isSuccess()) {
                throw new CouldNotSetPlayerPositionsException();
            }
        } catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnSetPlayerPosListener listener: listeners) {
            listener.setPosFinished(this);
        }
    }
}