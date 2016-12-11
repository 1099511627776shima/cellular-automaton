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
 * Created by bzdeco on 11.12.16.
 */
public class AntPicker {
    public AntPicker(AnchorPane antPickerPane, int cellSize) {
        this.antPickerPane = antPickerPane;
        this.cellSize = cellSize;
        initAntsDisplay();
        next = 0;
        antPickerPane.getChildren().add(0, antsDisplay.get(next % antsDisplay.size()).getValue());
        selectedAntState = AntState.NORTH;

        antPickerPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                changeAnt();
            }
        });
    }

    public AntState getAnt() {
        return selectedAntState;
    }

    public int getNewID() {
        return ID++;
    }

    public void disable() {
        antPickerPane.disableProperty().setValue(true);
    }

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
