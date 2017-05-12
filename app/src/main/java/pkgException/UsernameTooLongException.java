package pkgException;


public class UsernameTooLongException extends Exception {
    private int maxLenght = 0;

    public UsernameTooLongException(String msg, int maxLenght) {
        super(msg);
        this.maxLenght = maxLenght;
    }

    public UsernameTooLongException(int maxLenght) {
        super();
        this.maxLenght = maxLenght;
    }

    public final int getMaxLenght() {
        return maxLenght;
    }
}
