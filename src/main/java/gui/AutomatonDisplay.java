package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;
import cells.coordinates.Coords2D;
import cells.states.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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

    public void setCanvas(double width, double height) {
        canvas = new Canvas(width*CELL_SIZE, height*CELL_SIZE);
        this.width = width;
        this.height = height;
        draw = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void clearCanvas() {
        draw.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
    }

    public void setDrawParameters(Color strokeColor, double strokeWidth) {
        draw.setStroke(strokeColor);
        draw.setLineWidth(strokeWidth);
        offset = strokeWidth / 2;
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

    public abstract void updateDisplayHistory();

    protected Automaton automaton;
    protected Canvas canvas;
    protected double width;
    protected double height;
    protected GraphicsContext draw;
    final protected int CELL_SIZE;
    protected double offset;
}
