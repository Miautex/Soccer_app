package pkgDatabase;

import java.util.ArrayList;
import java.util.Collection;

import pkgData.Participation;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgMisc.GsonSerializor;
import pkgResult.ParticipationResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoadParticipationsHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoadParticipationsListener> listeners;
    private ArrayList<Participation> participations = null;
    private int gameID;

    protected LoadParticipationsHandler(ArrayList<OnLoadParticipationsListener> listeners, int gameID) {
        this.listeners = listeners;
        this.participations = new ArrayList<>();
        this.gameID = gameID;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            ParticipationResult ps = GsonSerializor.deserializeParticipationResult(response.getJson());
            for (Participation p: ps.getContent()) {
                participations.add(p);
            }
        }
        catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnLoadParticipationsListener listener: listeners) {
            listener.loadParticipationsFinished(this);
        }
    }

    public Collection<Participation> getPatrticipations() {
        return this.participations;
    }

    public int getGameID() {
        return this.gameID;
    }
}

