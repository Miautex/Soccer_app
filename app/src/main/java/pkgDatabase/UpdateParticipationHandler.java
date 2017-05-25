package pkgDatabase;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnParticipationUpdatedListener;
import pkgException.CouldNotUpdateParticipationException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class UpdateParticipationHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnParticipationUpdatedListener> listeners;

    protected UpdateParticipationHandler(ArrayList<OnParticipationUpdatedListener> listeners) {
        this.listeners = listeners;
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
                Result r = GsonSerializor.deserializeResult(response.getJson());
                if (!r.isSuccess()) {
                    throw new CouldNotUpdateParticipationException();
                }
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
