package pkgException;

public class UsernameTooShortException extends Exception {
    private int minLenght = 0;

    public UsernameTooShortException(String msg, int maxLenght) {
        super(msg);
        this.minLenght = maxLenght;
    }

    public UsernameTooShortException(int maxLenght) {
        super();
        this.minLenght = maxLenght;
    }

    public UsernameTooShortException() {
        super();
    }

    public final int getMinLenght() {
        return minLenght;
    }
}
