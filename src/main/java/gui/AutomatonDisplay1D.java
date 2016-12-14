package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is a concrete class used to display one-dimensional automatons.
 */
public class AutomatonDisplay1D extends AutomatonDisplay {
    public AutomatonDisplay1D(Automaton automaton, int width, int cellSize) {
        super(automaton, width, (width+1)/2, cellSize);
        currentRow = 0;
        history = new ArrayList<>();
    }

    @Override
    protected final void updateCell(Cell cell) {
        Coords1D coords = (Coords1D)cell.getCoords();

        draw.setFill(CellStateColor.get(cell.getState()));

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + currentRow*CELL_SIZE,
                      CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);
    }

    @Override
    public void display() {
        // currentRow == height when we reach end of rows in canvas
        if(currentRow == height) {
            currentRow = 0;
            // Remove the oldest element in history
            history.remove(0);
            for(int i = 0; i < history.size(); i++) {
                Automaton.CellIterator it = history.get(i).cellIterator();
                while(it.hasNext())
                    updateCell(it.next());
                currentRow++;
            }
        }

        // Drawing current state
        Automaton.CellIterator iterator = automaton.cellIterator();
        while (iterator.hasNext()) {
            updateCell(iterator.next());
        }
        currentRow++;
    }

    /**
     * Adds current automaton to the history that will be used when the next state of the automaton is drawn. Used to draw rows of cells of ancestors of the current automaton.
     */
    @Override
    public void updateDisplayHistory() {
        history.add(automaton);
    }

    // Tracking on which row the next generation should be drawn
    private int currentRow;
    private ArrayList<Automaton> history;
}
