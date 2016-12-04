package automatons;

import cells.Cell;
import cells.coordinates.Coords2D;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.VonNeumannNeighbourhood;
import cells.states.*;
import exceptions.NotEnoughNeighboursException;

import java.util.*;

public class LangtonAnt extends Automaton2Dim {
    // Neighbourhood must be Von Neumann with r = 1 and wrapping enabled;
    public LangtonAnt(CellStateFactory stateFactory, int width, int height) {
        super(stateFactory, new VonNeumannNeighbourhood(width, height, 1, true), width, height);
    }

    @Override
    protected Automaton newInstance(CellStateFactory stateFactory, CellNeighbourhood neighbourhood) {
        return new LangtonAnt(stateFactory, getWidth(), getHeight());
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
        int antsToAdd;
        if(leftState.hasAnt()) {
            if(leftState.cellState.equals(BinaryState.DEAD))
                antsToAdd = Collections.frequency(leftState.antState, AntState.NORTH);
            else
                antsToAdd = Collections.frequency(leftState.antState, AntState.SOUTH);

            for(int i=0; i < antsToAdd; i++)
                newLangtonState.addAnt(AntState.EAST);
        }

        // Ants from right cell
        if(rightState.hasAnt()) {
            if(rightState.cellState.equals(BinaryState.DEAD))
                antsToAdd = Collections.frequency(rightState.antState, AntState.SOUTH);
            else
                antsToAdd = Collections.frequency(rightState.antState, AntState.NORTH);

            for(int i=0; i < antsToAdd; i++)
                newLangtonState.addAnt(AntState.WEST);
        }

        // Ants from top cell
        if(topState.hasAnt()) {
            if(topState.cellState.equals(BinaryState.DEAD))
                antsToAdd = Collections.frequency(topState.antState, AntState.EAST);
            else
                antsToAdd = Collections.frequency(topState.antState, AntState.WEST);

            for(int i=0; i < antsToAdd; i++)
                newLangtonState.addAnt(AntState.SOUTH);
        }

        // Ants from bottom cell
        if(bottomState.hasAnt()) {
            if(bottomState.cellState.equals(BinaryState.DEAD))
                antsToAdd = Collections.frequency(bottomState.antState, AntState.WEST);
            else
                antsToAdd = Collections.frequency(bottomState.antState, AntState.EAST);

            for(int i=0; i < antsToAdd; i++)
                newLangtonState.addAnt(AntState.NORTH);
        }

        return newLangtonState;
    }
}
