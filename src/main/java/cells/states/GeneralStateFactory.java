package cells.states;

import cells.coordinates.CellCoordinates;

import java.util.HashMap;
import java.util.Map;

public class GeneralStateFactory implements CellStateFactory {
    public GeneralStateFactory(Map<CellCoordinates, CellState> structure, UniformStateFactory defaultStateFactory) {
        states = structure;
        this.defaultStateFactory = defaultStateFactory;
    }
    @Override
    public CellState initialState(CellCoordinates coords) {
        if(states.containsKey(coords))
            return states.get(coords);
        else
            return defaultStateFactory.initialState(coords);
    }

    private Map<CellCoordinates, CellState> states;
    /** When cell state is not specified in states map, GeneralStateFactory delegates its state initialization to UniformStateFactory */
    private UniformStateFactory defaultStateFactory;
}
