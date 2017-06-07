package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.UpdateGameHandler;

public interface OnGameUpdatedListener {
    void updateGameFinished(UpdateGameHandler handler);
}
