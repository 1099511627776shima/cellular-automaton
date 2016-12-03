package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;
import cells.coordinates.Coords2D;
import cells.states.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by bzdeco on 03.12.16.
 */
public abstract class AutomatonDisplay {
    public AutomatonDisplay(Automaton automaton, int width, int height, int cellSize) {
        this.CELL_SIZE = cellSize;
        this.automaton = automaton;
        setCanvas(width, height);
    }

    public void updateAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }

    public void setCanvas(int width, int height) {
        canvas = new Canvas(width*CELL_SIZE, height*CELL_SIZE);
        this.width = width;
        this.height = height;
        draw = canvas.getGraphicsContext2D();
    }

    public void setDrawParameters(Color strokeColor, double strokeWidth) {
        draw.setStroke(strokeColor);
        draw.setLineWidth(strokeWidth);
        offset = strokeWidth / 2;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void drawBoard() {
        // Horizontal lines
        for(int y = 0; y <= height; y++) {
            draw.moveTo(0,y*CELL_SIZE);
            draw.lineTo(width*CELL_SIZE,y*CELL_SIZE);
            draw.stroke();
        }
        // Vertical lines
        for(int x = 0; x <= width; x++) {
            draw.moveTo(x*CELL_SIZE, 0);
            draw.lineTo(x*CELL_SIZE, height*CELL_SIZE);
            draw.stroke();
        }
    }

    public void display() {
        Automaton.CellIterator iterator = automaton.cellIterator();

        while(iterator.hasNext()) {
            updateCell(iterator.next());
        }
    }

    protected abstract void updateCell(Cell cell);

    protected Color getCellFillColor(CellState state) {
        if(state.equals(BinaryState.ALIVE)) {
            return Color.BLACK;
        }
        else if(state.equals(BinaryState.DEAD)) {
            return Color.WHITE;
        }
        else if(state.equals(QuadState.GREEN)) {
            return Color.GREEN;
        }
        else if(state.equals(QuadState.YELLOW)) {
            return Color.YELLOW;
        }
        else if(state.equals(QuadState.BLUE)) {
            return Color.BLUE;
        }
        else if(state.equals(QuadState.RED)) {
            return Color.RED;
        }
        else if(state.equals(QuadState.DEAD)) {
            return Color.WHITE;
        }
        else if(state.equals(WireElectronState.VOID)) {
            return new Color(96/255, 9/255, 41/255, 1);
        }
        else if(state.equals(WireElectronState.WIRE)) {
            return Color.LAWNGREEN;
        }
        else if(state.equals(WireElectronState.ELECTRON_HEAD)) {
            return Color.DEEPSKYBLUE;
        }
        else if(state.equals(WireElectronState.ELECTRON_TAIL)) {
            return Color.ORANGERED;
        }
        else if(state instanceof LangtonCell) {
            LangtonCell langtonCell = (LangtonCell)state;

            if(langtonCell.cellState.equals(BinaryState.ALIVE))
                return Color.BLACK;
            else
                return Color.WHITE;
        }

        return Color.TRANSPARENT;
    }

    protected Automaton automaton;
    protected Canvas canvas;
    protected int width;
    protected int height;
    protected GraphicsContext draw;
    final protected int CELL_SIZE;
    protected double offset;
}
