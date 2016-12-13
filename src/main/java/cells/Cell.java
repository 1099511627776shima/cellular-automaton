package cells;

import cells.states.CellState;
import cells.coordinates.CellCoordinates;

/**
 * The <code>Cell</code> class represents a single cell in the automaton. It stores cell's coordinates (position) and state.
 */
public class Cell {
    /**
     * Sole constructor of this class creating a cell with given coordinates and state.
     * @param coords coordinates of the created cell
     * @param state state of the created cell
     */
    public Cell(CellCoordinates coords, CellState state) {
        this.coords = coords;
        this.state = state;
    }

    /**
     * Gets the state of the cell
     *
     * @return state of the cell
     */
    public CellState getState() {
        return state;
    }

    /**
     * Gets the coordinates of the cell
     *
     * @return coordinates of the cell
     */
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
