package gui;

import cells.states.*;
import javafx.event.EventHandler;
import javafx.scene.Parent;
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
        disable();

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
            case "binary":
                selectedState = BinaryState.DEAD;
                break;
            case "ant":
                selectedState = new LangtonCell(BinaryState.DEAD);
                break;
        }

        next = 0;
    }

    private void initStateColors() {
        stateColors = new HashMap<>();

        ArrayList<Pair<CellState, Color>> binary = new ArrayList<>();
        binary.add(new Pair<>(BinaryState.DEAD, CellStateColor.get(BinaryState.DEAD)));
        binary.add(new Pair<>(BinaryState.ALIVE, CellStateColor.get(BinaryState.ALIVE)));
        stateColors.put("binary", binary);

        ArrayList<Pair<CellState, Color>> ant = new ArrayList<>();
        ant.add(new Pair<>(new LangtonCell(BinaryState.DEAD), CellStateColor.get(BinaryState.DEAD)));
        ant.add(new Pair<>(new LangtonCell(BinaryState.ALIVE), CellStateColor.get(BinaryState.ALIVE)));
        stateColors.put("ant", ant);

        ArrayList<Pair<CellState, Color>> quad = new ArrayList<>();
        quad.add(new Pair<>(QuadState.DEAD, CellStateColor.get(QuadState.DEAD)));
        quad.add(new Pair<>(QuadState.RED, CellStateColor.get(QuadState.RED)));
        quad.add(new Pair<>(QuadState.GREEN, CellStateColor.get(QuadState.GREEN)));
        quad.add(new Pair<>(QuadState.YELLOW, CellStateColor.get(QuadState.YELLOW)));
        quad.add(new Pair<>(QuadState.BLUE, CellStateColor.get(QuadState.BLUE)));
        stateColors.put("quad", quad);

        ArrayList<Pair<CellState, Color>> wireworld = new ArrayList<>();
        wireworld.add(new Pair<>(WireElectronState.VOID, CellStateColor.get(WireElectronState.VOID)));
        wireworld.add(new Pair<>(WireElectronState.WIRE, CellStateColor.get(WireElectronState.WIRE)));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_HEAD, CellStateColor.get(WireElectronState.ELECTRON_HEAD)));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_TAIL, CellStateColor.get(WireElectronState.ELECTRON_TAIL)));
        stateColors.put("wireworld", wireworld);
    }

    private Rectangle statePickerRect;
    private CellState selectedState;
    private String currentMode;
    private int next;
    /** Map assigning particular automaton currentMode to map of possible cell states and corresponding colors */
    private Map<String, ArrayList<Pair<CellState, Color>>> stateColors;
}
