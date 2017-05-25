package pkgException;

public class CouldNotInsertParticipationException extends Exception {
    public CouldNotInsertParticipationException(String msg) {
        super(msg);
    }

    public CouldNotInsertParticipationException() {
        super();
    }
}
