package gui;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class representing a structure that can be inserted in the automaton using insert tool. It hold basic information for the structure needed to be stored to display it properly.
 */
public class Structure {
    public Structure(String structureName, String structureDescription, Map<Coords2D, CellState> structureMap, int width, int height) {
        this.structureName = structureName;
        this.structureDescription = structureDescription;
        this.structureMap = structureMap;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns map representing stored structure, that can be inserted in automaton - structure with repositioned cell coordinates according to where the user clicked.
     * @param pivotX X coordinate of the cell clicked by user
     * @param pivotY Y coordinate of the cell clicked by user
     * @return Map with structure with cells appropriately placed around the clicked cell
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

    /**
     * Provides the name of the stored structure.
     *
     * @return name of the stored structure
     */
    public String getName() {
        return structureName;
    }

    /**
     * Gets the width of the structure. Structures that actual width is an even number have their saved width increased by 1 - it allows to find the horizontal center of the structure.
     *
     * @return width of the structure given in number of cells
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the structure. Structures that actual height is an even number have their saved height increased by 1 - it allows to find the vertical center of the structure.
     *
     * @return height of the structure given in number of cells
     */
    public int getHeight() {
        return height;
    }

    /**
     * Provides detailed information about the stored structure.
     *
     * @return detailed information about characteristic behaviour of the structure
     */
    public String getDescription() {
        return structureDescription;
    }

    private String structureName;
    private String structureDescription;
    private int width;
    private int height;
    // Map containing positions of live cells RELATIVE to the pivotPoint
    private Map<Coords2D, CellState> structureMap;
}
