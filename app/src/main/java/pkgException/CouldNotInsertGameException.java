package pkgException;

public class CouldNotInsertGameException extends Exception {

    public CouldNotInsertGameException(String msg) {
        super(msg);
    }

    public CouldNotInsertGameException() {
        super();
    }
}
