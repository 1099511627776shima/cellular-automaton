package gui;

import automatons.Automaton;
import cells.Cell;
import cells.coordinates.Coords2D;
import cells.states.AntState;
import cells.states.LangtonCell;
import javafx.scene.paint.Color;

/**
 * Created by bzdeco on 11.12.16.
 */
public class AutomatonDisplayLangtonsAnt extends AutomatonDisplay2D {
    public AutomatonDisplayLangtonsAnt(Automaton automaton, int width, int height, int cellSize) {
        super(automaton, width, height, cellSize);
    }

    @Override
    protected void updateCell(Cell cell) {
        Coords2D coords = (Coords2D) cell.getCoords();

        draw.setFill(CellStateColor.get(cell.getState()));

        LangtonCell langtonCell = (LangtonCell)cell.getState();
        if(langtonCell.hasAnt()) {
            for(AntState ant : langtonCell.antStates.values()) {
                draw.setFill(Color.ORANGE);
            }
        }

        draw.fillRect(offset + coords.getX()*CELL_SIZE, offset + coords.getY()*CELL_SIZE,
                CELL_SIZE - 2*offset, CELL_SIZE - 2*offset);


    }
}
