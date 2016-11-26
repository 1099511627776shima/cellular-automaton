package cells.neighbourhood;

import cells.coordinates.CellCoordinates;

import java.util.Set;

public interface CellNeighbourhood {
    /** @param cell cell for which we search neighbours
     *  @return all possible neighbours of the cell according to neighbourhood strategy
     */
    Set<CellCoordinates> cellNeighbours(CellCoordinates cell);
}
