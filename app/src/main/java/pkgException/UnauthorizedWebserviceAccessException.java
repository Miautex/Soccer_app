package pkgException;

public class UnauthorizedWebserviceAccessException extends Exception {
    public UnauthorizedWebserviceAccessException(String msg) {
        super(msg);
    }

    public UnauthorizedWebserviceAccessException() {
        super();
    }
}
