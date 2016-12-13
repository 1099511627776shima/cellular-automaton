package automatons;

import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.*;

import java.util.*;

/**
 * This is a base abstract class for all types of cellular automatons. It implements basic operations that can be done on
 * automatons like generating the next state and adding particular structures.
 */
public abstract class Automaton {
    /**
     * Sole constructor of this class. Used by subclasses when creating a particular type of an automaton.
     *
     * @param stateFactory      specifies which state factory should be used for initialization of cells
     * @param neighbourStrategy specifies the rules for finding neighbours of a given cell
     * @see cells.states.CellStateFactory
     * @see cells.neighbourhood.CellNeighbourhood
     */
    public Automaton(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy) {
        this.stateFactory = stateFactory;
        this.neighbourStrategy = neighbourStrategy;
    }

    /**
     * Generates the next generation of this automaton.
     *
     * @return the automaton representing the next generation of the cells in this automaton
     */
    public Automaton nextState() {
        Automaton newAutomoton = newInstance();
        // Iterates through the Automaton representing old state
        CellIterator modelIterator = this.cellIterator();
        // Iterates through the Automaton representing new state
        CellIterator stateIterator = newAutomoton.cellIterator();

        while (modelIterator.hasNext()) {
            Cell currentCell = modelIterator.next();
            stateIterator.next();
            Set<CellCoordinates> neighbours = neighbourStrategy.cellNeighbours(currentCell.getCoords());
            // Set of neighbours of current cell from old state
            Set<Cell> mappedNeighbours = mapCoordinates(neighbours);
            // Set new state for current cell in new state based on their old state and old neighbours
            stateIterator.setState(nextCellState(currentCell, mappedNeighbours));
        }

        return newAutomoton;
    }

    /**
     * Updates cells in automaton based on given structure.
     *
     * @param structure map containing pairs of cell coordinates and states that should be updated in this automaton
     */
    public void insertStructure(Map<? extends CellCoordinates, ? extends CellState> structure) {
        cells.putAll(structure);
    }

    /**
     * Returns the iterator that allows to iterate through all the cells of the automaton.
     *
     * @return iterator over all of the cells in this automaton
     */
    public CellIterator cellIterator() {
        return new CellIterator();
    }

    /**
     * This is an iterator class for the cells in automaton. It enables going through all of the cells in the automaton
     * and getting information about their coordinates and states.
     */
    public class CellIterator {
        /**
         * Default constructor for an iterator. It sets it at the cell before the first one, so that <code>next</code>
         * will return the first cell in the automaton.
         */
        CellIterator() {
            currentCoordinates = initialCoordinates();
        }

        /**
         * Checks if an iterator still hasn't reached the last cell in the automaton.
         *
         * @return <code>true</code> if cell currently pointed by the iterator isn't the last one, <code>false</code>
         * otherwise
         */
        public boolean hasNext() {
            return hasNextCoordinates(currentCoordinates);
        }

        /**
         * Gets the cell with the next coordinates specified by the result of <code>nextCoordinates</code> method.
         *
         * @return the {@link Cell} which is the next cell of the currently pointed one. Note that if iterator is
         * already pointing at the last cell in the automaton using this method will result in unpredictable behaviour.
         * @see cells.Cell
         */
        public Cell next() {
            Cell cell = new Cell(nextCoordinates(currentCoordinates), cells.get(nextCoordinates(currentCoordinates)));
            currentCoordinates = cell.getCoords();
            return cell;
        }

        /**
         * Updates the state of the cell currently pointed by the iterator with a given value.
         *
         * @param state new state which the pointed cell should get
         */
        public void setState(CellState state) {
            Automaton.this.setCellState(currentCoordinates, state);
        }

        private CellCoordinates currentCoordinates;
    }

    @Override
    public boolean equals(Object other) {
        Automaton otherAutomoton = (Automaton) other;
        CellIterator iterator = this.cellIterator();
        CellIterator otherIterator = otherAutomoton.cellIterator();

        while (iterator.hasNext() && otherIterator.hasNext()) {
            if (!iterator.next().equals(otherIterator.next())) return false;
        }
        return true;
    }

    CellStateFactory getStateFactory() {
        return stateFactory;
    }

    CellNeighbourhood getNeighbourStrategy() {
        return neighbourStrategy;
    }

    /**
     * Creates exact copy of the current automaton.
     *
     * @return duplicate of the current automaton with no internal changes
     */
    protected abstract Automaton newInstance();

    /**
     * Gets the coordinates of the non-existent cell before the first one in the automaton. It is used by the automaton iterator.
     *
     * @return coordinates of the cell right before the first in the current automaton.
     */
    protected abstract CellCoordinates initialCoordinates();

    /**
     * Checks if there is a cell following one with the given coordinates.
     * @param coords coordinates of the cell for which is checked if it has successor
     * @return <code>true</code> if the cell of given coordinates has successor, <code>false</code> otherwise
     */
    protected abstract boolean hasNextCoordinates(CellCoordinates coords);

    /**
     * Gets coordinates of the cell following the one with given coordinates
     * @param coords coordinates of the cell which successor's coordinates will be returned
     * @return coordinates of the successor of the cell with given coordinates. Note it will return coordinates of the non-existing cell if the given cell coordinates are last in the automaton.
     */
    protected abstract CellCoordinates nextCoordinates(CellCoordinates coords);

    /**
     * Gets the state a given cell will have in the next generation based on its neighbours and current state. Realization of this method depends solely on the type of the automaton and specified rules.
     * @param currentCell the cell which state in the next generation is determined
     * @param neighboursStates set of all the cells recognized as neighbours of the current cell
     * @return the state of the given cell in the next generation
     * @see cells.neighbourhood.CellNeighbourhood
     */
    protected abstract CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates);

    private void setCellState(CellCoordinates coords, CellState state) {
        cells.put(coords, state);
    }

    private Set<Cell> mapCoordinates(Set<CellCoordinates> coordsSet) {
        Set<Cell> mappedCells = new HashSet<>();
        for (CellCoordinates coords : coordsSet) {
            mappedCells.add(new Cell(coords, cells.get(coords)));
        }

        return mappedCells;
    }

    private Map<CellCoordinates, CellState> cells = new HashMap<>();
    private CellNeighbourhood neighbourStrategy;
    private CellStateFactory stateFactory;
}