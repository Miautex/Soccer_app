package pkgException;


public class CouldNotLoadParticipationsException extends Exception {

    public CouldNotLoadParticipationsException(String msg) {
        super(msg);
    }

    public CouldNotLoadParticipationsException() {
        super();
    }
}
