package pkgException;

public class NameTooShortException extends Exception {
    private int minLenght = 0;

    public NameTooShortException(String msg, int maxLenght) {
        super(msg);
        this.minLenght = maxLenght;
    }

    public NameTooShortException(int maxLenght) {
        super();
        this.minLenght = maxLenght;
    }

    public NameTooShortException() {
        super();
    }

    public final int getMinLenght() {
        return minLenght;
    }
}
