package cells.states;

import java.util.*;

public class LangtonCell implements CellState {
    public BinaryState cellState;
    /** List of IDs of ants on the cell */
    public ArrayList<Integer> antIDs = new ArrayList<>();
    /** Map of antIDs and corresponding ant states for ants on the cell */
    public Map<Integer, AntState> antStates = new HashMap<>();

    public LangtonCell(BinaryState cellState) {
        this.cellState = cellState;
        clearCell();
    }

    /** Adds ant of given ID and state to the cell
     * @param antID ID of the added ant
     * @param state State of the added ant
     */
    public void addAnt(int antID, AntState state) {
        antIDs.add(antID);
        antStates.put(antID, state);
    }

    /** Removes all ants from the cell */
    public void clearCell() {
        antIDs = new ArrayList<>();
        antStates = new HashMap<>();
    }

    /** Checks if there is an ant on the cell */
    public boolean hasAnt() {
        return antStates.size() > 0;
    }
}
