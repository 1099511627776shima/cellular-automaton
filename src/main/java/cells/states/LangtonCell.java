package cells.states;

import java.util.*;

/**
 * This class is used to represent state of the cell in Langton's Ant automaton.
 * @see automatons.LangtonAnt
 */
public class LangtonCell implements CellState {
    /**
     * Binary state of the cell, which can be changed only by ant leaving the cell.
     */
    public BinaryState cellState;
    /**
     * List of IDs of ants on the cell
     */
    public ArrayList<Integer> antIDs = new ArrayList<>();
    /**
     * Map of antIDs and corresponding ant states for ants on the cell
     */
    public Map<Integer, AntState> antStates = new HashMap<>();

    /**
     * Sole constructor of this class used to create new cell state with a given state. Initially there are no ants on the cell.
     * @param cellState cell state with which cell should be initialized
     */
    public LangtonCell(BinaryState cellState) {
        this.cellState = cellState;
        clearCell();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LangtonCell that = (LangtonCell) o;

        if (cellState != that.cellState) return false;
        if (!antIDs.equals(that.antIDs)) return false;
        return antStates.equals(that.antStates);
    }

    @Override
    public int hashCode() {
        int result = cellState.hashCode();
        result = 31 * result + antIDs.hashCode();
        result = 31 * result + antStates.hashCode();
        return result;
    }

    /**
     * Adds ant of given ID and state to the cell
     * @param antID ID of the added ant
     * @param state state of the added ant
     */
    public void addAnt(int antID, AntState state) {
        antIDs.add(antID);
        antStates.put(antID, state);
    }

    /**
     * Removes all ants from the cell.
     */
    public void clearCell() {
        antIDs = new ArrayList<>();
        antStates = new HashMap<>();
    }

    /**
     * Checks if there is an ant on the cell.
     * @return if there is one or more ants on the cell returns <code>true</code>, otherwise <code>false</code>
     */
    public boolean hasAnt() {
        return antStates.size() > 0;
    }
}
