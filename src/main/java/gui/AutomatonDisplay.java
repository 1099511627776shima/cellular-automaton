package gui;

import cells.Cell;
import cells.coordinates.Coords2D;
import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.QuadState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AutomatonDisplay {
    public AutomatonDisplay(int cellSize) {
        CELL_SIZE = cellSize;
        canvas = new Canvas(100,100);
    }

    public AutomatonDisplay(int width, int height, int cellSize) {
        this(cellSize);
        setCanvas(width, height);
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

    public void updateCell(Cell cell) {
        Coords2D coords = (Coords2D) cell.getCoords();
        draw.setFill(getCellFillColor(cell.getState()));
        double offset = draw.getLineWidth() / 2;

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + coords.getY()*CELL_SIZE,
                      CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    private Color getCellFillColor(CellState state) {
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

        return Color.TRANSPARENT;
    }

    private Canvas canvas;
    private int width;
    private int height;
    private GraphicsContext draw;
    final private int CELL_SIZE;
}
