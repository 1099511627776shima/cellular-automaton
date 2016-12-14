package gui;

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
     * Sole constructor of this class creating new state picker from given JavaFX Rectangle which will display cell state that will be inserted in the next use of manual insert tool.
     *
     * @param statePickerRect JavaFX Rectangle used to display currently selected cell state that can be inserted into automaton. The state can be change by clicking on the active (not disabled) state picker rectangle.
     * @param mode automaton type, which provides information about the possible cell states
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

    /**
     * Gets currently selected cell state. Used when inserting a cell in manual insert mode.
     *
     * @return cell state that is selected and which color is displayed in the UI.
     */
    public CellState getState() {
        return selectedState;
    }

    /**
     * Enables the state picker's rectangle to be accessible for user. It happens when the user enters manual insert mode.
     */
    public void enable() {
        statePickerRect.disableProperty().setValue(false);
    }

    /**
     * Disables the state picker's rectangle to make it inaccessible for user. It happens when the user leaves manual insert mode or creates the new automaton, when all insert modes are disabled.
     */
    public void disable() {
        statePickerRect.disableProperty().setValue(true);
    }

    /**
     * Sets mode to given automaton mode. This mode must correspond with the actual mode of the displayed automaton.
     * @param mode mode which associated cell states should be displayed and accessible through state picker
     */
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
    private Map<AutomatonMode, ArrayList<Pair<CellState, Color>>> stateColors;
}
