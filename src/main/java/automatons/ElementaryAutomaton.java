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
    protected CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates) {

        BinaryState currentBinaryState = (BinaryState) currentCell.getState();

        BinaryState leftNeigh = null, rightNeigh = null;
        String rule = String.format("%8s", Integer.toBinaryString(this.rule).replace(" ", "0"));

        Coords1D currentCoords = (Coords1D)currentCell.getCoords();
        // If cell is on the edge of board the nonexistent neighbour is assumed dead
        if(neighboursStates.size() == 1) {
            Iterator<Cell> neighboursIt = neighboursStates.iterator();
            // left edge
            if(currentCoords.getX() == 0) {
                leftNeigh = BinaryState.DEAD;
                rightNeigh = (BinaryState)neighboursIt.next().getState();
            }
            //right edge
            else {
                leftNeigh = (BinaryState)neighboursIt.next().getState();
                rightNeigh = BinaryState.DEAD;
            }
        }
        // has both neighbours (due to being not on the verge of board or wrapping enabled)
        else {
            for(Cell cell : neighboursStates) {
                // Checking coords with regard to possible wrapping
                if(cell.getCoords().equals(new Coords1D(Math.floorMod(currentCoords.getX()-1, getSize())))) leftNeigh = (BinaryState)cell.getState();
                else rightNeigh = (BinaryState)cell.getState();
            }
        }

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
