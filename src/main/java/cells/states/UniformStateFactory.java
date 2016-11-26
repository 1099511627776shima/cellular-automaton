package cells.states;

import cells.coordinates.CellCoordinates;

public class UniformStateFactory implements CellStateFactory {
    public UniformStateFactory(CellState state) {
        this.state = state;
    }
    @Override
    public CellState initialState(CellCoordinates cell) {
        return state;
    }

    private CellState state;
}
