package cells.neighbourhood;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import exceptions.InvalidCoordinatesException;

import java.util.HashSet;
import java.util.Set;

public class VonNeumannNeighbourhood implements CellNeighbourhood {
    public VonNeumannNeighbourhood(int boardWidth, int boardHeight, int r, boolean wrappingEnabled) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.r = r;
        this.wrappingEnabled = wrappingEnabled;
    }

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
