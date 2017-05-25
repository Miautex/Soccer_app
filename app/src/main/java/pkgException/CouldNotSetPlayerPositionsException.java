package pkgException;

public class CouldNotSetPlayerPositionsException extends Exception {

    public CouldNotSetPlayerPositionsException(String msg) {
        super(msg);
    }

    public CouldNotSetPlayerPositionsException() {
        super();
    }
}
