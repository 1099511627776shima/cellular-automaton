package cells.states;

import cells.coordinates.CellCoordinates;

public interface CellStateFactory {
    CellState initialState(CellCoordinates cell);
}
