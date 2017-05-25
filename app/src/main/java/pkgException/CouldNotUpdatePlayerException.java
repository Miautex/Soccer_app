package pkgException;

public class CouldNotUpdatePlayerException extends Exception {
    public CouldNotUpdatePlayerException(String msg) {
        super(msg);
    }

    public CouldNotUpdatePlayerException() {
        super();
    }
}
