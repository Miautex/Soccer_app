package pkgException;

public class NameTooLongException extends Exception {
    private int maxLenght = 0;

    public NameTooLongException(String msg, int maxLenght) {
        super(msg);
        this.maxLenght = maxLenght;
    }

    public NameTooLongException(int maxLenght) {
        super();
        this.maxLenght = maxLenght;
    }

    public NameTooLongException() {
        super();
    }

    public final int getMaxLenght() {
        return maxLenght;
    }
}
