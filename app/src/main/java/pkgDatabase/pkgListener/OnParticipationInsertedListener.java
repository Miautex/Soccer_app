package pkgDatabase.pkgListener;

import pkgDatabase.InsertParticipationHandler;

public interface OnParticipationInsertedListener {
    void insertParticipationFinished(InsertParticipationHandler handler);
}
