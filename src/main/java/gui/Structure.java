package gui;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bzdeco on 06.12.16.
 */
public class Structure {
    public Structure(String structureName, String structureDescription, ArrayList<Coords2D> structureMap, Coords2D pivotPoint) {
        this.structureName = structureName;
        this.structureDescription = structureDescription;
        this.structureMap = structureMap;
        this.pivotPoint = pivotPoint;
    }

    /**
     * Returns map with stucture, that can be inserted in automaton (repositioned structure according to where the user clicked)
     * @param pivotX X coordinate of cell clicked by user
     * @param pivotY Y coordinate of cell clicked by user
     * @return Map with structure with coordinates mapped on automaton map around the clicked cell
     */
    public Map<CellCoordinates, CellState> getPositionedStructure(int pivotX, int pivotY) {
        Map<CellCoordinates, CellState> positionedStructure = new HashMap<>();

        for(Coords2D relativeCoords : structureMap) {
            positionedStructure.put(
                    new Coords2D(
                            relativeCoords.getX() + pivotX,
                            relativeCoords.getY() + pivotY
                    ),
                    BinaryState.ALIVE //FIXME need to determine kind of alive state elsewhere
            );
        }

        return positionedStructure;
    }

    private String structureName;
    private String structureDescription;
    /** Map containing positions of live cells RELATIVE to the pivotPoint */
    private ArrayList<Coords2D> structureMap;
    Coords2D pivotPoint;
}
