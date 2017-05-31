package pkgDatabase;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnParticipationInsertedListener;
import pkgException.CouldNotInsertParticipationException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class InsertParticipationHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnParticipationInsertedListener> listeners;

    protected InsertParticipationHandler(ArrayList<OnParticipationInsertedListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());

            if (!r.isSuccess()) {
                throw new CouldNotInsertParticipationException();
            }
        } catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnParticipationInsertedListener listener: listeners) {
            listener.insertParticipationFinished(this);
        }
    }
}