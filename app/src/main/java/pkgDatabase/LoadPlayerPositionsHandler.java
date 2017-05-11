package pkgDatabase;

import java.util.ArrayList;

import pkgData.GsonSerializor;
import pkgData.PlayerPosition;
import pkgDatabase.pkgListener.OnLoadPlayerPositionsListener;
import pkgResult.PositionResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoadPlayerPositionsHandler implements WebRequestTaskListener {
    private ArrayList<OnLoadPlayerPositionsListener> listeners;
    private int playerID;

    protected LoadPlayerPositionsHandler(int playerID, ArrayList<OnLoadPlayerPositionsListener> listeners) {
        this.listeners = listeners;
        this.playerID = playerID;
    }

    @Override
    public void done(AccessorResponse response) {
        ArrayList<PlayerPosition> positions = null;

        try {
            if (response.getException() != null) {
                throw response.getException();
            }
            else if (response.getResponseCode() == 500) {
                throw new Exception(response.getJson());
            }
            else {
                PositionResult ps = GsonSerializor.deserializePositionResult(response.getJson());
                positions = ps.getContent();
                success(positions);
            }
        }
        catch (Exception ex) {
            failed(ex);
        }
    }

    private void success(ArrayList<PlayerPosition> positions) {
        for (OnLoadPlayerPositionsListener listener: listeners) {
            listener.loadPlayerPositionsSuccessful(playerID, positions);
        }
    }

    private void failed(Exception ex) {
        for (OnLoadPlayerPositionsListener listener: listeners) {
            listener.loadPlayerPositionsFailed(ex);
        }
    }
}
