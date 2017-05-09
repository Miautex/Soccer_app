package pkgException;

public class CouldNotDeletePlayerException extends Exception {

    public CouldNotDeletePlayerException(String msg) {
        super(msg);
    }

    public CouldNotDeletePlayerException() {
        super();
    }
}

