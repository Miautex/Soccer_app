package pkgException;

public class CouldNotDeleteGameException extends Exception {
    public CouldNotDeleteGameException(String msg) {
        super(msg);
    }

    public CouldNotDeleteGameException() {
        super();
    }
}
