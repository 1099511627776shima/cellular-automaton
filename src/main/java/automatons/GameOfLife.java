package automatons;

import cells.Cell;
import cells.neighbourhood.CellNeighbourhood;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.CellStateFactory;
import cells.states.QuadState;

import java.util.*;

public class GameOfLife extends Automaton2Dim {
    public GameOfLife(CellStateFactory stateFactory, CellNeighbourhood neighbourStrategy, int width, int height, String rule, boolean quadLifeEnabled) {
        super(stateFactory, neighbourStrategy, width, height);

        // Sets rule to 23/3 as only in this scenario Quad Life makes sense
        if(quadLifeEnabled) this.rule = "23/3";
        else this.rule = rule;
        this.quadLifeEnabled = quadLifeEnabled;

        // Rule interpretation
        Scanner ruleScanner = new Scanner(rule);
        ruleScanner.useDelimiter("/");
        // FIXME assuming appropriate rule format
        int survival = ruleScanner.nextInt();
        int birth = ruleScanner.nextInt();


        // FIXME assuming only 1-9 rules (not greater than 10)
        while(survival > 0) {
            survivalCondition.add(survival % 10);
            survival /= 10;
        }
        while(birth > 0) {
            birthCondition.add(birth % 10);
            birth /= 10;
        }
    }


    protected String getRule() {
        return rule;
    }

    @Override
    protected Automaton newInstance(CellStateFactory stateFactory, CellNeighbourhood neighbourhood) {
        return new GameOfLife(stateFactory, neighbourhood, getWidth(), getHeight(), rule, quadLifeEnabled);
    }

    @Override
    protected CellState nextCellState(CellState currentState, Set<Cell> neighboursStates) {
        Set<Cell> aliveNeighbours = new HashSet<>();

        if(quadLifeEnabled) {
            // Creating alive neighbours set
            for(Cell cell : neighboursStates)
                if(cell.getState().equals(QuadState.RED) || cell.getState().equals(QuadState.YELLOW) || cell.getState().equals(QuadState.BLUE) || cell.getState().equals(QuadState.GREEN))
                    aliveNeighbours.add(cell);

            // If cell is alive
            if(currentState.equals(QuadState.RED) || currentState.equals(QuadState.YELLOW) || currentState.equals(QuadState.BLUE) || currentState.equals(QuadState.GREEN)) {
                if(survivalCondition.contains(aliveNeighbours.size())) return currentState;
                else return QuadState.DEAD;
            }
            // If cell is dead
            else {
                // If cell has enough alive neighbours
                if(birthCondition.contains(aliveNeighbours.size())) {

                    int redCells = 0, yellowCells = 0, blueCells = 0, greenCells = 0;
                    for(Cell cell : aliveNeighbours) {
                        QuadState currentQuadState = (QuadState)cell.getState();
                        switch(currentQuadState) {
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
                    if(redCells >= 2) return QuadState.RED;
                    else if(yellowCells >= 2) return QuadState.YELLOW;
                    else if(blueCells >= 2) return QuadState.BLUE;
                    else if(greenCells >= 2) return QuadState.GREEN;

                    // If there is no majority, the cell gets the remaining color
                    if(redCells == 0) return QuadState.RED;
                    else if(yellowCells == 0) return QuadState.YELLOW;
                    else if(blueCells == 0) return QuadState.BLUE;
                    else if(greenCells == 0) return QuadState.GREEN;
                }
                else
                    return QuadState.DEAD;
            }
        }
        // Quad Life disabled
        else {
            // Creating alive neighbours set
            for(Cell cell : neighboursStates)
                if(cell.getState().equals(BinaryState.ALIVE)) aliveNeighbours.add(cell);

            if(currentState.equals(BinaryState.ALIVE)) {
                if(survivalCondition.contains(aliveNeighbours.size())) return BinaryState.ALIVE;
                else return BinaryState.DEAD;
            } else {
                if(birthCondition.contains(aliveNeighbours.size())) return BinaryState.ALIVE;
                else return BinaryState.DEAD;
            }
        }

        return BinaryState.DEAD; // To silence IDE protests although all scenarios are covered above
    }

    /** String representation of a Rule */
    private String rule;
    /** Enables or disables Quad Life (4 different on states, sets rule to 23/3) */
    private boolean quadLifeEnabled;
    /** Set of integers within 0-8 range defining how many neighbours live cell needs to survive */
    private Set<Integer> survivalCondition = new HashSet<>();
    /** Set of integers within 0-8 range defining how many neighbours dead cell needs to be born */
    private Set<Integer> birthCondition = new HashSet<>();
}
