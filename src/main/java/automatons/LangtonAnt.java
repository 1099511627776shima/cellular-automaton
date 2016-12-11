package automatons;

import cells.Cell;
import cells.coordinates.Coords2D;
import cells.neighbourhood.VonNeumannNeighbourhood;
import cells.states.*;
import exceptions.NotEnoughNeighboursException;

import java.util.*;

public class LangtonAnt extends Automaton2Dim {
    // Neighbourhood must be Von Neumann with r = 1 and wrapping enabled;
    public LangtonAnt(CellStateFactory stateFactory, int width, int height) {
        // Fixed neighbourhood (VonNeumann with wrapping enabled)
        super(stateFactory, new VonNeumannNeighbourhood(width, height, 1, true), width, height);
    }

    @Override
    protected Automaton newInstance() {
        return new LangtonAnt(getStateFactory(), getWidth(), getHeight());
    }

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