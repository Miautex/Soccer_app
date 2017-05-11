package pkgDatabase;

import java.util.ArrayList;

import pkgData.GsonSerializor;
import pkgData.Player;
import pkgData.PlayerPosition;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgResult.PlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.AccessorRunListener;


public class LoadAllPlayersHandler implements AccessorRunListener {
    private ArrayList<OnLoadAllPlayersListener> listeners;

    protected LoadAllPlayersHandler(ArrayList<OnLoadAllPlayersListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void done(AccessorResponse response) {
        ArrayList<Player> players = new ArrayList<>();

        try {
            if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            }
            else {
                PlayerResult ps = GsonSerializor.deserializePlayerResult(response.getJson());
                players = ps.getContent();

                for (Player player : players) {
                    for (PlayerPosition pp : (Database.getInstance()).getPlayerPositions(player.getId())) {
                        player.addPosition(pp);
                    }
                }

                for (OnLoadAllPlayersListener listener: listeners) {
                    listener.loadPlayersSuccessful(players);
                }
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    @Override
    public void failed(Exception ex) {
        for (OnLoadAllPlayersListener listener: listeners) {
            listener.loadPlayersFailed(ex);
        }
    }
}
