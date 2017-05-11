package pkgDatabase.pkgListener;

import java.util.Collection;

import pkgData.PlayerPosition;

public interface OnLoadPlayerPositionsListener {
    void loadPlayerPositionsSuccessful(int playerID, Collection<PlayerPosition> positions);
    void loadPlayerPositionsFailed(Exception ex);
}
