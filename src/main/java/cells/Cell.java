package cells;

import cells.states.CellState;
import cells.coordinates.CellCoordinates;

public class Cell {
    public Cell(CellCoordinates coords, CellState state) {
        this.coords = coords;
        this.state = state;
    }

    public CellState getState() {
        return state;
    }

    public CellCoordinates getCoords() {
        return coords;
    }

    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + coords.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        Cell otherCell = (Cell) other;
        if(this.getCoords().equals(otherCell.getCoords()) && this.getState().equals(otherCell.getState()))
            return true;
        else
            return false;
    }

    private CellState state;
    private CellCoordinates coords;
}
