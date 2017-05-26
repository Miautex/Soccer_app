package pkgDatabase;

import java.util.ArrayList;
import java.util.Collection;

import pkgData.Player;
import pkgDatabase.pkgListener.OnLoadAllPlayersListener;
import pkgMisc.GsonSerializor;
import pkgResult.PlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;


public class LoadAllPlayersHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoadAllPlayersListener> listeners;
    private ArrayList<Player> players = null;

    protected LoadAllPlayersHandler(ArrayList<OnLoadAllPlayersListener> listeners) {
        this.listeners = listeners;
        this.players = new ArrayList<>();
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            PlayerResult ps = GsonSerializor.deserializePlayerResult(response.getJson());
            for (Player p: ps.getContent()) {
                players.add(p);
            }
        }
        catch (Exception ex) {
            setException(ex);
        }
        finally {
            finished();
        }
    }

    private void finished() {
        for (OnLoadAllPlayersListener listener: listeners) {
            listener.loadPlayersFinished(this);
        }
    }

    public Collection<Player> getPlayers() {
        return this.players;
    }
}
