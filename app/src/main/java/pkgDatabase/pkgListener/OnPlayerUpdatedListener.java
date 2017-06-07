package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.UpdatePlayerHandler;

public interface OnPlayerUpdatedListener {
    void updatePlayerFinished(UpdatePlayerHandler handler);
}
