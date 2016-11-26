package cells.neighbourhood;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import exceptions.InvalidCoordinatesException;

import java.util.*;

public class MooreNeighbourhood implements CellNeighbourhood {
    public MooreNeighbourhood(int boardWidth, int boardHeight, int r, boolean wrappingEnabled) {
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
