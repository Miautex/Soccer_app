package pkgDatabase;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnParticipationInsertedListener;
import pkgException.CouldNotInsertParticipationException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class InsertParticipationHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnParticipationInsertedListener> listeners;

    protected InsertParticipationHandler(ArrayList<OnParticipationInsertedListener> listeners) {
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
                Result r = GsonSerializor.deserializeResult(response.getJson());

                if (!r.isSuccess()) {
                    throw new CouldNotInsertParticipationException();
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
        for (OnParticipationInsertedListener listener: listeners) {
            listener.insertParticipationFinished(this);
        }
    }
}