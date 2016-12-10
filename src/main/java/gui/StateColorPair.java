package gui;

import cells.states.CellState;
import javafx.scene.paint.Color;

/**
 * Created by bzdeco on 09.12.16.
 */
public class StateColorPair {
    public StateColorPair(CellState state, Color color) {
        this.state = state;
        this.color = color;
    }

    public CellState getKey() {
        return state;
    }

    public Color getValue() {
        return color;
    }

    private CellState state;
    private Color color;
}
