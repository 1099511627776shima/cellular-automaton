package gui.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 29.11.16.
 */
public class CreateNewAutomatonStageController implements Initializable {
    @FXML
    private ComboBox<String> automatonTypeComboBox;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private ComboBox<String> neighbourhoodTypeComboBox;
    @FXML
    private TextField neighbourhoodRadiusTextField;
    @FXML
    private ToggleButton quadLifeToggle;
    @FXML
    private ToggleButton wrappingToggle;
    @FXML
    private Button createBtn;
    @FXML
    private Button cancelBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        automatonTypeComboBox.getItems().addAll(
                "Game of Life"
        );
        neighbourhoodTypeComboBox.getItems().addAll(
                "Von Neumann Neighbourhood",
                "Moore Neighbourhood"
        );

        setUIElementsListners();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMasterController(MainStageController mainController) {
        this.mainController = mainController;
    }

    private void setUIElementsListners() {
        // Create button
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // FIXME check entered values
                mainController.createAutomaton(
                        automatonTypeComboBox.getValue(),
                        Integer.parseInt(widthTextField.getText()),
                        Integer.parseInt(heightTextField.getText()),
                        neighbourhoodTypeComboBox.getValue(),
                        Integer.parseInt(neighbourhoodRadiusTextField.getText()),
                        quadLifeToggle.isSelected(),
                        wrappingToggle.isSelected()
                );
                stage.close();
            }
        });

        // Cancel button
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        // QuadLife toggle
        quadLifeToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(quadLifeToggle.isSelected()) quadLifeToggle.setText("Enabled");
                else quadLifeToggle.setText("Disabled");
            }
        });

        // Wrapping toggle
        wrappingToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(wrappingToggle.isSelected()) wrappingToggle.setText("Enabled");
                else wrappingToggle.setText("Disabled");
            }
        });
    }

    private Stage stage;
    private MainStageController mainController;
}
