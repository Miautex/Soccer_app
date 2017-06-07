package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.InsertPlayerHandler;

public interface OnPlayerInsertedListener {
    void insertPlayerFinished(InsertPlayerHandler handler);
}
