package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.InsertGameHandler;

public interface OnGameInsertedListener {
    void insertGameFinished(InsertGameHandler handler);
}
