package automatons;

import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords1D;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.CellStateFactory;

import java.util.*;

public class ElementaryAutomaton extends Automaton1Dim {
    public ElementaryAutomaton(CellStateFactory stateFactory, CellNeighbourhood neighbourhoodStrategy, int size, int rule) {
        super(stateFactory, neighbourhoodStrategy, size);
        this.rule = rule;
    }

    protected int getRule() {
        return rule;
    }

    @Override
    protected Automaton newInstance(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy) {
        return new ElementaryAutomaton(stateFactory, neighbourStrategy, getSize(), rule);
    }

    @Override
    protected CellState nextCellState(CellState currentState, Set<Cell> neighboursStates) {
        // If cell is on the edge of board it will be dead
        if(neighboursStates.size() < 2) return BinaryState.DEAD;

        BinaryState currentBinaryState = (BinaryState) currentState;
        Iterator<Cell> neighboursIt = neighboursStates.iterator(); // it must be a LinkedHashSet to preserve adding order
        BinaryState leftNeigh, rightNeigh;
        String rule = String.format("%8s", Integer.toBinaryString(this.rule).replace(" ", "0"));

        // There are guaranteed 2 neighbours
        leftNeigh = (BinaryState) neighboursIt.next().getState();
        rightNeigh = (BinaryState) neighboursIt.next().getState();

        if(currentBinaryState.equals(BinaryState.ALIVE)) {
            //111
            if(rule.charAt(0) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
            //110
            else if(rule.charAt(1) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
            //011
            else if(rule.charAt(4) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
            //010
            else if(rule.charAt(5) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
        }
        else {
            //101
            if(rule.charAt(2) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
            //100
            else if(rule.charAt(3) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
            //001
            else if(rule.charAt(6) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
            //000
            else if(rule.charAt(7) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
        }

        return BinaryState.DEAD;
    }

    private int rule;
}
