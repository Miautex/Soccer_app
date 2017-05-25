package pkgDatabase;

import java.util.ArrayList;

import pkgDatabase.pkgListener.OnLoginListener;
import pkgException.InvalidLoginDataException;
import pkgMisc.GsonSerializor;
import pkgWSA.AccessorResponse;
import pkgWSA.WebRequestTaskListener;

public class LoginHandler extends WebserviceResponseHandler
        implements WebRequestTaskListener {
    private ArrayList<OnLoginListener> listeners;
    private String local_pwEnc,
            username;

    public LoginHandler(String username, String local_pwEnc, ArrayList<OnLoginListener> listeners) {
        this.listeners = listeners;
        this.local_pwEnc = local_pwEnc;
        this.username = username;
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
            else if (response.getResponseCode() == 204) {       //if user doesn't exist
                throw new InvalidLoginDataException();
            }
            else {
                if (!response.getJson().isEmpty()) {
                    String remote_pwEnc = GsonSerializor.deserializePassword(response.getJson());

                    if (!local_pwEnc.equals(remote_pwEnc)) {
                        throw new InvalidLoginDataException();
                    }
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
        for (OnLoginListener listener: listeners) {
            listener.loginFinished(this);
        }
    }

    public String getUsername() {
        return this.username;
    }

}
