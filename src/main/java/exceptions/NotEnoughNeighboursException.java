package exceptions;

/**
 * This exception should be thrown whenever a method received insufficient amount of neighbours to determine the next state of the cell.
 */
public class NotEnoughNeighboursException extends RuntimeException {
    public NotEnoughNeighboursException(String msg) {
        super(msg);
    }
}
