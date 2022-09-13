package logging.exceptions;

public class ProductNotInDatabaseException extends IllegalArgumentException {

    public ProductNotInDatabaseException(String message) {
        super(message);
    }
}
