package pkgDatabase;

import pkgException.UnauthorizedWebserviceAccessException;
import pkgWSA.AccessorResponse;

/**
 * @author Elias Santner
 */

public abstract class WebserviceResponseHandler {
    private Exception exception = null;

    public Exception getException() {
        return this.exception;
    }

    protected void setException(Exception ex) {
        this.exception = ex;
    }

    protected void handleResponse(AccessorResponse response) throws Exception {
        if (response.getException() != null) {
            throw response.getException();
        }
        else if (response.getResponseCode() == 500) {
            throw new Exception(response.getJson());
        }
        else if (response.getResponseCode() == 401) {       //unauthorized
            throw new UnauthorizedWebserviceAccessException();
        }
    }
}
