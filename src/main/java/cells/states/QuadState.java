package cells.states;

/**
 * All possible Quad Life states. A cell can have one of the four live states (red, green, yellow or blue) or be dead.
 */
public enum QuadState implements CellState {
    DEAD, RED, YELLOW, BLUE, GREEN;
}
