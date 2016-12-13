package automatons;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.CellState;
import cells.states.CellStateFactory;

import java.util.*;

/**
 * This is a base abstract class for all types of two-dimensional automatons. It implements the methods that are able to
 * determine some parameters of the automaton like its dimensions, find coordinates of the next cell and check if such
 * cell exists in the particular automaton.
 */
public abstract class Automaton2Dim extends Automaton {
    /**
     * Sole constructor of this class. Used by subclasses when creating a particular type of two-dimensional automaton.
     *
     * @param stateFactory      specifies which state factory should be used for initialization of cells
     * @param neighbourStrategy specifies the rules for finding neighbours of a given cell
     * @param width             specifies the number of cells in width
     * @param height            specifies the number of cells in height
     */
    public Automaton2Dim(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy, int width, int height) {
        super(stateFactory, neighbourStrategy);

        Map<CellCoordinates, CellState> initMap = new HashMap<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Coords2D newCoords = new Coords2D(x, y);
                initMap.put(newCoords, stateFactory.initialState(newCoords));
            }
        }
        super.insertStructure(initMap);
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the number of cells in width of the automaton.
     *
     * @return number of cells in width of the automaton
     */
    protected int getWidth() {
        return width;
    }

    /**
     * Gets the number of cells in height of the automaton.
     *
     * @return number of cells in height of the automaton
     */
    protected int getHeight() {
        return height;
    }

    @Override
    protected CellCoordinates initialCoordinates() {
        return new Coords2D(-1, 0);
    }

    @Override
    protected boolean hasNextCoordinates(CellCoordinates coords) {
        Coords2D coords2D = (Coords2D) coords;
        if (coords2D.getX() < width - 1) return true;
        else if (coords2D.getY() < height - 1) return true;
        else return false;
    }

    @Override
    protected CellCoordinates nextCoordinates(CellCoordinates coords) {
        Coords2D coords2D = (Coords2D) coords;
        if (coords2D.getX() < width - 1) return new Coords2D(coords2D.getX() + 1, coords2D.getY());
        else return new Coords2D(0, coords2D.getY() + 1);
    }

    private int width;
    private int height;
}
