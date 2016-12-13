package cells.states;

/**
 * All possible cell states for Wire World automaton.
 */
public enum WireElectronState implements CellState {
    VOID, WIRE, ELECTRON_HEAD, ELECTRON_TAIL;
}
