package logging;

public class ProductException extends Exception {

    public ProductException(String errMessage, Throwable err) {
        super(errMessage, err);
    }
}
