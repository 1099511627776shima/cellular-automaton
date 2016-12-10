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
                ArrayList<StateColorPair> availableColors = stateColors.get(currentMode);
                next++;
                StateColorPair pair =  availableColors.get(next % availableColors.size());
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
            default:
                selectedState = BinaryState.DEAD;
                break;
        }

        next = 0;
    }

    private void initStateColors() {
        stateColors = new HashMap<>();

        ArrayList<StateColorPair> binary = new ArrayList<>();
        binary.add(new StateColorPair(BinaryState.DEAD, Color.WHITE));
        binary.add(new StateColorPair(BinaryState.ALIVE, Color.BLACK));
        stateColors.put("binary", binary);

        ArrayList<StateColorPair> quad = new ArrayList<>();
        quad.add(new StateColorPair(QuadState.DEAD, Color.WHITE));
        quad.add(new StateColorPair(QuadState.RED, Color.RED));
        quad.add(new StateColorPair(QuadState.GREEN, Color.GREEN));
        quad.add(new StateColorPair(QuadState.YELLOW, Color.YELLOW));
        quad.add(new StateColorPair(QuadState.BLUE, Color.BLUE));
        stateColors.put("quad", quad);

        ArrayList<StateColorPair> wireworld = new ArrayList<>();
        wireworld.add(new StateColorPair(WireElectronState.VOID, Color.BLACK));
        wireworld.add(new StateColorPair(WireElectronState.WIRE, Color.LAWNGREEN));
        wireworld.add(new StateColorPair(WireElectronState.ELECTRON_HEAD, new Color(0, 128/255, 1, 1)));
        wireworld.add(new StateColorPair(WireElectronState.ELECTRON_TAIL, new Color(1, 64/255, 0, 1)));
        stateColors.put("wireworld", wireworld);
    }

    private Rectangle statePickerRect;
    private CellState selectedState;
    private String currentMode;
    private int next;
    /** Map assigning particular automaton currentMode to map of possible cell states and corresponding colors */
    private Map<String, ArrayList<StateColorPair>> stateColors;
}
