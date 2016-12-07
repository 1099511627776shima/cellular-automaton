package gui;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BinaryStructures {

    public static Structure getStructure(String name) {
        return structures.get(name);
    }

    private static Map<String, Structure> initializeStructures() {
        Map<String, Structure> initializedStructures = new HashMap<>();

        /* Hardcoded BinaryStructures */

        // Glider
        Coords2D gliderPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> gliderMap = generateMap("0,-1;1,0;1,1;0,1;-1,1");
        Structure glider = new Structure(
                "glider",
                "Moving structure",
                gliderMap,
                gliderPivot,
                3,
                3
        );
        initializedStructures.put("glider", glider);

        // Block
        Coords2D blockPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> blockMap = generateMap("0,0;1,0;0,1;1,1");
        Structure block = new Structure(
                "block",
                "Still life",
                blockMap,
                blockPivot,
                2,
                2
        );
        initializedStructures.put("block", block);

        return initializedStructures;
    }

    private static Map<String, Structure> structures = initializeStructures();

    private static Map<Coords2D, CellState> generateMap(String pattern) {
        Map<Coords2D, CellState> resultMap = new HashMap<>();

        Scanner patternScanner = new Scanner(pattern);
        patternScanner.useDelimiter(";");
        while(patternScanner.hasNext()) {
            Scanner coordsScanner = new Scanner(patternScanner.next());
            coordsScanner.useDelimiter(",");
            resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), BinaryState.ALIVE);
        }

        return resultMap;
    }

    public static void main(String[] args) {
        System.out.println(BinaryStructures.getStructure("glider"));
    }
}
