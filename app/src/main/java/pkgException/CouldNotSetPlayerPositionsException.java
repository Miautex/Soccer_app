package pkgException;

/**
 * Created by Elias on 09.05.2017.
 */

public class CouldNotSetPlayerPositionsException extends Exception {

    public CouldNotSetPlayerPositionsException(String msg) {
        super(msg);
    }

    public CouldNotSetPlayerPositionsException() {
        super();
    }
}
