package pkgException;

/**
 * Created by Elias on 04.06.2017.
 */

public class CannotDeletePlayerOfLocalGameException extends Exception {
    public CannotDeletePlayerOfLocalGameException(String msg) {
        super(msg);
    }

    public CannotDeletePlayerOfLocalGameException() {
        super();
    }
}
