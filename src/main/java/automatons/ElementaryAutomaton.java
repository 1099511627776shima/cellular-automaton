package automatons;

import cells.Cell;
import cells.coordinates.Coords1D;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.CellStateFactory;

import java.util.*;

/**
 * This is a concrete class representing the most basic one-dimensional automaton. Behaviour of the cells in this
 * automaton is defined by the given rule which is a number between 0 and 255. Binary representation of the rule encodes
 * all 8 combinations of states of a particular cell and 2 adjacent cells. Commonly displayed as rows of cells, where each row is a next generation - the oldest generations are on the top, the youngest on the bottom.
 */
public class ElementaryAutomaton extends Automaton1Dim {
    /**
     * Sole constructor of this class. Used to create elementary automaton with specified size, rule, way of finding cell neighbours and form of initialization.
     * @param stateFactory specifies which state factory should be used for initialization of cells
     * @param neighbourhoodStrategy specifies the rules for finding neighbours of a given cell
     * @param size specifies the number of cells in the automaton. Elementary automaton is commonly displayed as rows of cells so the size is the number of cells in a row.
     * @param rule
     */
    public ElementaryAutomaton(CellStateFactory stateFactory, CellNeighbourhood neighbourhoodStrategy, int size, int rule) {
        super(stateFactory, neighbourhoodStrategy, size);
        this.rule = rule;
    }

    /**
     * Gets the rule which determines the outcome which each combination of 3 cells produces.
     *
     * @return number representing rule by which cells in next generations are created
     */
    protected int getRule() {
        return rule;
    }

    @Override
    protected Automaton newInstance() {
        return new ElementaryAutomaton(getStateFactory(), getNeighbourStrategy(), getSize(), rule);
    }

    /**
     * Gets the state of the given cell in the next generation of the automaton. In elementary automaton next cell state is determined by the binary representation of the given rule.
     *
     * @param currentCell the cell which state in the next generation is determined
     * @param neighboursStates set of all the cells recognized as neighbours of the current cell
     * @return the state of the given cell in the next generation
     */
    @Override
    protected CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates) {

        BinaryState currentBinaryState = (BinaryState) currentCell.getState();

        BinaryState leftNeigh = null, rightNeigh = null;
        String rule = String.format("%8s", Integer.toBinaryString(this.rule).replace(" ", "0"));

        Coords1D currentCoords = (Coords1D) currentCell.getCoords();
        // If cell is on the edge of board the nonexistent neighbour is assumed dead
        if (neighboursStates.size() == 1) {
            Iterator<Cell> neighboursIt = neighboursStates.iterator();
            // left edge
            if (currentCoords.getX() == 0) {
                leftNeigh = BinaryState.DEAD;
                rightNeigh = (BinaryState) neighboursIt.next().getState();
            }
            //right edge
            else {
                leftNeigh = (BinaryState) neighboursIt.next().getState();
                rightNeigh = BinaryState.DEAD;
            }
        }
        // has both neighbours (due to being not on the verge of board or wrapping enabled)
        else {
            for (Cell cell : neighboursStates) {
                // Checking coords with regard to possible wrapping
                if (cell.getCoords().equals(new Coords1D(Math.floorMod(currentCoords.getX() - 1, getSize()))))
                    leftNeigh = (BinaryState) cell.getState();
                else rightNeigh = (BinaryState) cell.getState();
            }
        }

        if (currentBinaryState.equals(BinaryState.ALIVE)) {
            //111
            if (rule.charAt(0) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
                //110
            else if (rule.charAt(1) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
                //011
            else if (rule.charAt(4) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
                //010
            else if (rule.charAt(5) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
        } else {
            //101
            if (rule.charAt(2) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
                //100
            else if (rule.charAt(3) == '1' && leftNeigh.equals(BinaryState.ALIVE) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
                //001
            else if (rule.charAt(6) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.ALIVE))
                return BinaryState.ALIVE;
                //000
            else if (rule.charAt(7) == '1' && leftNeigh.equals(BinaryState.DEAD) && rightNeigh.equals(BinaryState.DEAD))
                return BinaryState.ALIVE;
        }

        return BinaryState.DEAD;
    }

    private int rule;
}
