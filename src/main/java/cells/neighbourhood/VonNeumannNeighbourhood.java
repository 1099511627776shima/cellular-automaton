package cells.neighbourhood;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import exceptions.InvalidCoordinatesException;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a cell neighbourhood used in two-dimensional automatons. Cell neighbours can be found in a diamond-like shape around a particular cell.
 */
public class VonNeumannNeighbourhood implements CellNeighbourhood {
    /**
     * Sole constructor of this class creating neighbourhood for an automaton of the given dimensions and wrapping mode with a specified radius.
     * @param boardWidth width in cells of the automaton
     * @param boardHeight height in cells of the automaton
     * @param r radius of the neighbourhood. It can be described of the range from the particular cell within which they are considered as neighbours of that cell.
     * @param wrappingEnabled if set to <code>true</code> board can be considered as wrapped vertically and horizontally. Getting to the one edge of the automaton means getting back to the opposite edge. Thus from any cell it is possible to move and search for neighbours in any direction without reaching any limits.
     */
    public VonNeumannNeighbourhood(int boardWidth, int boardHeight, int r, boolean wrappingEnabled) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.r = r;
        this.wrappingEnabled = wrappingEnabled;
    }

    /**
     * Returns the set of neighbours gathered from a diamond-like area around the cell.
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
            for(int i = 0; i <= r; i++) {
                for(int x = cellXCoord - r + i; x <= cellXCoord + r - i; x++) {
                    neighbours.add(new Coords2D(Math.floorMod(x, boardWidth), Math.floorMod(cellYCoord + i, boardHeight)));
                    neighbours.add(new Coords2D(Math.floorMod(x, boardWidth), Math.floorMod(cellYCoord - i, boardHeight)));
                }
            }
        }
        else {
            for(int i = 0; i <= r; i++) {
                for(int x = cellXCoord - r + i; x <= cellXCoord + r - i; x++) {
                    if(x >= 0 && x < boardWidth) {
                        if((cellYCoord + i) < boardHeight) neighbours.add(new Coords2D(x, cellYCoord + i));
                        if((cellYCoord - i) >= 0) neighbours.add(new Coords2D(x, cellYCoord - i));
                    }
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
