package pkgDatabase.pkgListener;

import java.util.Collection;

import pkgData.Player;

public interface OnLoadAllPlayersListener {
    void loadPlayersSuccessful(Collection<Player> players);
    void loadPlayersFailed(Exception ex);
}
