package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.RemovePlayerHandler;

public interface OnPlayerRemovedListener {
    void removePlayerFinished(RemovePlayerHandler handler);
}
