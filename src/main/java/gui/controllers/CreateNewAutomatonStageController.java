package gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the class used to setup the tool for creating new automaton. It loads proper settings according to the chosen automaton type.
 */
public class CreateNewAutomatonStageController implements Initializable, Controller {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automatonTypeComboBox.getItems().addAll(automatonTypes);
        createBtn.setDisable(true);

        setUIElementsListeners();
    }

    /**
     * Adds access to the stage controlled by this controller.
     *
     * @param stage create new automaton stage created in {@link gui.Main}
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Adds access to the main stage controller.
     *
     * @param mainController controller of the main stage of the application
     */
    public void setMasterController(MainStageController mainController) {
        this.mainController = mainController;
    }

    private CreatorController loadSettings(String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        settingsAnchor.getChildren().setAll((Node)loader.load());

        return loader.getController();
    }

    private void setUIElementsListeners() {
        // Loading different settings options
        automatonTypeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event){
                String type = automatonTypeComboBox.getValue();

                try {
                    // Elementary Automaton
                    if (type.equals(automatonTypes[0])) {
                        automatonCreatorController = loadSettings("settings-elementary-automaton.fxml");
                    }

                    // Game Of Life
                    else if (type.equals(automatonTypes[1])) {
                        automatonCreatorController = loadSettings("settings-game-of-life.fxml");
                    }

                    // Langton's Ant
                    else if (type.equals(automatonTypes[2])) {
                        automatonCreatorController = loadSettings("settings-langtons-ant.fxml");
                    }

                    // Wire World
                    else {
                        automatonCreatorController = loadSettings("settings-wireworld.fxml");
                    }

                    createBtn.setDisable(false);
                }
                catch (IOException exc) {
                    System.out.println("Could not load settings for " + type);
                }

                // Resizing content of settingsAnchor to maximum size
                settingsAnchor.setTopAnchor(settingsAnchor.getChildren().get(0), 0.);
                settingsAnchor.setBottomAnchor(settingsAnchor.getChildren().get(0), 0.);
                settingsAnchor.setRightAnchor(settingsAnchor.getChildren().get(0), 0.);
                settingsAnchor.setLeftAnchor(settingsAnchor.getChildren().get(0), 0.);
            }
        });

        // Create button
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = automatonCreatorController.inputIsValid();
                if(alert == null) {
                    mainController.createAutomaton(automatonCreatorController.createAutomaton(), automatonCreatorController.getWidth(), automatonCreatorController.getHeight(), automatonCreatorController.getMode());
                    stage.close();
                }
                else {
                    alert.showAndWait();
                    event.consume();
                }
            }
        });

        // Cancel button
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
    }

    private Stage stage;
    private MainStageController mainController;
    private CreatorController automatonCreatorController;
    @FXML
    private ComboBox<String> automatonTypeComboBox;
    @FXML
    private AnchorPane settingsAnchor;
    @FXML
    private Button createBtn;
    @FXML
    private Button cancelBtn;

    private String[] automatonTypes = {
            "Elementary Automaton",
            "Game of Life",
            "Langton's Ant",
            "Wire World"
    };
}
