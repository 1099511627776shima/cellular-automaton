package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords1D;
import cells.coordinates.Coords2D;
import cells.states.*;
import gui.controllers.MainStageController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * This is an abstract class for all types of automaton displays. It provides basic methods for displaying the automaton.
 */
public abstract class AutomatonDisplay {
    /**
     * Sole constructor of this class used by subclasses. Links display to the displayed automaton, prepares Canvas for displaying automaton setting it size regarding number of cells and size of the displeyed cell given in pixels.
     * @param automaton automaton which will be displayed. Later to be update by <code>updateAutomaton</code> method
     * @param width number of cells in width of the displayed automaton
     * @param height number of cells in height of the displayed automaton
     * @param cellSize size of the single displayed cell given in pixels
     */
    public AutomatonDisplay(Automaton automaton, int width, int height, int cellSize) {
        this.CELL_SIZE = cellSize;
        this.automaton = automaton;
        setCanvas(width, height);
    }

    /**
     * Updates automaton linked to the display with a new automaton. Used in <code>stepForward</code> method from the <code>MainStageController</code> class
     *
     * @param automaton automaton with which the display should be linked
     * @see MainStageController#stepForward()
     */
    public void updateAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }

    /**
     * Sets the new JavaFX Canvas for the automaton display. Used when creating a completely new automaton.
     *
     * @param width width of the new automaton given in number of cells
     * @param height height of the new automaton given in number of cells
     * @see MainStageController#createAutomaton(Automaton, int, int, AutomatonMode)
     */
    public void setCanvas(int width, int height) {
        canvas = new Canvas(width*CELL_SIZE, height*CELL_SIZE);
        this.width = width;
        this.height = height;
        draw = canvas.getGraphicsContext2D();
    }

    /**
     * Returns the <code>Canvas</code> on which the automaton is displayed.
     *
     * @return JavaFX Canvas on which the automaton cells are drawn
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * Sets automaton drawing options.
     *
     * @param strokeColor color of the board grid used to separate cells
     * @param strokeWidth width of the board grid
     */
    public void setDrawParameters(Color strokeColor, double strokeWidth) {
        draw.setStroke(strokeColor);
        draw.setLineWidth(strokeWidth);
        offset = strokeWidth / 2;
    }

    /**
     * Draws the grid of the board which separates cells.
     */
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

    /**
     * Display the automaton currently linked to automaton display. Used for initial display and after updating the automaton with <code>updateAutomaton</code> method.
     */
    public void display() {
        Automaton.CellIterator iterator = automaton.cellIterator();

        while(iterator.hasNext()) {
            updateCell(iterator.next());
        }
    }

    /**
     * Updates the history of the displayed automatons for these types of cellular automaton that need to remember their previous states to display next state.
     */
    public abstract void updateDisplayHistory();

    /**
     * Update the display color of the cell. If cell changed its state it will be drawn with a new fill color according to its cell state.
     *
     * @param cell cell to be redrawn with same of new color
     * @see gui.CellStateColor
     */
    protected abstract void updateCell(Cell cell);

    /**
     * Automaton that is currently displayed and on which the <code>display</code> method operates.
     */
    protected Automaton automaton;

    /**
     * JavaFX Canvas on which the automaton is drawn.
     */
    protected Canvas canvas;

    /**
     * Number of cells in width of the {@link gui.AutomatonDisplay#automaton}.
     */
    protected int width;

    /**
     * Number of cells in height of the {@link gui.AutomatonDisplay#automaton}.
     */
    protected int height;

    /**
     * JavaFX GraphicsContext used to access Canvas and draw on it.
     */
    protected GraphicsContext draw;

    /**
     * Size of the single cell given in pixels.
     */
    final protected int CELL_SIZE;

    /**
     * Equals half of the stroke width used to draw automaton grid. Used to properly fill displayed cells.
     */
    protected double offset;
}
