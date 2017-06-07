package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.InsertParticipationHandler;

public interface OnParticipationInsertedListener {
    void insertParticipationFinished(InsertParticipationHandler handler);
}
