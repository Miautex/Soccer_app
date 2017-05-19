package pkgDatabase.pkgListener;

import java.util.Collection;

import pkgData.Participation;

public interface OnLoadParticipationsListener {
    void loadParticipationsSuccessful(Collection<Participation> participations, int gameID);
    void loadParticipationsFailed(Exception ex);
}
