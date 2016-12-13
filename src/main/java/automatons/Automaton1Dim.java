package automatons;

import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords1D;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.CellState;
import cells.states.CellStateFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is a base abstract class for all types of one-dimensional cellular automatons. It implements the methods that
 * are able to determine some parameters of the automaton like its size, find coordinates of the next cell and check if
 * such cell exists in the particular automaton.
 */
public abstract class Automaton1Dim extends Automaton {
    /**
     * Sole constructor of this class. Used by subclasses when creating a particular type of one-dimensional automaton.
     *
     * @param stateFactory          specifies which state factory should be used for initialization of cells
     * @param neighbourhoodStrategy specifies the rules for finding neighbours of a given cell
     * @param size                  specifies the number of cells in the automaton. One-dimensional automatons are
     *                              commonly displayed as rows of cells so the size is the number of cells in a row.
     */
    public Automaton1Dim(CellStateFactory stateFactory, CellNeighbourhood neighbourhoodStrategy, int size) {
        super(stateFactory, neighbourhoodStrategy);

        Map<CellCoordinates, CellState> initMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Coords1D newCoords = new Coords1D(i);
            initMap.put(newCoords, stateFactory.initialState(newCoords));
        }
        super.insertStructure(initMap);
        this.size = size;
    }

    /**
     * Gets the size of the one-dimensional automaton, which is the number of cells it consists of.
     *
     * @return number of cells in the automaton
     */
    protected int getSize() {
        return size;
    }

    @Override
    protected boolean hasNextCoordinates(CellCoordinates coords) {
        Coords1D coords1D = (Coords1D) coords;
        if (coords1D.getX() < size - 1) return true;
        else return false;
    }

    @Override
    protected CellCoordinates nextCoordinates(CellCoordinates coords) {
        Coords1D coords1D = (Coords1D) coords;
        return new Coords1D(coords1D.getX() + 1);
    }

    @Override
    protected CellCoordinates initialCoordinates() {
        return new Coords1D(-1);
    }

    private int size;
}
