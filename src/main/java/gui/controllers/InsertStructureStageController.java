package gui.controllers;

import gui.eventhandlers.ListViewSelectionEventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 06.12.16.
 */
public class InsertStructureStageController implements Initializable, Controller {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        structuresList.getItems().addAll(structuresNames);

        setUIElementsListners();
    }

    public ListView<String> getListView() {
        return structuresList;
    }

    public void changeStructureNameLabel(String newLabel) {
        structureName.setText(newLabel);
    }

    private void setUIElementsListners() {
        structuresList.setOnMouseClicked(new ListViewSelectionEventHandler(this));
    }

    @FXML private Canvas structureCanvas;
    @FXML private Label structureName;
    @FXML private ListView<String> structuresList;
    private ObservableList<String> structuresNames = FXCollections.observableArrayList(
            "Glider",
            "Gosper Glider Gun",
            "Lightweight spaceship",
            "Beacon",
            "Blinker",
            "Toad",
            "Pulsar",
            "Block",
            "Boat",
            "Beehive",
            "Loaf",
            "Pond",
            "Tub",
            "Small Exploder",
            "Exploder",
            "Crocodile",
            "Tumbler/Fountain",
            "The R-pentonimo",
            "Diehard",
            "Acorn",
            "Garden of Eden",
            "Smallest immortal",
            "5x5 immortal"
    );
}
