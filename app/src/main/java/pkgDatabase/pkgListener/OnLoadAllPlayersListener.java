package pkgDatabase.pkgListener;

import pkgDatabase.pkgHandler.LoadAllPlayersHandler;

public interface OnLoadAllPlayersListener {
    void loadPlayersFinished(LoadAllPlayersHandler handler);
}
