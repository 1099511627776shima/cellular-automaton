package automatons;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.CellState;
import cells.states.CellStateFactory;

import java.util.*;

public abstract class Automaton2Dim extends Automaton {
    public Automaton2Dim(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy, int width, int height) {
        super(stateFactory, neighbourStrategy);

        Map<CellCoordinates, CellState> initMap = new HashMap<>();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                Coords2D newCoords = new Coords2D(x, y);
                initMap.put(newCoords, stateFactory.initialState(newCoords));
            }
        }
        super.insertStructure(initMap);
        this.width = width;
        this.height = height;
    }

    protected int getWidth() {
        return width;
    }

    protected int getHeight() {
        return height;
    }

    @Override
    protected CellCoordinates initialCoordinates() {
        return new Coords2D(-1, 0);
    }

    @Override
    protected boolean hasNextCoordinates(CellCoordinates coords) {
        Coords2D coords2D = (Coords2D)coords;
        if(coords2D.getX() < width - 1) return true;
        else if(coords2D.getY() < height - 1) return true;
        else return false;
    }

    @Override
    protected CellCoordinates nextCoordinates(CellCoordinates coords) {
        Coords2D coords2D = (Coords2D)coords;
        if(coords2D.getX() < width - 1) return new Coords2D(coords2D.getX() + 1, coords2D.getY());
        else return new Coords2D(0, coords2D.getY() + 1);
    }

    private int width;
    private int height;
}
