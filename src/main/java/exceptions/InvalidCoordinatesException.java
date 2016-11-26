package exceptions;

public class InvalidCoordinatesException extends RuntimeException {
    public InvalidCoordinatesException(String msg) {
        super(msg);
    }
}
