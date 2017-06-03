package pkgDatabase;

import java.util.ArrayList;

import pkgData.Player;
import pkgData.PlayerPosition;
import pkgDatabase.pkgListener.OnPlayerInsertedListener;
import pkgDatabase.pkgListener.OnSetPlayerPosListener;
import pkgException.CouldNotSetPlayerPositionsException;
import pkgException.DuplicateUsernameException;
import pkgMisc.GsonSerializor;
import pkgResult.SinglePlayerResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class InsertPlayerHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener, OnSetPlayerPosListener {
    private ArrayList<OnPlayerInsertedListener> listeners;
    private Player remote_player,
                    local_player;

    protected InsertPlayerHandler(ArrayList<OnPlayerInsertedListener> listeners, Player p) {
        this.listeners = listeners;
        this.local_player = p;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);
            SinglePlayerResult r = GsonSerializor.deserializeSinglePlayerResult(response.getJson());

            if (r.isSuccess()) {
                remote_player = r.getContent();
                setPositions();
            }
            else if (!r.isSuccess() && r.getError() != null && r.getError().getErrorMessage().
                    contains("MySQLIntegrityConstraintViolationException")) {
                throw new DuplicateUsernameException(local_player.getUsername());
            }
            else {
                throw new CouldNotSetPlayerPositionsException();
            }
        } catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    private void setPositions() {
        try {
            for (PlayerPosition pp: local_player.getPositions()) {
                remote_player.addPosition(pp);
            }
            Database.getInstance().setPlayerPositions(remote_player, this);
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
        for (OnPlayerInsertedListener listener: listeners) {
            listener.insertPlayerFinished(this);
        }
    }

    public Player getPlayer() {
        return this.remote_player == null ? this.local_player : this.remote_player;
    }

    public Player getPlayerLocal() {
        return this.local_player;
    }
}