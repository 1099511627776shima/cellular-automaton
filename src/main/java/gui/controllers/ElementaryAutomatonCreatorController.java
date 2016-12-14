package gui.controllers;

import automatons.Automaton;
import automatons.ElementaryAutomaton;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords1D;
import cells.neighbourhood.ElementaryNeighbourhood;
import cells.states.*;
import gui.AutomatonMode;
import gui.CreateErrorAlert;
import gui.eventhandlers.SimpleToggleEventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 03.12.16.
 */
public class ElementaryAutomatonCreatorController extends CreatorController implements Initializable {
    @Override
    public Automaton createAutomaton() {
        width = Integer.parseInt(numberOfCellsTextField.getText());
        height = 1;
        CellStateFactory cellStateFactory = new UniformStateFactory(BinaryState.DEAD);
        boolean wrapping = wrappingToggle.isSelected();

        if(initialPatternComboBox.getValue().equals(initialPatterns.get(0)))
            cellStateFactory = new UniformStateFactory(BinaryState.DEAD);
        if(initialPatternComboBox.getValue().equals(initialPatterns.get(1)))
            cellStateFactory = new UniformStateFactory(BinaryState.ALIVE);
        if(initialPatternComboBox.getValue().equals(initialPatterns.get(2))) {
            Map<CellCoordinates, CellState> singleCellStructure = new HashMap<>();
            singleCellStructure.put(new Coords1D((width-1)/2), BinaryState.ALIVE);
            cellStateFactory = new GeneralStateFactory(singleCellStructure, new UniformStateFactory(BinaryState.DEAD));
        }

        return new ElementaryAutomaton(
                cellStateFactory,
                new ElementaryNeighbourhood(width, wrapping),
                width,
                Integer.parseInt(ruleTextField.getText())
        );
    }

    @Override
    public AutomatonMode getMode() {
        return AutomatonMode.BINARY;
    }

    @Override
    public Alert inputIsValid() {
        CreateErrorAlert alert = new CreateErrorAlert();

        String number = "\\d+";
        if(!numberOfCellsTextField.getText().matches(number) || Integer.parseInt(numberOfCellsTextField.getText()) % 2 == 0)
            alert.addMessage("Number of cells must be an odd number between 1 and 400");
        if(ruleTextField.getText().equals("") || !ruleTextField.getText().matches(number))
            alert.addMessage("Rule must be specified as a number");
        else {
            int rule = Integer.parseInt(ruleTextField.getText());
            if(rule < 0 || rule > 255)
                alert.addMessage("Rule must be a number between 0 and 255");
        }
        if(initialPatternComboBox.getValue() == null)
            alert.addMessage("Initial pattern was not selected");

        return alert.getAlert();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialPatternComboBox.setItems(initialPatterns);

        setUIElementsListners();
    }

    private void setUIElementsListners() {
        wrappingToggle.setOnAction(new SimpleToggleEventHandler(wrappingToggle));
    }

    @FXML
    private TextField numberOfCellsTextField;

    @FXML
    private TextField ruleTextField;

    @FXML
    private ComboBox<String> initialPatternComboBox;

    @FXML
    private ToggleButton wrappingToggle;

    private ObservableList<String> initialPatterns = FXCollections.observableArrayList(
            "all cells dead",
            "all cells alive",
            "single alive cell in the middle"
    );
}
