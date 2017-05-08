package pkgException;

public class DuplicateUsernameException extends Exception {

    public DuplicateUsernameException(String msg) {
        super(msg);
    }
}
