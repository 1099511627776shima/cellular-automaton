package cells.states;

import java.util.*;

/* FIXME do we really need ant ID */

public class LangtonCell implements CellState {
    public BinaryState cellState;
    /** List of all ants currently on this cell */
    // public ArrayList<Integer> antID;
    /** Map of ant states according to their ID */
    public ArrayList<AntState> antState;

    public LangtonCell(BinaryState cellState) {
        this.cellState = cellState;
        clearCell();
    }

    /** Adds ant of given ID and state to the cell
     *
     * @param state State of the added ant
     */
    public void addAnt(AntState state) {
        //antID.add(ID);
        antState.add(state);
    }

    /** Removes all ants from the cell */
    public void clearCell() {
        // antID = new ArrayList<>();
        antState = new ArrayList<>();
    }

    /** Checks if there is an ant on the cell */
    public boolean hasAnt() {
        return antState.size() > 0;
    }
}
