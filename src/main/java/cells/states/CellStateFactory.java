package cells.states;

import cells.coordinates.CellCoordinates;

/**
 * The <code>CellStateFactory</code> interface should be implemented by any class that is used for automaton initialization.
 */
public interface CellStateFactory {
    /**
     * Provides information with which state a cell should be initialized.
     *
     * @param cell cell coordinates of the initialized cell
     * @return state with which the given cell should be initialized
     */
    CellState initialState(CellCoordinates cell);
}
