package cells.states;

import cells.coordinates.CellCoordinates;

import java.util.Map;

/**
 * This class is used to initialize automaton's cells with a given structure and uniformly set other cell's states.
 */
public class GeneralStateFactory implements CellStateFactory {
    /**
     * Sole constructor of this class used to set the structure to be used in automaton initialization and <code>UniformStateFactory</code> to initialize all remaining cells.
     * @param structure structure to be used for initialization - inserted in the automaton
     * @param defaultStateFactory defines the state witch which other cells should be initialized
     * @see cells.states.UniformStateFactory
     */
    public GeneralStateFactory(Map<CellCoordinates, CellState> structure, UniformStateFactory defaultStateFactory) {
        states = structure;
        this.defaultStateFactory = defaultStateFactory;
    }

    /**
     * Provides information with which state a cell should be initialized.
     *
     * @param coords cell coordinates of the initialized cell
     * @return if cell coordinates are listed in the structure defined in a constructor it will be initialized according to this structure. Otherwise it will be initialized with a state provided by the <code>UniformStateFactory</code>
     */
    @Override
    public CellState initialState(CellCoordinates coords) {
        if(states.containsKey(coords))
            return states.get(coords);
        else
            return defaultStateFactory.initialState(coords);
    }

    private Map<CellCoordinates, CellState> states;
    private UniformStateFactory defaultStateFactory;
}
