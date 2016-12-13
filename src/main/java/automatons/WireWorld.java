package automatons;

import cells.Cell;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.MooreNeighbourhood;
import cells.states.CellState;
import cells.states.CellStateFactory;
import cells.states.WireElectronState;

import java.util.*;

/**
 * This is a concrete class representing the two-dimensional automaton called Wire World. In this type of the automaton cells represent parts of the electrical circuit. They can be conductors, isolators or parts of the electron (it consists of 2 cells: head and tail).
 */
public class WireWorld extends Automaton2Dim {
    /**
     * Sole constructor of this class used to create Wire World automaton with specified dimensions, form of cell state initialization and enabled or disabled wrapping. For Wire World automaton the neighbourhood must be Moore Neighbourhood with radius set to 1. However it's possible to determine if neighbourhood should have wrapping enabled or not.
     * @param stateFactory specifies which state factory should be used for initialization of cells
     * @param width specifies the number of cells in width
     * @param height specifies the number of cells in height
     * @param wrappingEnabled specifies if the Moore Neighbourhood should use wrapping (if set to <code>true</code>) or not
     */
    public WireWorld(CellStateFactory stateFactory, int width, int height, boolean wrappingEnabled) {
        // Neighbourhood fixed (Moore Neighbourhood)
        super(stateFactory, new MooreNeighbourhood(width, height, 1, wrappingEnabled), width, height);
        this.wrappingEnabled = wrappingEnabled;
    }

    @Override
    protected Automaton newInstance() {
        return new WireWorld(getStateFactory(), getWidth(), getHeight(), wrappingEnabled);
    }

    /**
     * Gets the state of the given cell in the next generation of the automaton. The state can be changed only if there is a electron head among cell neighbours.
     *
     * @param currentCell the cell which state in the next generation is determined
     * @param neighboursStates set of all the cells recognized as neighbours of the current cell
     * @return the state of the given cell in the next generation. If the cell was a conductor in can stay a conductor or become electron head. Electron heads become electron tails. Electron tails become conductors and isolators remain isolators.
     * @see cells.neighbourhood.MooreNeighbourhood
     */
    @Override
    protected CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates) {
        CellState currentState = currentCell.getState();
        if(currentState.equals(WireElectronState.VOID)) return currentState;
        else if(currentState.equals(WireElectronState.ELECTRON_HEAD)) return WireElectronState.ELECTRON_TAIL;
        else if(currentState.equals(WireElectronState.ELECTRON_TAIL)) return WireElectronState.WIRE;
        // If currentState is WIRE
        else {
            int electronHeadCounter = 0;
            for(Cell cell : neighboursStates)
                if(cell.getState().equals(WireElectronState.ELECTRON_HEAD))
                    electronHeadCounter++;

            if(electronHeadCounter == 1 || electronHeadCounter == 2)
                return WireElectronState.ELECTRON_HEAD;
            else
                return currentState;
        }
    }

    // Must be stored here to be accessible by newInstance (as neighbourhood is fixed and given only choice of using wrapping or not)
    private boolean wrappingEnabled;
}
