package gui;

import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.QuadState;

import java.util.*;

public class Structures {

    public static Structure getStructure(String name, boolean quadLifeEnabled) {
        if(quadLifeEnabled) {
            return quadStructures.get(name);
        }
        else
            return binaryStructures.get(name);
    }

    /** For quad life colors are distributed randomly */
    private static Map<String, Structure> initializeStructures(boolean quad) {
        Map<String, Structure> initializedStructures = new HashMap<>();

        /* Hardcoded Structures */

        // Glider
        Coords2D gliderPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> gliderMap = generateMap("0,-1;1,0;1,1;0,1;-1,1", quad);
        Structure glider = new Structure(
                "glider",
                "Moving structure",
                gliderMap,
                gliderPivot,
                3,
                3
        );
        initializedStructures.put("glider", glider);

        // Gosper Glider Gun
        Coords2D gosperGliderGunPivot = new Coords2D(18, 4);
        Map<Coords2D, CellState> gosperGliderGunMap = generateMap("-1,1;-2,1;-4,1;-8,1;-17,1;-18,1;-2,0;-8,0;-17,0;-18,0;2,0;3,0;4,1;6,1;2,-1;3,-1;16,-1;17,-1;16,-2;17,-2;2,-2;3,-2;-3,-1;-7,-1;-6,-2;-5,-2;4,-3;6,-3;6,-4;6,2;-2,2;-3,3;-5,4;-6,4;-7,3;-8,2", quad);
        Structure gosperGliderGun = new Structure(
                "Gosper Glider Gun",
                "Producing structure",
                gosperGliderGunMap,
                gosperGliderGunPivot,
                37,
                9
        );
        initializedStructures.put("gosper glider gun", gosperGliderGun);

        // Lightweight spaceship
        Coords2D lightweightSpaceshipPivot = new Coords2D(2, 2);
        Map<Coords2D, CellState> lightweightSpaceshipMap = generateMap("1,-1;-2,-1;2,0;2,1;-2,1;-1,2;0,2;1,2;2,2", quad);
        Structure lightweightSpaceship = new Structure(
                "Lightweight spaceship",
                "Moving structure",
                lightweightSpaceshipMap,
                lightweightSpaceshipPivot,
                5,
                5
        );
        initializedStructures.put("lightweight spaceship", lightweightSpaceship);

        // Beacon
        Coords2D beaconPivot = new Coords2D(2, 2);
        Map<Coords2D, CellState> beaconMap = generateMap("-1,-1;0,-1;-1,0;0,0;1,1;2,1;1,2;2,2", quad);
        Structure beacon = new Structure(
                "Beacon",
                "Oscillator",
                beaconMap,
                beaconPivot,
                4,
                4
        );
        initializedStructures.put("beacon", beacon);

        // Blinker
        Coords2D blinkerPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> blinkerMap = generateMap("0,-1;0,0;0,1", quad);
        Structure blinker = new Structure(
                "Blinker",
                "Oscillator",
                blinkerMap,
                blinkerPivot,
                3,
                3
        );
        initializedStructures.put("blinker", blinker);

        // Toad
        Coords2D toadPivot = new Coords2D(2, 1);
        Map<Coords2D, CellState> toadMap = generateMap("0,0;1,0;2,0;-1,1;0,1;1,1", quad);
        Structure toad = new Structure(
                "Toad",
                "Oscillator",
                toadMap,
                toadPivot,
                4,
                2
        );
        initializedStructures.put("toad", toad);

        // Pulsar
        Coords2D pulsarPivot = new Coords2D(6, 6);
        Map<Coords2D, CellState> pulsarMap = generateMap("2,1;3,1;4,1;2,6;3,6;4,6;1,2;1,3;1,4;6,2;6,3;6,4;-2,1;-3,1;-4,1;-2,6;-3,6;-4,6;-1,2;-1,3;-1,4;-6,2;-6,3;-6,4;2,-1;3,-1;4,-1;2,-6;3,-6;4,-6;1,-2;1,-3;1,-4;6,-2;6,-3;6,-4;-2,-1;-3,-1;-4,-1;-2,-6;-3,-6;-4,-6;-1,-2;-1,-3;-1,-4;-6,-2;-6,-3;-6,-4", quad);
        Structure pulsar = new Structure(
                "Pulsar",
                "Oscillator",
                pulsarMap,
                pulsarPivot,
                13,
                13
        );
        initializedStructures.put("pulsar", pulsar);

        // Block
        Coords2D blockPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> blockMap = generateMap("0,0;1,0;0,1;1,1", quad);
        Structure block = new Structure(
                "block",
                "Still life",
                blockMap,
                blockPivot,
                2,
                2
        );
        initializedStructures.put("block", block);

        // Boat
        Coords2D boatPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> boatMap = generateMap("-1,0;-1,-1;0,-1;1,0;0,1", quad);
        Structure boat = new Structure(
                "boat",
                "Still life",
                boatMap,
                boatPivot,
                3,
                3
        );
        initializedStructures.put("boat", boat);

        // Beehive
        Coords2D beehivePivot = new Coords2D(2, 1);
        Map<Coords2D, CellState> beehiveMap = generateMap("-1,0;0,-1;1,-1;2,0;1,1;0,1", quad);
        Structure beehive = new Structure(
                "beehive",
                "Still life",
                beehiveMap,
                beehivePivot,
                4,
                3
        );
        initializedStructures.put("beehive", beehive);

        // Loaf
        Coords2D loafPivot = new Coords2D(2, 2);
        Map<Coords2D, CellState> loafMap = generateMap("0,-1;1,-1;-1,0;2,0;0,1;2,1;1,2", quad);
        Structure loaf = new Structure(
                "loaf",
                "Still life",
                loafMap,
                loafPivot,
                4,
                4
        );
        initializedStructures.put("loaf", loaf);

        // Pond
        Coords2D pondPivot = new Coords2D(2, 2);
        Map<Coords2D, CellState> pondMap = generateMap("0,-1;1,-1;-1,0;-1,1;2,0;2,1;0,2;1,2", quad);
        Structure pond = new Structure(
                "pond",
                "Still life",
                pondMap,
                pondPivot,
                4,
                4
        );
        initializedStructures.put("pond", pond);

        // Tub
        Coords2D tubPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> tubMap = generateMap("0,-1;0,1;1,0;-1,0", quad);
        Structure tub = new Structure(
                "tub",
                "Still life",
                tubMap,
                tubPivot,
                3,
                3
        );
        initializedStructures.put("tub", tub);

        // Small exploder
        Coords2D smallExploderPivot = new Coords2D(1, 2);
        Map<Coords2D, CellState> smallExploderMap = generateMap("0,-1;-1,0;0,0;1,0;-1,1;1,1;0,2", quad);
        Structure smallExploder = new Structure(
                "small exploder",
                "Producing structure transforming into still life",
                smallExploderMap,
                smallExploderPivot,
                3,
                4
        );
        initializedStructures.put("small exploder", smallExploder);

        // Exploder
        Coords2D exploderPivot = new Coords2D(2, 2);
        Map<Coords2D, CellState> exploderMap = generateMap("0,-2;0,2;-2,-2;-2,-1;-2,0;-2,1;-2,2;2,-2;2,-1;2,0;2,1;2,2", quad);
        Structure exploder = new Structure(
                "exploder",
                "Producing structure transforming into pulsar",
                exploderMap,
                exploderPivot,
                5,
                5
        );
        initializedStructures.put("exploder", exploder);

        // Crocodile
        Coords2D crocodilePivot = new Coords2D(5, 0);
        Map<Coords2D, CellState> crocodileMap = generateMap("-5,0;-4,0;-3,0;-2,0;-1,0;0,0;1,0;2,0;3,0;4,0", quad);
        Structure crocodile = new Structure(
                "crocodile",
                "Oscillator",
                crocodileMap,
                crocodilePivot,
                11,
                1
        );
        initializedStructures.put("crocodile", crocodile);

        // Tumbler/Fountain
        Coords2D tumblerPivot = new Coords2D(3, 3);
        Map<Coords2D, CellState> tumblerMap = generateMap("-2,-2;-1,-2;-2,-1;-1,-1;-1,0;-1,1;-1,2;-3,1;-3,2;-3,3;-2,3;2,-2;1,-2;2,-1;1,-1;1,0;1,1;1,2;3,1;3,2;3,3;2,3", quad);
        Structure tumbler = new Structure(
                "tumbler/fountain",
                "Oscillator",
                tumblerMap,
                tumblerPivot,
                7,
                6
        );
        initializedStructures.put("tumbler/fountain", tumbler);

        // The R-pentonimo
        Coords2D rpentonimoPivot = new Coords2D(1, 1);
        Map<Coords2D, CellState> rpentonimoMap = generateMap("0,-1;1,-1;-1,0;0,0;0,1", quad);
        Structure rpentonimo = new Structure(
                "the r-pentonimo",
                "",
                rpentonimoMap,
                rpentonimoPivot,
                3,
                3
        );
        initializedStructures.put("the r-pentonimo", rpentonimo);

        // Diehard
        Coords2D diehardPivot = new Coords2D(4, 1);
        Map<Coords2D, CellState> diehardMap = generateMap("2,-1;1,1;2,1;3,1;-4,0;-3,0;-3,1", quad);
        Structure diehard = new Structure(
                "diehard",
                "Disappears after 130 generations",
                diehardMap,
                diehardPivot,
                9,
                3
        );
        initializedStructures.put("diehard", diehard);

        // Acorn
        Coords2D acornPivot = new Coords2D(3, 1);
        Map<Coords2D, CellState> acornMap = generateMap("0,0;-2,-1;-3,1;-2,1;1,1;2,1;3,1", quad);
        Structure acorn = new Structure(
                "acorn",
                "Produces 633 cells after 5206 generations",
                acornMap,
                acornPivot,
                8,
                3
        );
        initializedStructures.put("acorn", acorn);

        // Garden of Eden
        // Smallest immortal
        // 5x5 immortal

        return initializedStructures;
    }

    private static Map<String, Structure> binaryStructures = initializeStructures(false);

    private static Map<String, Structure> quadStructures = initializeStructures(true);

    private static Map<Coords2D, CellState> generateMap(String pattern, boolean quad) {
        Map<Coords2D, CellState> resultMap = new HashMap<>();

        Scanner patternScanner = new Scanner(pattern);
        patternScanner.useDelimiter(";");
        while(patternScanner.hasNext()) {
            Scanner coordsScanner = new Scanner(patternScanner.next());
            coordsScanner.useDelimiter(",");

            if(quad) {
                Random random = new Random();
                int which = random.nextInt(4);
                switch (which) {
                    case 0:
                        resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), QuadState.RED);
                        break;
                    case 1:
                        resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), QuadState.GREEN);
                        break;
                    case 2:
                        resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), QuadState.YELLOW);
                        break;
                    case 3:
                        resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), QuadState.BLUE);
                        break;
                }
            }
            else
                resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), BinaryState.ALIVE);
        }

        return resultMap;
    }
}
