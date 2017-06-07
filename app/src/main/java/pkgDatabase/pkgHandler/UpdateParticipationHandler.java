package pkgDatabase.pkgHandler;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnParticipationUpdatedListener;
import pkgException.CouldNotUpdateParticipationException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class UpdateParticipationHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnParticipationUpdatedListener> listeners;

    public UpdateParticipationHandler(ArrayList<OnParticipationUpdatedListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());
            if (!r.isSuccess()) {
                throw new CouldNotUpdateParticipationException();
            }
        } catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnParticipationUpdatedListener listener: listeners) {
            listener.updateParticipationFinished(this);
        }
    }
}
