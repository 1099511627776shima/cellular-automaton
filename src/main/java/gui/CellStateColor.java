package gui;

import cells.states.*;
import javafx.scene.paint.Color;

/**
 * Created by bzdeco on 07.12.16.
 */
public class CellStateColor {
    public static Color get(CellState state) {
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
            return Color.BLACK;
        }
        else if(state.equals(WireElectronState.WIRE)) {
            return Color.LAWNGREEN;
        }
        else if(state.equals(WireElectronState.ELECTRON_HEAD)) {
            return new Color(0, 128/255, 1, 1);
        }
        else if(state.equals(WireElectronState.ELECTRON_TAIL)) {
            return new Color(1, 64/255, 0, 1);
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
}
