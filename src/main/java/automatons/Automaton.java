package automatons;

import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.*;

import java.util.*;

public abstract class Automaton {
    public Automaton(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy) {
        this.stateFactory = stateFactory;
        this.neighbourStrategy = neighbourStrategy;
    }

    public Automaton nextState() {
        Automaton newAutomoton = newInstance();
        // Iterates through the Automaton representing old state
        CellIterator modelIterator = this.cellIterator();
        // Iterates through the Automaton representing new state
        CellIterator stateIterator = newAutomoton.cellIterator();

        while(modelIterator.hasNext()) {
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

    public void insertStructure(Map<? extends CellCoordinates, ? extends CellState> structure) {
        cells.putAll(structure);
    }

    public CellStateFactory getStateFactory() {
        return stateFactory;
    }

    public CellNeighbourhood getNeighbourStrategy() {
        return neighbourStrategy;
    }

    @Override
    public boolean equals(Object other) {
        Automaton otherAutomoton = (Automaton) other;
        CellIterator iterator = this.cellIterator();
        CellIterator otherIterator = otherAutomoton.cellIterator();

        while(iterator.hasNext() && otherIterator.hasNext()) {
            if(!iterator.next().equals(otherIterator.next())) return false;
        }
        return true;
    }

    public CellIterator cellIterator() {
        return new CellIterator();
    }

    protected abstract Automaton newInstance();
    protected abstract CellCoordinates initialCoordinates();
    protected abstract boolean hasNextCoordinates(CellCoordinates coords);
    protected abstract CellCoordinates nextCoordinates(CellCoordinates coords);
    protected abstract CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates);

    public class CellIterator {
        public CellIterator() {
            currentCoordinates = initialCoordinates();
        }
        public boolean hasNext() {
            return hasNextCoordinates(currentCoordinates);
        }
        public Cell next() {
            Cell cell = new Cell(nextCoordinates(currentCoordinates), cells.get(nextCoordinates(currentCoordinates)));
            currentCoordinates = cell.getCoords();
            return cell;
        }
        public void setState(CellState state) {
            Automaton.this.setCellState(currentCoordinates, state);
        }
        private CellCoordinates currentCoordinates;
    }

    private void setCellState(CellCoordinates coords, CellState state) {
        cells.put(coords, state);
    }

    /** Returns set of cells of given coordinates.
     *  @param coordsSet Set of coordinates for created cells
     *  @return Set of cells made from given set of coordinates
     */
    private Set<Cell> mapCoordinates(Set<CellCoordinates> coordsSet) {
        Set<Cell> mappedCells = new HashSet<>();
        for(CellCoordinates coords : coordsSet) {
            mappedCells.add(new Cell(coords, cells.get(coords)));
        }

        return mappedCells;
    }

    private Map<CellCoordinates, CellState> cells = new HashMap<>();
    private CellNeighbourhood neighbourStrategy;
    private CellStateFactory stateFactory;
}