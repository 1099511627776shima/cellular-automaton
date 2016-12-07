package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by bzdeco on 03.12.16.
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
            // Get latest history element
            Iterator<Automaton> historyElementIt = history.get(history.size()-1).iterator();
            historyElementIt.next(); // omit first autromaton in it
            while(historyElementIt.hasNext()) {
                Automaton.CellIterator it = historyElementIt.next().cellIterator();
                while (it.hasNext()) {
                    updateCell(it.next());
                }
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

    @Override
    public void updateDisplayHistory() {
        ArrayList<Automaton> newHistoryElement = new ArrayList<>();

        // Copy latest history element to new (optionally omitting first automaton in this element)
        if(history.size() > 0) {
            ArrayList<Automaton> lastHistoryElement = history.get(history.size()-1);
            Iterator<Automaton> it = lastHistoryElement.iterator();
            // If last history element contains all rows of canvas
            if(lastHistoryElement.size() == height)
                it.next(); // Omit first element
            while(it.hasNext())
                newHistoryElement.add(it.next());
        }
        // Add latest state
        newHistoryElement.add(automaton);

        history.add(newHistoryElement);
    }

    @Override
    public void retrieveFromDisplayHistoryAndDisplay() {
        ArrayList<Automaton> historyElementToDisplay = history.remove(history.size()-1);
        Iterator<Automaton> automatonIterator = historyElementToDisplay.iterator();

        // Clear Canvas and redraw board
        clearCanvas();
        drawBoard();

        currentRow = 0;
        while(automatonIterator.hasNext()) {
            Automaton.CellIterator it = automatonIterator.next().cellIterator();
            while(it.hasNext())
                updateCell(it.next());
            currentRow++;
        }
    }

    /** Tracking on which row the next generation should be drawn */
    private int currentRow;
    private ArrayList<ArrayList<Automaton>> history;
}
