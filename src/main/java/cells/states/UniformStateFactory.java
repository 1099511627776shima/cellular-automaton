package cells.states;

import cells.coordinates.CellCoordinates;

/**
 * This class is used to initialize automaton's cells uniformly with a given state.
 */
public class UniformStateFactory implements CellStateFactory {
    /**
     * Sole constructor of this class used to determine with which state all the cells will be initialized.
     * @param state state with which all the cells will be initialized
     */
    public UniformStateFactory(CellState state) {
        this.state = state;
    }

    /**
     * Provides the same (uniform) state for all the cells to be initialized with
     *
     * @param cell cell coordinates of the initialized cell
     * @return same state for every cell defined in the constructor
     */
    @Override
    public CellState initialState(CellCoordinates cell) {
        return state;
    }

    private CellState state;
}
