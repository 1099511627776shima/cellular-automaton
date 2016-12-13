package cells.neighbourhood;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import exceptions.InvalidCoordinatesException;

import java.util.*;

/**
 * This is a cell neighbourhood used in two-dimensional automatons. Cell neighbours can be found in a square around a particular cell.
 */
public class MooreNeighbourhood implements CellNeighbourhood {
    /**
     * Sole constructor of this class creating neighbourhood for an automaton of the given dimensions and wrapping mode with a specified radius.
     * @param boardWidth width in cells of the automaton
     * @param boardHeight height in cell of the automaton for which the neighbourhood is created
     * @param r radius of the neighbourhood. It can be described of the range from the particular cell within which they are considered as neighbours of that cell.
     * @param wrappingEnabled if set to <code>true</code> board can be considered as wrapped vertically and horizontally. Getting to the one edge of the automaton means getting back to the opposite edge. Thus from any cell it is possible to move and search for neighbours in any direction without reaching any limits.
     */
    public MooreNeighbourhood(int boardWidth, int boardHeight, int r, boolean wrappingEnabled) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.r = r;
        this.wrappingEnabled = wrappingEnabled;
    }

    /**
     * Returns the set of neighbours gathered from a square area around the cell.
     *
     * @param cell cell for which we search neighbours
     * @return set of neighbours of the cell - depending on the radius and wrapping mode their number may vary distinctively
     */
    @Override
    public Set<CellCoordinates> cellNeighbours(CellCoordinates cell) {
        int cellXCoord = ((Coords2D)cell).getX();
        int cellYCoord = ((Coords2D)cell).getY();

        if(cellXCoord < 0 || cellXCoord >= boardWidth || cellYCoord < 0 || cellYCoord >= boardHeight) throw new InvalidCoordinatesException("Cell does not exist");

        Set<CellCoordinates> neighbours = new HashSet<>();
        if(wrappingEnabled) {
            for(int y = cellYCoord - r; y <= cellYCoord + r; y++) {
                for(int x = cellXCoord - r; x <= cellXCoord + r; x++) {
                    neighbours.add(new Coords2D(Math.floorMod(x, boardWidth), Math.floorMod(y, boardHeight)));
                }
            }
        }
        else {
            for(int y = cellYCoord - r; y <= cellYCoord + r; y++) {
                for (int x = cellXCoord - r; x <= cellXCoord + r; x++) {
                    if(x >= 0 && x < boardWidth && y >= 0 && y < boardHeight) neighbours.add(new Coords2D(x,y));
                }
            }
        }

        // Remove itself (cell which neighbours we are searching for)
        neighbours.remove(new Coords2D(cellXCoord, cellYCoord));
        return neighbours;
    }

    private int boardWidth;
    private int boardHeight;
    private int r;
    private boolean wrappingEnabled;
}
