package pkgDatabase.pkgHandler;

import java.util.ArrayList;

import pkgData.Game;
import pkgData.Participation;
import pkgDatabase.Database;
import pkgDatabase.pkgListener.OnGameUpdatedListener;
import pkgDatabase.pkgListener.OnParticipationUpdatedListener;
import pkgException.CouldNotUpdateGameException;
import pkgMisc.GsonSerializor;
import pkgResult.Result;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

/**
 * @author Elias Santner
 */

public class UpdateGameHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener, OnParticipationUpdatedListener {
    private ArrayList<OnGameUpdatedListener> listeners;
    private Game local_game;
    private int partsToUpdate = 0;

    public UpdateGameHandler(ArrayList<OnGameUpdatedListener> listeners, Game g) {
        this.listeners = listeners;
        this.local_game = g;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            Result r = GsonSerializor.deserializeResult(response.getJson());
            if (r.isSuccess()) {
                updateParticipations();
            }
            else {
                throw new CouldNotUpdateGameException();
            }
        } catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    private void updateParticipations() {
        try {
            partsToUpdate = local_game.getParticipations().size();

            for (Participation part : local_game.getParticipations()) {
                Database.getInstance().update(part, this);
            }
        }
        catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    @Override
    public void updateParticipationFinished(UpdateParticipationHandler handler) {
        if (handler.getException() == null) {
            partsToUpdate--;
            if (partsToUpdate == 0) {
                finished();
            }
        }
        else {
            setException(handler.getException());
            finished();
        }
    }

    private void finished() {
        for (OnGameUpdatedListener listener: listeners) {
            listener.updateGameFinished(this);
        }
    }

    public Game getGame() {
        return this.local_game;
    }
}
