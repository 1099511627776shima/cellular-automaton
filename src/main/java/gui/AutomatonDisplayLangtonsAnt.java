package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords2D;
import cells.states.AntState;
import cells.states.LangtonCell;
import javafx.scene.paint.Color;

/**
 * This is a concrete class used to display Langton's Ant automatons. It varies from the regular two-dimensional automaton because of the implementation of the Langton cell state.
 *
 * @see cells.states.LangtonCell
 */
public class AutomatonDisplayLangtonsAnt extends AutomatonDisplay2D {
    public AutomatonDisplayLangtonsAnt(Automaton automaton, int width, int height, int cellSize) {
        super(automaton, width, height, cellSize);
    }

    @Override
    protected void updateCell(Cell cell) {
        Coords2D coords = (Coords2D) cell.getCoords();

        draw.setFill(CellStateColor.get(cell.getState()));

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + coords.getY()*CELL_SIZE,
                CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);

        LangtonCell langtonCell = (LangtonCell)cell.getState();
        if(langtonCell.hasAnt()) {
            // Coordinates for polygons to be drawn as ants
            double[] verticalXPoints = {coords.getX()*CELL_SIZE+offset, coords.getX()*CELL_SIZE + CELL_SIZE/2, (coords.getX()+1)*CELL_SIZE-offset};
            double[] horizontalYPoints = {coords.getY()*CELL_SIZE+offset, coords.getY()*CELL_SIZE + CELL_SIZE/2, (coords.getY()+1)*CELL_SIZE-offset};
            double[] northYPoints = {(coords.getY()+1)*CELL_SIZE-offset, coords.getY()*CELL_SIZE+offset, (coords.getY()+1)*CELL_SIZE-offset};
            double[] eastXPoints = {coords.getX()*CELL_SIZE+offset, (coords.getX()+1)*CELL_SIZE-offset, coords.getX()*CELL_SIZE+offset};
            double[] southYPoints = {coords.getY()*CELL_SIZE+offset, (coords.getY()+1)*CELL_SIZE-offset, coords.getY()*CELL_SIZE+offset};
            double[] westXPoints = {(coords.getX()+1)*CELL_SIZE-offset, coords.getX()*CELL_SIZE+offset, (coords.getX()+1)*CELL_SIZE-offset};

            draw.setFill(Color.ORANGE);
            for(AntState ant : langtonCell.antStates.values()) {
                switch(ant) {
                    case NORTH:
                        draw.fillPolygon(verticalXPoints, northYPoints, 3);
                        break;
                    case EAST:
                        draw.fillPolygon(eastXPoints, horizontalYPoints, 3);
                        break;
                    case SOUTH:
                        draw.fillPolygon(verticalXPoints, southYPoints, 3);
                        break;
                    case WEST:
                        draw.fillPolygon(westXPoints, horizontalYPoints, 3);
                        break;
                }
            }
        }
    }
}
