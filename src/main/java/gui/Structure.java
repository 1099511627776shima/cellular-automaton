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
    public Structure(String structureName, String structureDescription, Map<Coords2D, CellState> structureMap, Coords2D pivotPoint, int structureWidth, int structureHeight) {
        this.structureName = structureName;
        this.structureDescription = structureDescription;
        this.structureMap = structureMap;
        this.pivotPoint = pivotPoint;
        this.structureWidth = structureWidth;
        this.structureHeight = structureHeight;
    }

    /**
     * Returns map with stucture, that can be inserted in automaton (repositioned structure according to where the user clicked)
     * @param pivotX X coordinate of cell clicked by user
     * @param pivotY Y coordinate of cell clicked by user
     * @return Map with structure with coordinates mapped on automaton map around the clicked cell
     */
    public Map<CellCoordinates, CellState> getPositionedStructure(int pivotX, int pivotY) {
        Map<CellCoordinates, CellState> positionedStructure = new HashMap<>();

        for(Coords2D relativeCoords : structureMap.keySet()) {
            positionedStructure.put(
                    new Coords2D(
                            relativeCoords.getX() + pivotX,
                            relativeCoords.getY() + pivotY
                    ),
                    structureMap.get(relativeCoords)
            );
        }

        return positionedStructure;
    }

    public String getName() {
        return structureName;
    }

    public String getDescription() {
        return structureDescription;
    }

    public int getWidth() {
        return structureWidth;
    }

    public int getHeight() {
        return structureHeight;
    }

    /* public String toString() {
        String output = new String();
        output += structureName + "\n";
        for(Coords2D coords : structureMap.keySet())
            output += coords.toString() + " ";
        output += "\n";

        return output;
    } */

    private String structureName;
    private String structureDescription;
    private int structureWidth;
    private int structureHeight;
    /** Map containing positions of live cells RELATIVE to the pivotPoint */
    private Map<Coords2D, CellState> structureMap;
    private Coords2D pivotPoint;
}
