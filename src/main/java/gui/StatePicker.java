package gui;

import automatons.Automaton;
import cells.states.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is the tool for manually inserting cells in the automaton. It allows user to choose cell state of the added cell.
 */
public class StatePicker {
    /**
     *
     * @param statePickerRect
     * @param mode
     */
    public StatePicker(Rectangle statePickerRect, AutomatonMode mode) {
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

    public void changeMode(AutomatonMode mode) {
        this.currentMode = mode;

        switch (mode) {
            case QUAD:
                selectedState = QuadState.DEAD;
                break;
            case WIREWORLD:
                selectedState = WireElectronState.VOID;
                break;
            case BINARY:
                selectedState = BinaryState.DEAD;
                break;
            case ANT:
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
        stateColors.put(AutomatonMode.BINARY, binary);

        ArrayList<Pair<CellState, Color>> ant = new ArrayList<>();
        ant.add(new Pair<>(new LangtonCell(BinaryState.DEAD), CellStateColor.get(BinaryState.DEAD)));
        ant.add(new Pair<>(new LangtonCell(BinaryState.ALIVE), CellStateColor.get(BinaryState.ALIVE)));
        stateColors.put(AutomatonMode.ANT, ant);

        ArrayList<Pair<CellState, Color>> quad = new ArrayList<>();
        quad.add(new Pair<>(QuadState.DEAD, CellStateColor.get(QuadState.DEAD)));
        quad.add(new Pair<>(QuadState.RED, CellStateColor.get(QuadState.RED)));
        quad.add(new Pair<>(QuadState.GREEN, CellStateColor.get(QuadState.GREEN)));
        quad.add(new Pair<>(QuadState.YELLOW, CellStateColor.get(QuadState.YELLOW)));
        quad.add(new Pair<>(QuadState.BLUE, CellStateColor.get(QuadState.BLUE)));
        stateColors.put(AutomatonMode.QUAD, quad);

        ArrayList<Pair<CellState, Color>> wireworld = new ArrayList<>();
        wireworld.add(new Pair<>(WireElectronState.VOID, CellStateColor.get(WireElectronState.VOID)));
        wireworld.add(new Pair<>(WireElectronState.WIRE, CellStateColor.get(WireElectronState.WIRE)));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_HEAD, CellStateColor.get(WireElectronState.ELECTRON_HEAD)));
        wireworld.add(new Pair<>(WireElectronState.ELECTRON_TAIL, CellStateColor.get(WireElectronState.ELECTRON_TAIL)));
        stateColors.put(AutomatonMode.WIREWORLD, wireworld);
    }

    private Rectangle statePickerRect;
    private CellState selectedState;
    private AutomatonMode currentMode;
    private int next;
    /** Map assigning particular automaton currentMode to map of possible cell states and corresponding colors */
    private Map<AutomatonMode, ArrayList<Pair<CellState, Color>>> stateColors;
}
