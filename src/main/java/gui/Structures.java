package gui;

import cells.coordinates.Coords2D;
import cells.states.*;

import java.util.*;

/**
 * This class is used to store and access all hardcoded structures.
 */
public class Structures {
    /**
     * Static method giving access to stored structures.
     *
     * @param name name of the structures to be returned. It must be given in lowercase.
     * @param mode mode of the automaton the structure will be inserted into
     * @return structure associated with the given name in the given mode
     */
    public static Structure getStructure(String name, AutomatonMode mode) {
        switch (mode) {
            case QUAD:
                return quadStructures.get(name);
            case BINARY:
                return binaryStructures.get(name);
            case WIREWORLD:
                return wireworldStructures.get(name);
            case ANT:
                return antStructures.get(name);
            default:
                return null;
        }
    }

    // For quad life colors are distributed randomly
    private static Map<String, Structure> initializeStructuresGoL(String mode) {
        Map<String, Structure> initializedStructures = new HashMap<>();

        /* Hardcoded Structures */

        // Glider
        Map<Coords2D, CellState> gliderMap = generateMap("0,-1;1,0;1,1;0,1;-1,1", mode);
        Structure glider = new Structure(
                "glider",
                "Moving structure",
                gliderMap,
                3,
                3
        );
        initializedStructures.put("glider", glider);

        // Gosper Glider Gun
        Map<Coords2D, CellState> gosperGliderGunMap = generateMap("-1,1;-2,1;-4,1;-8,1;-17,1;-18,1;-2,0;-8,0;-17,0;-18,0;2,0;3,0;4,1;6,1;2,-1;3,-1;16,-1;17,-1;16,-2;17,-2;2,-2;3,-2;-3,-1;-7,-1;-6,-2;-5,-2;4,-3;6,-3;6,-4;6,2;-2,2;-3,3;-5,4;-6,4;-7,3;-8,2", mode);
        Structure gosperGliderGun = new Structure(
                "Gosper Glider Gun",
                "Producing structure",
                gosperGliderGunMap,
                37,
                9
        );
        initializedStructures.put("gosper glider gun", gosperGliderGun);

        // Lightweight spaceship
        Map<Coords2D, CellState> lightweightSpaceshipMap = generateMap("1,-1;-2,-1;2,0;2,1;-2,1;-1,2;0,2;1,2;2,2", mode);
        Structure lightweightSpaceship = new Structure(
                "Lightweight spaceship",
                "Moving structure",
                lightweightSpaceshipMap,
                5,
                5
        );
        initializedStructures.put("lightweight spaceship", lightweightSpaceship);

        // Beacon
        Map<Coords2D, CellState> beaconMap = generateMap("-1,-1;0,-1;-1,0;0,0;1,1;2,1;1,2;2,2", mode);
        Structure beacon = new Structure(
                "Beacon",
                "Oscillator",
                beaconMap,
                4,
                4
        );
        initializedStructures.put("beacon", beacon);

        // Blinker
        Map<Coords2D, CellState> blinkerMap = generateMap("0,-1;0,0;0,1", mode);
        Structure blinker = new Structure(
                "Blinker",
                "Oscillator",
                blinkerMap,
                3,
                3
        );
        initializedStructures.put("blinker", blinker);

        // Toad
        Map<Coords2D, CellState> toadMap = generateMap("0,0;1,0;2,0;-1,1;0,1;1,1", mode);
        Structure toad = new Structure(
                "Toad",
                "Oscillator",
                toadMap,
                4,
                2
        );
        initializedStructures.put("toad", toad);

        // Pulsar
        Map<Coords2D, CellState> pulsarMap = generateMap("2,1;3,1;4,1;2,6;3,6;4,6;1,2;1,3;1,4;6,2;6,3;6,4;-2,1;-3,1;-4,1;-2,6;-3,6;-4,6;-1,2;-1,3;-1,4;-6,2;-6,3;-6,4;2,-1;3,-1;4,-1;2,-6;3,-6;4,-6;1,-2;1,-3;1,-4;6,-2;6,-3;6,-4;-2,-1;-3,-1;-4,-1;-2,-6;-3,-6;-4,-6;-1,-2;-1,-3;-1,-4;-6,-2;-6,-3;-6,-4", mode);
        Structure pulsar = new Structure(
                "Pulsar",
                "Oscillator",
                pulsarMap,
                13,
                13
        );
        initializedStructures.put("pulsar", pulsar);

        // Block
        Map<Coords2D, CellState> blockMap = generateMap("0,0;1,0;0,1;1,1", mode);
        Structure block = new Structure(
                "block",
                "Still life",
                blockMap,
                2,
                2
        );
        initializedStructures.put("block", block);

        // Boat
        Map<Coords2D, CellState> boatMap = generateMap("-1,0;-1,-1;0,-1;1,0;0,1", mode);
        Structure boat = new Structure(
                "boat",
                "Still life",
                boatMap,
                3,
                3
        );
        initializedStructures.put("boat", boat);

        // Beehive
        Map<Coords2D, CellState> beehiveMap = generateMap("-1,0;0,-1;1,-1;2,0;1,1;0,1", mode);
        Structure beehive = new Structure(
                "beehive",
                "Still life",
                beehiveMap,
                4,
                3
        );
        initializedStructures.put("beehive", beehive);

        // Loaf
        Map<Coords2D, CellState> loafMap = generateMap("0,-1;1,-1;-1,0;2,0;0,1;2,1;1,2", mode);
        Structure loaf = new Structure(
                "loaf",
                "Still life",
                loafMap,
                4,
                4
        );
        initializedStructures.put("loaf", loaf);

        // Pond
        Map<Coords2D, CellState> pondMap = generateMap("0,-1;1,-1;-1,0;-1,1;2,0;2,1;0,2;1,2", mode);
        Structure pond = new Structure(
                "pond",
                "Still life",
                pondMap,
                4,
                4
        );
        initializedStructures.put("pond", pond);

        // Tub
        Map<Coords2D, CellState> tubMap = generateMap("0,-1;0,1;1,0;-1,0", mode);
        Structure tub = new Structure(
                "tub",
                "Still life",
                tubMap,
                3,
                3
        );
        initializedStructures.put("tub", tub);

        // Small exploder
        Map<Coords2D, CellState> smallExploderMap = generateMap("0,-1;-1,0;0,0;1,0;-1,1;1,1;0,2", mode);
        Structure smallExploder = new Structure(
                "small exploder",
                "Producing structure transforming into still life",
                smallExploderMap,
                3,
                4
        );
        initializedStructures.put("small exploder", smallExploder);

        // Exploder
        Map<Coords2D, CellState> exploderMap = generateMap("0,-2;0,2;-2,-2;-2,-1;-2,0;-2,1;-2,2;2,-2;2,-1;2,0;2,1;2,2", mode);
        Structure exploder = new Structure(
                "exploder",
                "Producing structure transforming into pulsar",
                exploderMap,
                5,
                5
        );
        initializedStructures.put("exploder", exploder);

        // Crocodile
        Map<Coords2D, CellState> crocodileMap = generateMap("-5,0;-4,0;-3,0;-2,0;-1,0;0,0;1,0;2,0;3,0;4,0", mode);
        Structure crocodile = new Structure(
                "crocodile",
                "Oscillator",
                crocodileMap,
                11,
                1
        );
        initializedStructures.put("crocodile", crocodile);

        // Tumbler/Fountain
        Map<Coords2D, CellState> tumblerMap = generateMap("-2,-2;-1,-2;-2,-1;-1,-1;-1,0;-1,1;-1,2;-3,1;-3,2;-3,3;-2,3;2,-2;1,-2;2,-1;1,-1;1,0;1,1;1,2;3,1;3,2;3,3;2,3", mode);
        Structure tumbler = new Structure(
                "tumbler/fountain",
                "Oscillator",
                tumblerMap,
                7,
                6
        );
        initializedStructures.put("tumbler/fountain", tumbler);

        // The R-pentonimo
        Map<Coords2D, CellState> rpentonimoMap = generateMap("0,-1;1,-1;-1,0;0,0;0,1", mode);
        Structure rpentonimo = new Structure(
                "the r-pentonimo",
                "",
                rpentonimoMap,
                3,
                3
        );
        initializedStructures.put("the r-pentonimo", rpentonimo);

        // Diehard
        Map<Coords2D, CellState> diehardMap = generateMap("2,-1;1,1;2,1;3,1;-4,0;-3,0;-3,1", mode);
        Structure diehard = new Structure(
                "diehard",
                "Disappears after 130 generations",
                diehardMap,
                9,
                3
        );
        initializedStructures.put("diehard", diehard);

        // Acorn
        Map<Coords2D, CellState> acornMap = generateMap("0,0;-2,-1;-3,1;-2,1;1,1;2,1;3,1", mode);
        Structure acorn = new Structure(
                "acorn",
                "Produces 633 cells after 5206 generations",
                acornMap,
                8,
                3
        );
        initializedStructures.put("acorn", acorn);

        // Garden of Eden
        // Smallest immortal
        // 5x5 immortal

        return initializedStructures;
    }

    private static Map<String, Structure> initializeStructuresWW() {
        Map<String, Structure> initializedStructures = new HashMap<>();

        // AND
        Map<Coords2D, CellState> andMap = generateMap("0,-1;0,1;-1,0;-2,0;-3,0;-2,-1;-2,1;-4,1;-4,2;-4,3;-5,4;-6,3;-6,2;-6,1;-7,0;-8,0;0,-3;-1,-3;-2,-3;-3,-3;-4,-3;-5,-3;-6,-3;-7,-3;-8,-3;1,-2;2,-2;3,-2;1,2;2,2;3,2;2,1;2,3;4,-1;4,0;4,1;4,3;5,3;6,2;7,1;8,1", "wireworld");
        Structure and = new Structure(
                "and",
                "Logical gate",
                andMap,
                17,
                9
        );
        initializedStructures.put("and", and);

        // OR
        Map<Coords2D, CellState> orMap = generateMap("0,0;-1,0;1,0;2,0;3,0;0,-1;0,1;-1,-2;-2,-2;-3,-2;-1,2;-2,2;-3,2", "wireworld");
        Structure or = new Structure(
                "or",
                "Logical gate",
                orMap,
                7,
                5
        );
        initializedStructures.put("or", or);

        // XOR
        Map<Coords2D, CellState> xorMap = generateMap("1,0;2,0;3,0;1,-1;0,-1;-1,-1;-2,-1;1,1;0,1;-1,1;-2,1;-2,0;-1,-2;-1,2;-2,-3;-3,-3;-4,-3;-2,3;-3,3;-4,3", "wireworld");
        Structure xor = new Structure(
                "xor",
                "Logical gate",
                xorMap,
                9,
                7
        );
        initializedStructures.put("xor", xor);

        // NAND
        Map<Coords2D, CellState> nandMap = generateMap("0,0;-1,0;1,0;2,0;3,0;0,-1;0,1;-1,-2;0,-2;1,-2;-1,2;0,2;1,2;-4,-2;-3,-2;-2,-3;-1,-3;-1,-4;0,-4;1,-3;1,-5;2,-4;-4,2;-3,2;-2,3;-1,3;-1,4;0,4;1,3;1,5;2,4", "wireworld");
        Structure nand = new Structure(
                "nand",
                "Logical gate",
                nandMap,
                9,
                11
        );
        initializedStructures.put("nand", nand);

        // NOT
        Map<Coords2D, CellState> notMap = generateMap("0,-1;0,2;-2,2;2,-1;3,-1;-3,-1;-4,-1;-1,-2;-2,-2", "wireworld");
        notMap.put(new Coords2D(-1, 0), WireElectronState.ELECTRON_HEAD);
        notMap.put(new Coords2D(0, 0), WireElectronState.ELECTRON_HEAD);
        notMap.put(new Coords2D(1, 0), WireElectronState.ELECTRON_HEAD);
        notMap.put(new Coords2D(-1, 2), WireElectronState.ELECTRON_HEAD);
        notMap.put(new Coords2D(-2, 1), WireElectronState.ELECTRON_TAIL);
        notMap.put(new Coords2D(0, 1), WireElectronState.ELECTRON_TAIL);
        notMap.put(new Coords2D(1, 1), WireElectronState.ELECTRON_TAIL);
        Structure not = new Structure(
                "not",
                "Logical gate",
                notMap,
                9,
                5
        );
        initializedStructures.put("not", not);

        // Clock
        Map<Coords2D, CellState> clockMap = generateMap("0,-1;-1,-1;-2,-1;-3,0;-2,1;1,0;2,0;3,0", "wireworld");
        clockMap.put(new Coords2D(0, 1), WireElectronState.ELECTRON_TAIL);
        clockMap.put(new Coords2D(-1, 1), WireElectronState.ELECTRON_HEAD);
        Structure clock = new Structure(
                "clock",
                "Clock generating 1 electron every in 8 steps",
                clockMap,
                7,
                3
        );
        initializedStructures.put("clock", clock);

        return initializedStructures;
    }

    private static Map<String, Structure> binaryStructures = initializeStructuresGoL("binary");

    private static Map<String, Structure> quadStructures = initializeStructuresGoL("quad");

    private static Map<String, Structure> antStructures = initializeStructuresGoL("ant");

    private static Map<String, Structure> wireworldStructures = initializeStructuresWW();

    private static Map<Coords2D, CellState> generateMap(String pattern, String mode) {
        Map<Coords2D, CellState> resultMap = new HashMap<>();

        Scanner patternScanner = new Scanner(pattern);
        patternScanner.useDelimiter(";");
        while(patternScanner.hasNext()) {
            Scanner coordsScanner = new Scanner(patternScanner.next());
            coordsScanner.useDelimiter(",");

            switch(mode) {
                case "quad":
                    Random random = new Random();
                    // Generate random cell color
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
                    break;
                case "wireworld":
                    resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), WireElectronState.WIRE);
                    break;
                case "binary":
                    resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), BinaryState.ALIVE);
                    break;
                case "ant":
                    resultMap.put(new Coords2D(coordsScanner.nextInt(), coordsScanner.nextInt()), new LangtonCell(BinaryState.ALIVE));
                    break;

            }
        }

        return resultMap;
    }
}
