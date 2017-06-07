package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.LoadParticipationsHandler;

public interface OnLoadParticipationsListener {
    void loadParticipationsFinished(LoadParticipationsHandler handler);
}
