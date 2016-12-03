package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;
import cells.coordinates.Coords2D;
import cells.states.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AutomatonDisplay2D extends AutomatonDisplay {
    public AutomatonDisplay2D(Automaton automaton, int width, int height, int cellSize) {
        super(automaton, width, height, cellSize);
    }

    @Override
    protected final void updateCell(Cell cell) {
        Coords2D coords = (Coords2D) cell.getCoords();

        draw.setFill(getCellFillColor(cell.getState()));

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + coords.getY()*CELL_SIZE,
                      CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);
    }
}
