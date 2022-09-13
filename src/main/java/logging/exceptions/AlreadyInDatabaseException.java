package logging.exceptions;

public class AlreadyInDatabaseException extends IllegalStateException{

    public AlreadyInDatabaseException(String message) {super(message);}
}
