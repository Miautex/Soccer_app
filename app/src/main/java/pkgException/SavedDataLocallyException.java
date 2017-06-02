package pkgException;

/**
 * Created by Elias on 01.06.2017.
 */

public class SavedDataLocallyException extends Exception {
    public SavedDataLocallyException(String msg) {
        super(msg);
    }

    public SavedDataLocallyException() {
        super();
    }
}
