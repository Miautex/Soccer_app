package pkgException;

public class CouldNotUpdateGameException extends Exception {
    public CouldNotUpdateGameException(String msg) {
        super(msg);
    }

    public CouldNotUpdateGameException() {
        super();
    }
}
