package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgDatabase.pkgListener.OnPlayerUpdatedListener;
import pkgDatabase.pkgListener.OnSetPlayerPosListener;
import pkgException.CouldNotUpdatePlayerException;
import pkgException.DuplicateUsernameException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class UpdatePlayerHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener, OnSetPlayerPosListener {
    private ArrayList<OnPlayerUpdatedListener> listeners;
    private Player local_player;

    protected UpdatePlayerHandler(ArrayList<OnPlayerUpdatedListener> listeners, Player p) {
        this.listeners = listeners;
        this.local_player = p;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());

            if (r.isSuccess()) {
                setPositions();
            }
            else if (!r.isSuccess() && r.getError() != null && r.getError().getErrorMessage().
                    contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException(local_player.getUsername());
            }
            else {
                throw new CouldNotUpdatePlayerException();
            }
        } catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    private void setPositions() {
        try {
            Database.getInstance().setPlayerPositions(local_player, this);
        }
        catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    @Override
    public void setPosFinished(SetPlayerPositionsHandler handler) {
        setException(handler.getException());
        finished();
    }

    private void finished() {
        for (OnPlayerUpdatedListener listener: listeners) {
            listener.updatePlayerFinished(this);
        }
    }

    public Player getPlayer() {
        return this.local_player;
    }
}