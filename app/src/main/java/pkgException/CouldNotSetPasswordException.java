package pkgException;

public class CouldNotSetPasswordException extends Exception {
    public CouldNotSetPasswordException(String msg) {
        super(msg);
    }

    public CouldNotSetPasswordException() {
        super();
    }
}
