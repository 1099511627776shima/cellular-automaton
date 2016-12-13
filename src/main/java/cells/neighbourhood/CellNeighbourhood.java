package cells.neighbourhood;

import cells.coordinates.CellCoordinates;

import java.util.Set;

/**
 * The <code>CellNeighbourhood</code> interface should be implemented by any class that is representing rules for determining cell's neighbours.
 */
public interface CellNeighbourhood {
    /**
     * Returns the set of all neighbours of the cell of given coordinates.
     *
     * @param cell cell for which we search neighbours
     * @return set of all possible neighbours of the cell according to the type of the neighbourhood
     */
    Set<CellCoordinates> cellNeighbours(CellCoordinates cell);
}
