package cells.neighbourhood;

import automatons.ElementaryAutomaton;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords1D;
import exceptions.InvalidCoordinatesException;

import java.util.*;

public class ElementaryNeighbourhood implements CellNeighbourhood {
    public ElementaryNeighbourhood(int boardSize, boolean wrappingEnabled) {
        this.boardSize = boardSize;
        this.wrappingEnabled = wrappingEnabled;
    }

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
