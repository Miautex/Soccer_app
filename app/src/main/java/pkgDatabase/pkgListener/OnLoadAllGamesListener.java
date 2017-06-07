package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.LoadAllGamesHandler;

public interface OnLoadAllGamesListener {
    void loadGamesFinished(LoadAllGamesHandler handler);
}
