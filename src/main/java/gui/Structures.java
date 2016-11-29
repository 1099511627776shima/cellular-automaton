package gui;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;

import java.util.HashMap;
import java.util.Map;

public class Structures {

    public static final Map<CellCoordinates, CellState> GLIDER = createGlider();

    private static Map<CellCoordinates, CellState> createGlider() {
        Map<CellCoordinates, CellState> glider = new HashMap<>();
        glider.put(new Coords2D(1,0), BinaryState.ALIVE);
        glider.put(new Coords2D(2,1), BinaryState.ALIVE);
        glider.put(new Coords2D(0,2), BinaryState.ALIVE);
        glider.put(new Coords2D(1,2), BinaryState.ALIVE);
        glider.put(new Coords2D(2,2), BinaryState.ALIVE);

        return glider;
    }
}
