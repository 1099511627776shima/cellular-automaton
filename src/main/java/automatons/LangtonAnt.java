package automatons;

import cells.Cell;
import cells.coordinates.Coords2D;
import cells.neighbourhood.VonNeumannNeighbourhood;
import cells.states.*;
import exceptions.NotEnoughNeighboursException;

import java.util.*;

/**
 * This is a concrete class representing the two-dimensional automaton called Langton's Ant. This is a type of the automaton where one or more so-called ants traverse through the cells of the automaton changing the current state of the cell to the opposite one. Movement of ants is defined by simple rules - ant turns right on the dead cell and turns left on the live cell. Ants don't interact with each other.
 */
public class LangtonAnt extends Automaton2Dim {
    /**
     * Sole constructor of this class used to create Langton's Ant automaton with specified dimensions and cells initialization. For Langton's Ant automaton the only neighbourhood for the typical rules of ant movement must is Von Neumann Neighbourhood with radius set to 1.
     * @param stateFactory specifies which state factory should be used for initialization of cells
     * @param width specifies the number of cells in width
     * @param height specifies the number of cells in height
     * @see cells.neighbourhood.VonNeumannNeighbourhood
     */
    public LangtonAnt(CellStateFactory stateFactory, int width, int height) {
        // Fixed neighbourhood (VonNeumann with wrapping enabled)
        super(stateFactory, new VonNeumannNeighbourhood(width, height, 1, true), width, height);
    }

    @Override
    protected Automaton newInstance() {
        return new LangtonAnt(getStateFactory(), getWidth(), getHeight());
    }

    /**
     * Gets the state of the given cell in the next generation of the automaton. The state of the cells can be changed only when the cell is left by the ant. So if there are no ants on the cell it will have the same state in the next generation. Number of ants on the cell doesn't affect changing state to the opposite one.
     *
     * @param currentCell the cell which state in the next generation is determined
     * @param neighboursStates set of all the cells recognized as neighbours of the current cell
     * @return unchanged state if there are no ants on the cell or the opposite one otherwise
     */
    @Override
    protected CellState nextCellState(Cell currentCell, Set<Cell> neighboursStates) {
        if(neighboursStates.size() < 4) throw new NotEnoughNeighboursException("One or more neighbours of Langton Cell not identified");

        LangtonCell currentLangtonState = (LangtonCell)currentCell.getState();
        LangtonCell newLangtonState;

        // Give the new cell an opposite state if there is an ant on it
        if(currentLangtonState.hasAnt()) newLangtonState = new LangtonCell(currentLangtonState.cellState.equals(BinaryState.ALIVE) ? BinaryState.DEAD : BinaryState.ALIVE);
        else newLangtonState = new LangtonCell(currentLangtonState.cellState);

        // Checking for ants in 4 adjusting cells (Von Neumann Neighbourhood with r = 1 and wrapping enabled)

        // Getting positions of neighbours
        int currentCellXCoord = ((Coords2D)currentCell.getCoords()).getX();
        int currentCellYCoord = ((Coords2D)currentCell.getCoords()).getY();
        Coords2D leftCoords = new Coords2D(Math.floorMod(currentCellXCoord - 1, getWidth()), currentCellYCoord);
        Coords2D rightCoords = new Coords2D(Math.floorMod(currentCellXCoord + 1, getWidth()), currentCellYCoord);
        Coords2D topCoords = new Coords2D(currentCellXCoord, Math.floorMod(currentCellYCoord - 1, getHeight()));
        Coords2D bottomCoords = new Coords2D(currentCellXCoord, Math.floorMod(currentCellYCoord + 1, getHeight()));

        // Getting states of neighbours
        LangtonCell leftState = null, rightState = null, topState = null, bottomState = null;
        for(Cell cell : neighboursStates) {
            if(cell.getCoords().equals(leftCoords)) leftState = (LangtonCell)cell.getState();
            if(cell.getCoords().equals(rightCoords)) rightState = (LangtonCell)cell.getState();
            if(cell.getCoords().equals(topCoords)) topState = (LangtonCell)cell.getState();
            if(cell.getCoords().equals(bottomCoords)) bottomState = (LangtonCell)cell.getState();
        }

        // Checking if ant can come from adjacent cell

        // Ants from left cell
        AntState searchedState;
        if(leftState.hasAnt()) {
            if(leftState.cellState.equals(BinaryState.DEAD))
                searchedState = AntState.NORTH;
            else
                searchedState = AntState.SOUTH;

            for(int antID : leftState.antIDs) {
                if(leftState.antStates.get(antID).equals(searchedState))
                    newLangtonState.addAnt(antID, AntState.EAST);
            }
        }

        // Ants from right cell
        if(rightState.hasAnt()) {
            if(rightState.cellState.equals(BinaryState.DEAD))
                searchedState = AntState.SOUTH;
            else
                searchedState = AntState.NORTH;

            for(int antID : rightState.antIDs) {
                if (rightState.antStates.get(antID).equals(searchedState))
                    newLangtonState.addAnt(antID, AntState.WEST);
            }
        }

        // Ants from top cell
        if(topState.hasAnt()) {
            if(topState.cellState.equals(BinaryState.DEAD))
                searchedState = AntState.EAST;
            else
                searchedState = AntState.WEST;

            for(int antID : topState.antIDs) {
                if (topState.antStates.get(antID).equals(searchedState))
                    newLangtonState.addAnt(antID, AntState.SOUTH);
            }
        }

        // Ants from bottom cell
        if(bottomState.hasAnt()) {
            if(bottomState.cellState.equals(BinaryState.DEAD))
                searchedState = AntState.WEST;
            else
                searchedState = AntState.EAST;

            for(int antID : bottomState.antIDs) {
                if (bottomState.antStates.get(antID).equals(searchedState))
                    newLangtonState.addAnt(antID, AntState.NORTH);
            }
        }

        return newLangtonState;
    }
}