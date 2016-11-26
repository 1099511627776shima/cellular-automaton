package gui;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;

import java.util.HashMap;
import java.util.Map;

public class Structure {
    Structure() {
        glider = new HashMap<>();
        glider.put(new Coords2D(1,0), BinaryState.ALIVE);
        glider.put(new Coords2D(2,1), BinaryState.ALIVE);
        glider.put(new Coords2D(0,2), BinaryState.ALIVE);
        glider.put(new Coords2D(1,2), BinaryState.ALIVE);
        glider.put(new Coords2D(2,2), BinaryState.ALIVE);
    }
    public final Map<CellCoordinates, CellState> glider;
}
