package pkgDatabase;

import java.util.ArrayList;

import pkgData.Participation;
import pkgDatabase.pkgListener.OnLoadParticipationsListener;
import pkgException.CouldNotLoadParticipationsException;
import pkgMisc.GsonSerializor;
import pkgResult.ParticipationResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoadParticipationsHandler implements WebRequestTaskListener {
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
            if (response.getException() != null) {
                throw new CouldNotLoadParticipationsException();
            }
            else if (response.getResponseCode() == 500) {
                throw new CouldNotLoadParticipationsException();
            }
            else {
                ParticipationResult ps = GsonSerializor.deserializeParticipationResult(response.getJson());
                for (Participation p: ps.getContent()) {
                    participations.add(p);
                }
                success(participations);
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    private void success(ArrayList<Participation> participations) {
        for (OnLoadParticipationsListener listener: listeners) {
            listener.loadParticipationsSuccessful(participations, gameID);
        }
    }

    private void failed(Exception ex) {
        for (OnLoadParticipationsListener listener: listeners) {
            listener.loadParticipationsFailed(ex);
        }
    }
}

