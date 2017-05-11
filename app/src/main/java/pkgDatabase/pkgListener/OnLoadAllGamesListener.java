package pkgDatabase.pkgListener;

import java.util.Collection;

import pkgData.Game;

public interface OnLoadAllGamesListener {
    void loadGamesSuccessful(Collection<Game> games);
    void loadGamesFailed(Exception ex);
}
