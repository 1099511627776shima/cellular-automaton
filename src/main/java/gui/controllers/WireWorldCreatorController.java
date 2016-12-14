package gui.controllers;

import automatons.Automaton;
import automatons.WireWorld;
import cells.states.CellStateFactory;
import cells.states.UniformStateFactory;
import cells.states.WireElectronState;
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
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 30.11.16.
 */
public class WireWorldCreatorController extends CreatorController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialCircuitComboBox.setItems(initialCircuit);

        setUIElementsListners();
    }

    @Override
    public Automaton createAutomaton() {
        width = Integer.parseInt(widthTextField.getText());
        height = Integer.parseInt(heightTextField.getText());

        CellStateFactory cellStateFactory;
        boolean wrapping = wrappingToggle.isSelected();

        if(initialCircuitComboBox.getValue().equals(initialCircuit.get(0))) {
            cellStateFactory = new UniformStateFactory(WireElectronState.VOID);
        }
        else {
            cellStateFactory = new UniformStateFactory(WireElectronState.WIRE);
        }

        return new WireWorld(
                cellStateFactory,
                width,
                height,
                wrapping
        );
    }

    @Override
    public AutomatonMode getMode() {
        return AutomatonMode.WIREWORLD;
    }

    @Override
    public Alert inputIsValid() {
        CreateErrorAlert alert = new CreateErrorAlert();

        String number = "\\d+";
        if(!widthTextField.getText().matches(number))
            alert.addMessage("Width must be a number between 1 and 400");
        if(!heightTextField.getText().matches(number))
            alert.addMessage("Height must be a number between 1 and 400");
        if(initialCircuitComboBox.getValue() == null)
            alert.addMessage("Initial state was not selected");

        return alert.getAlert();
    }

    private void setUIElementsListners() {
        wrappingToggle.setOnAction(new SimpleToggleEventHandler(wrappingToggle));
    }

    @FXML
    private GridPane settings;

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private ComboBox<String> initialCircuitComboBox;

    @FXML
    private ToggleButton wrappingToggle;

    private ObservableList<String> initialCircuit = FXCollections.observableArrayList(
            "everything is empty",
            "everything is a conductor"
    );
}
