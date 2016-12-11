package gui;

import cells.states.BinaryState;
import cells.states.CellState;
import cells.states.QuadState;
import cells.states.WireElectronState;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bzdeco on 09.12.16.
 */
public class StatePicker {
    public StatePicker(Rectangle statePickerRect, String mode) {
        this.statePickerRect = statePickerRect;
        initStateColors();
        changeMode(mode);
        statePickerRect.setFill(Color.WHITE);
        enable();

        // Setting up color change
        statePickerRect.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ArrayList<Pair<CellState, Color>> availableColors = stateColors.get(currentMode);
                next++;
                Pair<CellState, Color> pair =  availableColors.get(next % availableColors.size());
                selectedState = pair.getKey();
                statePickerRect.setFill(pair.getValue());
            }
        });
    }

    public CellState getState() {
        return selectedState;
    }

    public void enable() {
        statePickerRect.disableProperty().setValue(false);
    }

    public void disable() {
        statePickerRect.disableProperty().setValue(true);
    }

    public void changeMode(String mode) {
        this.currentMode = mode;

        switch (mode) {
            case "quad":
                selectedState = QuadState.DEAD;
                break;
            case "wireworld":
                selectedState = WireElectronState.VOID;
                break;
            // "binary" and "ant"
            default:
                selectedState = BinaryState.DEAD;
                break;
        }

        next = 0;
    }

    private void initStateColors() {
        stateColors = new HashMap<>();

        ArrayList<Pair<CellState, Color>> binary = new ArrayList<>();
        binary.add(new Pair<>(BinaryState.DEAD, Color.WHITE));
        binary.add(new Pair<>(BinaryState.ALIVE, Color.BLACK));
        stateColors.put("binary", binary);
        stateColors.put("ant", binary);

        ArrayList<Pair<CellState, Color>> quad = new ArrayList<>();
        quad.add(new Pair<>(QuadState.DEAD, Color.WHITE));
        quad.add(new Pair<>(QuadState.RED, Color.RED));
        quad.add(new Pair<>(QuadState.GREEN, Color.GREEN));
        quad.add(new Pair<>(QuadState.YELLOW, Color.YELLOW));
        quad.add(new Pair<>(QuadState.BLUE, Color.BLUE));
        stateColors.put("quad", quad);

        ArrayList<Pair<CellState, Color>> wireworld = new ArrayList<>();
        wireworld.add(new Pair<>(WireElectronState.VOID, Color.BLACK));
        wireworld.add(new Pair<>(WireElectronState.WIRE, Color.LAWNGREEN));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_HEAD, new Color(0, 128/255, 1, 1)));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_TAIL, new Color(1, 64/255, 0, 1)));
        stateColors.put("wireworld", wireworld);
    }

    private Rectangle statePickerRect;
    private CellState selectedState;
    private String currentMode;
    private int next;
    /** Map assigning particular automaton currentMode to map of possible cell states and corresponding colors */
    private Map<String, ArrayList<Pair<CellState, Color>>> stateColors;
}
