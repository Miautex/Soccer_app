package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.UpdateParticipationHandler;

public interface OnParticipationUpdatedListener {
    void updateParticipationFinished(UpdateParticipationHandler handler);
}
