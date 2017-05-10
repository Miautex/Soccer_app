package pkgException;

public class InvalidLoginDataException extends Exception {
    public InvalidLoginDataException(String msg) {
        super(msg);
    }

    public InvalidLoginDataException() {
        super();
    }
}
