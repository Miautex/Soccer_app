package pkgDatabase;

public abstract class WebserviceResponseHandler {
    private Exception exception = null;

    public Exception getException() {
        return this.exception;
    }

    protected void setException(Exception ex) {
        this.exception = ex;
    }
}
