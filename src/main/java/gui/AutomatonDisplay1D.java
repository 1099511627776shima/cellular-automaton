package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;

/**
 * Created by bzdeco on 03.12.16.
 */
public class AutomatonDisplay1D extends AutomatonDisplay {
    public AutomatonDisplay1D(Automaton automaton, int width, int cellSize) {
        super(automaton, width, (width+1)/2, cellSize);
        currentRow = 0;
    }

    @Override
    protected final void updateCell(Cell cell) {
        Coords1D coords = (Coords1D)cell.getCoords();

        draw.setFill(getCellFillColor(cell.getState()));

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + currentRow*CELL_SIZE,
                      CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);
    }

    @Override
    public void display() {
        Automaton.CellIterator iterator = automaton.cellIterator();

        while(iterator.hasNext()) {
            updateCell(iterator.next());
        }

        currentRow = Math.floorMod(currentRow + 1, height);
    }

    /** Tracking on which row the next generation should be drawn */
    private int currentRow;
}
