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

public abstract class Automaton1Dim extends Automaton {
    public Automaton1Dim(CellStateFactory stateFactory, CellNeighbourhood neighbourhoodStrategy, int size) {
        super(stateFactory, neighbourhoodStrategy);

        Map<CellCoordinates, CellState> initMap = new HashMap<>();
        for(int i = 0; i < size; i++) {
            Coords1D newCoords = new Coords1D(i);
            initMap.put(newCoords, stateFactory.initialState(newCoords));
        }
        super.insertStructure(initMap);
        this.size = size;
    }

    protected int getSize() {
        return size;
    }

    @Override
    protected boolean hasNextCoordinates(CellCoordinates coords) {
        Coords1D coords1D = (Coords1D) coords;
        if(coords1D.getX() < size - 1)
            return true;
        else
            return false;
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
