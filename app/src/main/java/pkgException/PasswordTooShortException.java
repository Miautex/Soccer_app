package pkgException;

public class PasswordTooShortException extends Exception {
    private int minLenght = 0;

    public PasswordTooShortException(String msg, int maxLenght) {
        super(msg);
        this.minLenght = maxLenght;
    }

    public PasswordTooShortException(int maxLenght) {
        super();
        this.minLenght = maxLenght;
    }

    public PasswordTooShortException() {
        super();
    }

    public final int getMinLenght() {
        return minLenght;
    }
}
