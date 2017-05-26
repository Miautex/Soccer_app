package pkgDatabase;

import java.util.ArrayList;

import pkgData.Game;
import pkgData.Participation;
import pkgDatabase.pkgListener.OnGameInsertedListener;
import pkgDatabase.pkgListener.OnParticipationInsertedListener;
import pkgException.CouldNotInsertGameException;
import pkgMisc.GsonSerializor;
import pkgResult.SingleGameResult;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class InsertGameHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener, OnParticipationInsertedListener {

    private ArrayList<OnGameInsertedListener> listeners;
    private Game local_game = null,
            remote_game = null;
    private int partsToInsert = 0;

    public InsertGameHandler(ArrayList<OnGameInsertedListener> listeners, Game game) {
        this.listeners = listeners;
        this.local_game = game;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            SingleGameResult gs = GsonSerializor.deserializeSingleGameResult(response.getJson());

            if (gs.isSuccess()) {
                remote_game = gs.getContent();
                insertParticipations();
            }
            else {
                throw new CouldNotInsertGameException();
            }
        }
        catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    private void insertParticipations() {
        try {
            for (Participation part : local_game.getParticipations()) {
                remote_game.addParticipation(part);
            }

            partsToInsert = remote_game.getParticipations().size();

            for (Participation part : remote_game.getParticipations()) {
                Database.getInstance().insert(part, this);
            }
        }
        catch (Exception ex) {
            setException(ex);
            finished();
        }
    }

    @Override
    public void insertParticipationFinished(InsertParticipationHandler handler) {
        if (handler.getException() == null) {
            partsToInsert--;
            if (partsToInsert == 0) {
                finished();
            }
        }
        else {
            setException(handler.getException());
            finished();
        }
    }

    private void finished() {
        for (OnGameInsertedListener listener: listeners) {
            listener.insertGameFinished(this);
        }
    }

    public Game getGame() {
        return this.remote_game;
    }
}
