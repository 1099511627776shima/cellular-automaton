package gui.controllers;

import automatons.Automaton;
import automatons.LangtonAnt;
import cells.states.*;
import gui.AutomatonMode;
import gui.CreateErrorAlert;
import gui.eventhandlers.SimpleToggleEventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 03.12.16.
 */
public class LangtonsAntCreatorController extends CreatorController implements Initializable {
    @Override
    public Automaton createAutomaton() {
        width = Integer.parseInt(widthTextField.getText());
        height = Integer.parseInt(heightTextField.getText());

        CellStateFactory cellStateFactory;

        if(initialStateComboBox.getValue().equals(initialStates.get(0))) {
            cellStateFactory = new UniformStateFactory(new LangtonCell(BinaryState.DEAD));
        }
        else {
            cellStateFactory = new UniformStateFactory(new LangtonCell(BinaryState.ALIVE));
        }

        return new LangtonAnt(
                cellStateFactory,
                width,
                height
        );
    }

    @Override
    public AutomatonMode getMode() {
        return AutomatonMode.ANT;
    }

    @Override
    public Alert inputIsValid() {
        CreateErrorAlert alert = new CreateErrorAlert();

        String number = "\\d+";
        if(!widthTextField.getText().matches(number))
            alert.addMessage("Width must be a positive number");
        if(!heightTextField.getText().matches(number))
            alert.addMessage("Height must be a positive number");
        if(initialStateComboBox.getValue() == null)
            alert.addMessage("Initial cell state was not selected");

        return alert.getAlert();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialStateComboBox.setItems(initialStates);
    }


    @FXML private GridPane settings;

    @FXML private TextField widthTextField;

    @FXML private TextField heightTextField;

    @FXML private ComboBox<String> initialStateComboBox;

    private ObservableList<String> initialStates = FXCollections.observableArrayList(
            "dead",
            "alive"
    );
}
