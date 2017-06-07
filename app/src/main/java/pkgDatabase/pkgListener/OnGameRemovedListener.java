package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.RemoveGameHandler;

public interface OnGameRemovedListener {
    void removeGameFinished(RemoveGameHandler handler);
}
