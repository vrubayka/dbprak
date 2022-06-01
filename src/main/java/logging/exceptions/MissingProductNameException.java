package logging.exceptions;

public class MissingProductNameException extends RuntimeException {

    public MissingProductNameException(String message) {
        super(message);
    }
}
