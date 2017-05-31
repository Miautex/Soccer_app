package pkgException;

public class NoLocalDataException extends Exception {
    public NoLocalDataException(String msg) {
        super(msg);
    }

    public NoLocalDataException() {
        super();
    }
}
