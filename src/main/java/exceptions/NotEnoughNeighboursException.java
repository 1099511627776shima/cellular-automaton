package exceptions;

public class NotEnoughNeighboursException extends RuntimeException {
    public NotEnoughNeighboursException(String msg) {
        super(msg);
    }
}
