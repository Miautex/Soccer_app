package pkgException;

public class CouldNotUpdateParticipationException extends Exception {
    public CouldNotUpdateParticipationException(String msg) {
        super(msg);
    }

    public CouldNotUpdateParticipationException() {
        super();
    }
}
