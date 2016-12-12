package gui;

import cells.states.*;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bzdeco on 07.12.16.
 */
public class CellStateColor {
    public static Color get(CellState state) {

        if(state instanceof LangtonCell) {
            LangtonCell langtonCell = (LangtonCell)state;

            if(langtonCell.cellState.equals(BinaryState.ALIVE))
                return Color.BLACK;
            else
                return Color.WHITE;
        }
        else
            return colorsForStates.get(state);
    }

    private static Map<CellState, Color> initializeColorsForStates() {
        Map<CellState, Color> result = new HashMap<>();

        result.put(BinaryState.ALIVE, Color.BLACK);
        result.put(BinaryState.DEAD, Color.WHITE);
        result.put(QuadState.GREEN, Color.GREEN);
        result.put(QuadState.RED, Color.RED);
        result.put(QuadState.YELLOW, Color.YELLOW);
        result.put(QuadState.BLUE, Color.BLUE);
        result.put(QuadState.DEAD, Color.WHITE);
        result.put(WireElectronState.VOID, Color.BLACK);
        result.put(WireElectronState.WIRE, Color.LAWNGREEN);
        result.put(WireElectronState.ELECTRON_HEAD, new Color(0, 128/255, 1, 1));
        result.put(WireElectronState.ELECTRON_TAIL, new Color(1, 64/255, 0, 1));

        return result;
    }

    private static Map<CellState, Color> colorsForStates = initializeColorsForStates();
}
