package exceptions;

/**
 * This exception should be thrown whenever a method receives unexpected cell coordinates, namely non-existent in the automaton.
 */
public class InvalidCoordinatesException extends RuntimeException {
    public InvalidCoordinatesException(String msg) {
        super(msg);
    }
}
