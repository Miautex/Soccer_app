package pkgDatabase;

import java.util.ArrayList;
import java.util.Collection;

import pkgDatabase.pkgListener.OnLoginListener;
import pkgException.InvalidLoginDataException;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoginHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoginListener> listenersToInform,       //Listeners are notified directly by this handler
                                        listenersToStore;       //Listeners are stored for access by Database
    private String loginKey = null,
            username;

    public LoginHandler(String username, ArrayList<OnLoginListener> listenersToInform,
                        ArrayList<OnLoginListener> listenersToStore) {
        this.listenersToInform = listenersToInform;
        this.listenersToStore = listenersToStore;
        this.username = username;
    }

    @Override
    public void done(AccessorResponse response) {
        try {
            //throws Exception if error happened
            handleResponse(response);

            if (response.getResponseCode() == 204) {       //if user doesn't exist
                throw new InvalidLoginDataException();
            }
            else {
                if (!response.getJson().isEmpty()) {
                    loginKey = response.getJson();
                }
                else {
                    throw new InvalidLoginDataException();
                }
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
        for (OnLoginListener listener: listenersToInform) {
            listener.loginFinished(this);
        }
    }

    public String getUsername() {
        return this.username;
    }

    public String getLoginKey() {
        return this.loginKey;
    }

    public Collection<OnLoginListener> getStoredListeners() { return this.listenersToStore; }
}
