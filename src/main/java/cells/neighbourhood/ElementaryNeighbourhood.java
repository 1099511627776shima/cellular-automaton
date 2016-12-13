package cells.neighbourhood;

import automatons.ElementaryAutomaton;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords1D;
import exceptions.InvalidCoordinatesException;

import java.util.*;

/**
 * This is a cell neighbourhood used in Elementary Automaton.
 */
public class ElementaryNeighbourhood implements CellNeighbourhood {
    /**
     * Sole constructor of this class creating neighbourhood for an automaton of a given size and wrapping mode.
     * @param boardSize size of the one-dimensional automaton
     * @param wrappingEnabled if <code>true</code> cell on the edge of the board is considered as a neighbour of the first cell in a row and vice versa. Otherwise cell on the edge has only one real neighbour and the missing one is assumed to be dead.
     */
    public ElementaryNeighbourhood(int boardSize, boolean wrappingEnabled) {
        this.boardSize = boardSize;
        this.wrappingEnabled = wrappingEnabled;
    }

    /**
     * Returns two neighbours of the cell.
     *
     * @param cell cell for which we search neighbours
     * @return set of two neighbours of the cell - if on the edge depending on the wrapping mode the "missing" neighbour is assumed dead or taken from the other end of the row.
     * @throws InvalidCoordinatesException thrown if given cell coordinate is not accessible - either negative or bigger then the automaton size
     */
    @Override
    public Set<CellCoordinates> cellNeighbours(CellCoordinates cell) {
        int cellXCoord = ((Coords1D) cell).getX();

        if(cellXCoord < 0 || cellXCoord >= boardSize) throw new InvalidCoordinatesException("Cell does not exist");

        Set<CellCoordinates> neighbours = new LinkedHashSet<>();
        if(wrappingEnabled) {
            neighbours.add(new Coords1D(Math.floorMod(cellXCoord - 1, boardSize)));
            neighbours.add(new Coords1D(Math.floorMod(cellXCoord + 1, boardSize)));
        }
        else {
            if(cellXCoord - 1 >= 0) neighbours.add(new Coords1D(cellXCoord - 1));
            if(cellXCoord + 1 < boardSize) neighbours.add(new Coords1D(cellXCoord + 1));
        }

        // When wrapping is not enabled: can return 0 (if width == 1) or 1 (if on the edge of board) or 2 neighbours
        return neighbours;
    }

    private int boardSize;
    private boolean wrappingEnabled;
}
