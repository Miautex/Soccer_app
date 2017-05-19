package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgMisc.GsonSerializor;
import pkgResult.PlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;


public class LoadAllPlayersHandler implements WebRequestTaskListener {
    private ArrayList<OnLoadAllPlayersListener> listeners;
    private ArrayList<Player> players = null;

    protected LoadAllPlayersHandler(ArrayList<OnLoadAllPlayersListener> listeners) {
        this.listeners = listeners;
        this.players = new ArrayList<>();
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
                    players.add(p);
                }
                success(players);
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
}
