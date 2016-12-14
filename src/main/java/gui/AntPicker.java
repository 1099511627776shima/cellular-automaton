package gui;

import cells.states.AntState;
import cells.states.LangtonCell;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the tool for inserting ants into Langton's Ant automaton. It allows user to choose state of the added ant.
 */
public class AntPicker {
    /**
     * Sole constructor of this class creating new ant picker from given JavaFX AnchorPane which will display ant state that will be inserted in the next use of add ant tool.
     *
     * @param antPickerPane JavaFX AnchorPane used to display currently selected ant state that can be inserted into automaton. The state can be change by clicking on the active (not disabled) ant picker pane.
     * @param cellSize size of the cell in the displayed automaton where the ant will be inserted
     */
    public AntPicker(AnchorPane antPickerPane, int cellSize) {
        // Clearing old content of ant picker
        if(antPickerPane.getChildren().size() > 0) antPickerPane.getChildren().remove(0);
        this.antPickerPane = antPickerPane;
        this.cellSize = cellSize;
        initAntsDisplay();
        next = 0;
        antPickerPane.getChildren().setAll(antsDisplay.get(next % antsDisplay.size()).getValue());
        selectedAntState = AntState.NORTH;
        disable();

        antPickerPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeAnt();
            }
        });
    }

    /**
     * Get the currently selected ant state.
     *
     * @return currently selected ant state represented by a triangle pointing in the one of four geographical directions
     */
    public AntState getAnt() {
        return selectedAntState;
    }

    /**
     * Get the new ID for the added ant
     *
     * @return unused ID fot the added ant that will be associated with it
     */
    public int getNewID() {
        return ID++;
    }

    /**
     * Disables the ant picker's pane to make it inaccessible for user. It happens when the user leaves add ant mode or creates the new automaton, when all insert modes are disabled.
     */
    public void disable() {
        antPickerPane.disableProperty().setValue(true);
    }

    /**
     * Enables the ant picker's pane to be accessible for user. It happens when the user enters add ant mode.
     */
    public void enable() {
        antPickerPane.disableProperty().setValue(false);
    }

    private void changeAnt() {
        next++;
        antPickerPane.getChildren().remove(0);
        antPickerPane.getChildren().add(0, antsDisplay.get(next % antsDisplay.size()).getValue());
        selectedAntState = antsDisplay.get(next % antsDisplay.size()).getKey();
    }

    private void initAntsDisplay() {
        antsDisplay = new ArrayList<>();

        SVGPath north = new SVGPath();
        north.setFill(Color.ORANGE);
        north.setContent("M0," + cellSize + " l" + cellSize/2 + "," + (-cellSize) + " l" + cellSize/2 + "," + cellSize + " l" + (-cellSize) + ",0");
        antsDisplay.add(new Pair<>(AntState.NORTH, north));

        SVGPath east = new SVGPath();
        east.setFill(Color.ORANGE);
        east.setContent("M0,0 l" + cellSize + "," + cellSize/2 + " l" + (-cellSize) + "," + cellSize/2 + " l0," + (-cellSize));
        antsDisplay.add(new Pair<>(AntState.EAST, east));

        SVGPath south = new SVGPath();
        south.setFill(Color.ORANGE);
        south.setContent("M0,0 l" + cellSize + ",0 l" + (-cellSize/2) + "," + cellSize + " l" + (-cellSize/2) + "," + (-cellSize));
        antsDisplay.add(new Pair<>(AntState.SOUTH, south));

        SVGPath west = new SVGPath();
        west.setFill(Color.ORANGE);
        west.setContent("M" + cellSize + ",0 l0," + cellSize + " l" + (-cellSize) + "," + (-cellSize/2) + " l" + cellSize + "," + (-cellSize/2));
        antsDisplay.add(new Pair<>(AntState.WEST, west));
    }

    private AnchorPane antPickerPane;
    private int cellSize;
    private int next;
    private int ID;
    private ArrayList<Pair<AntState, SVGPath>> antsDisplay;
    private AntState selectedAntState;
}
