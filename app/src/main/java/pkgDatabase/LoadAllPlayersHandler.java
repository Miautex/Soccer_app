package pkgDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import pkgMisc.GsonSerializor;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgDatabase.pkgListener.OnLoadPlayerPositionsListener;
import pkgResult.PlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;


public class LoadAllPlayersHandler implements WebRequestTaskListener, OnLoadPlayerPositionsListener {
    private ArrayList<OnLoadAllPlayersListener> listeners;
    private HashMap<Integer, Player> hmPlayers = null;
    private int numPlayerPositionsLoadingRemaining = 0;

    protected LoadAllPlayersHandler(ArrayList<OnLoadAllPlayersListener> listeners) {
        this.listeners = listeners;
        this.hmPlayers = new HashMap<>();
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            if (response.getException() != null) {
                throw response.getException();
            }
            else if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            }
            else {
                PlayerResult ps = GsonSerializor.deserializePlayerResult(response.getJson());
                for (Player p: ps.getContent()) {
                    hmPlayers.put(p.getId(), p);
                }

                numPlayerPositionsLoadingRemaining = hmPlayers.size();
                for (Player player : hmPlayers.values()) {
                    Database.getInstance().getPlayerPositions(player.getId(), this);
                }
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    private void success(ArrayList<Player> players) {
        for (OnLoadAllPlayersListener listener: listeners) {
            listener.loadPlayersSuccessful(players);
        }
    }

    private void failed(Exception ex) {
        for (OnLoadAllPlayersListener listener: listeners) {
            listener.loadPlayersFailed(ex);
        }
    }

    @Override
    public void loadPlayerPositionsSuccessful(int playerID, Collection<PlayerPosition> positions) {
        try {
            Player player = hmPlayers.get(playerID);
            ArrayList<Player> listPlayers = new ArrayList<>();

            for (PlayerPosition pos : positions) {
                player.addPosition(pos);
            }
            numPlayerPositionsLoadingRemaining--;

            if (numPlayerPositionsLoadingRemaining==0) {
                listPlayers.addAll(hmPlayers.values());
                success(listPlayers);
            }


        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    @Override
    public void loadPlayerPositionsFailed(Exception ex) {
        failed(ex);
    }
}
