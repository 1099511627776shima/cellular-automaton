package automatons;

import cells.Cell;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.CellStateFactory;
import cells.states.QuadState;

import java.util.*;

/**
 * This is a concrete class representing the classical type of two-dimensional automaton. Game of Life specifies the
 * rules when cell can survive to the next generation and when a dead cell can become live. This class allows to specify
 * any rules for survival and birth conditions.
 */
public class GameOfLife extends Automaton2Dim {
    /**
     * Sole constructor of this class. Used to create Game of Life with specified dimensions, rules of survival and birth of the cells, way of finding cell neighbours and form of initialization. It is also possible to enable Quad Life, which allows cells to be in 4 different alive states.
     * @param stateFactory specifies which state factory should be used for initialization of cells
     * @param neighbourStrategy specifies the rules for finding neighbours of a given cell
     * @param width specifies the number of cells in width
     * @param height specifies the number of cells in height
     * @param survivalConditions specifies how many live cells among its neighbours does a live cell need to survive on to the next generation
     * @param birthConditions specifies how many live cells among its neighbours does a dead cell need to become alive in the next generation
     * @param quadLifeEnabled if <code>true</code> it enables cells to have 4 different live states
     */
    public GameOfLife(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy, int width, int height, Set<Integer> survivalConditions, Set<Integer> birthConditions, boolean quadLifeEnabled) {
        super(stateFactory, neighbourStrategy, width, height);

        // Sets rule to 23/3 as only in this scenario Quad Life makes sense
        if (quadLifeEnabled) {
            this.survivalConditions.add(2);
            this.survivalConditions.add(3);
            this.birthConditions.add(3);
        } else {
            this.survivalConditions.addAll(survivalConditions);
            this.birthConditions.addAll(birthConditions);
        }

        this.quadLifeEnabled = quadLifeEnabled;
    }

    @Override
    protected Automaton newInstance() {
        return new GameOfLife(getStateFactory(), getNeighbourStrategy(), getWidth(), getHeight(), survivalConditions, birthConditions, quadLifeEnabled);
    }

    /**
     * Gets the state of the given cell in the next generation of the automaton. In Game of Life next cell state is determined by the survival and birth conditions - the number of cells a particular cell needs to survive or to become a live cell.
     *
     * @param currentCell the cell which state in the next generation is determined
     * @param neighboursStates set of all the cells recognized as neighbours of the current cell
     * @return the state of the given cell in the next generation
     */
    @Override
    protected CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates) {
        Set<Cell> aliveNeighbours = new HashSet<>();
        CellState currentState = currentCell.getState();

        if (quadLifeEnabled) {
            // Creating alive neighbours set
            for (Cell cell : neighboursStates)
                if (cell.getState().equals(QuadState.RED) || cell.getState().equals(QuadState.YELLOW) || cell.getState().equals(QuadState.BLUE) || cell.getState().equals(QuadState.GREEN))
                    aliveNeighbours.add(cell);

            // If cell is alive
            if (currentState.equals(QuadState.RED) || currentState.equals(QuadState.YELLOW) || currentState.equals(QuadState.BLUE) || currentState.equals(QuadState.GREEN)) {
                if (survivalConditions.contains(aliveNeighbours.size())) return currentState;
                else return QuadState.DEAD;
            }
            // If cell is dead
            else {
                // If cell has enough alive neighbours
                if (birthConditions.contains(aliveNeighbours.size())) {

                    int redCells = 0, yellowCells = 0, blueCells = 0, greenCells = 0;
                    for (Cell cell : aliveNeighbours) {
                        QuadState currentQuadState = (QuadState) cell.getState();
                        switch (currentQuadState) {
                            case RED:
                                redCells++;
                                break;
                            case YELLOW:
                                yellowCells++;
                                break;
                            case BLUE:
                                blueCells++;
                                break;
                            case GREEN:
                                greenCells++;
                                break;
                        }
                    }

                    // Cell gets the color of the majority of the parents
                    if (redCells >= 2) return QuadState.RED;
                    else if (yellowCells >= 2) return QuadState.YELLOW;
                    else if (blueCells >= 2) return QuadState.BLUE;
                    else if (greenCells >= 2) return QuadState.GREEN;

                    // If there is no majority, the cell gets the remaining color
                    if (redCells == 0) return QuadState.RED;
                    else if (yellowCells == 0) return QuadState.YELLOW;
                    else if (blueCells == 0) return QuadState.BLUE;
                    else if (greenCells == 0) return QuadState.GREEN;
                } else return QuadState.DEAD;
            }
        }
        // Quad Life disabled
        else {
            // Creating alive neighbours set
            for (Cell cell : neighboursStates)
                if (cell.getState().equals(BinaryState.ALIVE)) aliveNeighbours.add(cell);

            if (currentState.equals(BinaryState.ALIVE)) {
                if (survivalConditions.contains(aliveNeighbours.size())) return BinaryState.ALIVE;
                else return BinaryState.DEAD;
            } else {
                if (birthConditions.contains(aliveNeighbours.size())) return BinaryState.ALIVE;
                else return BinaryState.DEAD;
            }
        }

        return BinaryState.DEAD; // To silence IDE protests although all scenarios are covered above
    }

    /**
     * Enables or disables Quad Life (4 different on states, sets rule to 23/3)
     */
    private boolean quadLifeEnabled;
    /**
     * Set of integers defining how many neighbours live cell needs to survive
     */
    private Set<Integer> survivalConditions = new HashSet<>();
    /**
     * Set of integers defining how many neighbours dead cell needs to become live
     */
    private Set<Integer> birthConditions = new HashSet<>();
}
