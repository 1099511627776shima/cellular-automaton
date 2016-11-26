package automatons;

import cells.Cell;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.MooreNeighbourhood;
import cells.states.CellState;
import cells.states.CellStateFactory;
import cells.states.WireElectronState;

import java.util.*;

public class WireWorld extends Automaton2Dim {
    // Neighbourhood must be Moore with r = 1 and wrapping enabled (for some extreme schemes)
    public WireWorld(CellStateFactory stateFactory, int width, int height) {
        super(stateFactory, new MooreNeighbourhood(width, height, 1, true), width, height);
    }

    @Override
    protected Automaton newInstance(CellStateFactory stateFactory, CellNeighbourhood neighbourhood) {
        return new LangtonAnt(stateFactory, getWidth(), getHeight());
    }

    @Override
    protected CellState nextCellState(CellState currentState, Set<Cell> neighboursStates) {
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
}
