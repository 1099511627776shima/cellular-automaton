package gui.controllers;

import automatons.Automaton;
import automatons.GameOfLife;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.MooreNeighbourhood;
import cells.neighbourhood.VonNeumannNeighbourhood;
import cells.states.BinaryState;
import cells.states.CellStateFactory;
import cells.states.QuadState;
import cells.states.UniformStateFactory;
import gui.AutomatonMode;
import gui.CreateErrorAlert;
import gui.eventhandlers.SimpleToggleEventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.*;

/**
 * Created by bzdeco on 30.11.16.
 */
public class GameOfLifeCreatorController extends CreatorController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        neighbourhoodTypeComboBox.setItems(neighbourhoodTypes);
        initialStateComboBox.setItems(initialState);

        setUIElementListners();
    }

    @Override
    public Automaton createAutomaton() {
        width = Integer.parseInt(widthTextField.getText());
        height = Integer.parseInt(heightTextField.getText());

        int neighbourhoodRadius = Integer.parseInt(neighbourhoodRadiusTextField.getText());
        Set<Integer> survivalConditions = new HashSet<>();
        Set<Integer> birthConditions = new HashSet<>();
        CellNeighbourhood cellNeighbourhood;
        CellStateFactory cellStateFactory = new UniformStateFactory(BinaryState.DEAD); // to silence protesting Intellij
        boolean quadLife = quadLifeToggle.isSelected();
        boolean wrapping = wrappingToggle.isSelected();

        Scanner conditionsScanner = new Scanner(survivesTextField.getText());
        while(conditionsScanner.hasNextInt())
            survivalConditions.add(conditionsScanner.nextInt());
        conditionsScanner = new Scanner(isBornTextField.getText());
        while(conditionsScanner.hasNextInt())
            birthConditions.add(conditionsScanner.nextInt());

        if(neighbourhoodTypeComboBox.getValue().equals(neighbourhoodTypes.get(0))) {
            cellNeighbourhood = new MooreNeighbourhood(width, height, neighbourhoodRadius, wrapping);
        }
        else {
            cellNeighbourhood = new VonNeumannNeighbourhood(width, height, neighbourhoodRadius, wrapping);
        }

        if(quadLife) {
            if(initialStateComboBox.getValue().equals(initialStateQuadLife.get(0)))
                cellStateFactory = new UniformStateFactory(QuadState.DEAD);
            else if(initialStateComboBox.getValue().equals(initialStateQuadLife.get(1)))
                cellStateFactory = new UniformStateFactory(QuadState.RED);
            else if(initialStateComboBox.getValue().equals(initialStateQuadLife.get(2)))
                cellStateFactory = new UniformStateFactory(QuadState.GREEN);
            else if(initialStateComboBox.getValue().equals(initialStateQuadLife.get(3)))
                cellStateFactory = new UniformStateFactory(QuadState.YELLOW);
            else if(initialStateComboBox.getValue().equals(initialStateQuadLife.get(4)))
                cellStateFactory = new UniformStateFactory(QuadState.BLUE);
        }
        else {
            if(initialStateComboBox.getValue().equals(initialState.get(0)))
                cellStateFactory = new UniformStateFactory(BinaryState.DEAD);
            else
                cellStateFactory = new UniformStateFactory(BinaryState.ALIVE);
        }

        return new GameOfLife(
                cellStateFactory,
                cellNeighbourhood,
                width,
                height,
                survivalConditions,
                birthConditions,
                quadLife
        );
    }

    @Override
    public AutomatonMode getMode() {
        if(quadLifeToggle.isSelected())
            return AutomatonMode.QUAD;
        else
            return AutomatonMode.BINARY;
    }

    public Alert inputIsValid() {
        CreateErrorAlert alert = new CreateErrorAlert();

        String number = "\\d+";
        String conditions = "[\\d+[\\s]*]+";
        if(!widthTextField.getText().matches(number))
            alert.addMessage("Width must be a positive number");
        if(!heightTextField.getText().matches(number))
            alert.addMessage("Height must be a positive number");
        if(neighbourhoodTypeComboBox.getValue() == null)
            alert.addMessage("Neighbourhood type was not selected");
        if(!neighbourhoodRadiusTextField.getText().matches(number))
            alert.addMessage("Neighbourhood radius must be a positive number");
        if(!survivesTextField.getText().matches(conditions) || !isBornTextField.getText().matches(conditions)) {
            alert.addMessage("Survival/birth conditions must be positive numbers");
            alert.addMessage("Conditions must be separated by spaces");
        }
        if(initialStateComboBox.getValue() == null)
            alert.addMessage("Initial cell state was not selected");

        return alert.getAlert();
    }

    private void setUIElementListners() {
        // Adding/removing initial state options if QuadLife is enabled/disabled
        // QuadLife toggle text change
        quadLifeToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(quadLifeToggle.isSelected()) {
                    quadLifeToggle.setText("Enabled");
                    initialStateComboBox.setItems(initialStateQuadLife);
                    // QuadLife makes sense only with Conway's GoL rules
                    survivesTextField.setText("2 3");
                    survivesTextField.disableProperty().setValue(true);
                    isBornTextField.setText("3");
                    isBornTextField.disableProperty().setValue(true);
                }
                else {
                    quadLifeToggle.setText("Disabled");
                    initialStateComboBox.setItems(initialState);
                    survivesTextField.disableProperty().setValue(false);
                    isBornTextField.disableProperty().setValue(false);
                }
            }
        });

        // Wrapping toggle text change
        wrappingToggle.setOnAction(new SimpleToggleEventHandler(wrappingToggle));

    }

    private ObservableList<String> neighbourhoodTypes = FXCollections.observableArrayList(
            "Moore Neighbourhood",
            "Von Neumann Neighbourhood"
    );

    private ObservableList<String> initialState = FXCollections.observableArrayList(
            "dead",
            "alive"
    );

    private ObservableList<String> initialStateQuadLife = FXCollections.observableArrayList(
            "dead",
            "alive (red)",
            "alive (green)",
            "alive (yellow)",
            "alive (blue)"
    );

    @FXML
    private TextField widthTextField;

    @FXML
    private TextField heightTextField;

    @FXML
    private ComboBox<String> neighbourhoodTypeComboBox;

    @FXML
    private ComboBox<String> initialStateComboBox;

    @FXML
    private TextField neighbourhoodRadiusTextField;

    @FXML
    private TextField survivesTextField;

    @FXML
    private TextField isBornTextField;

    @FXML
    private ToggleButton quadLifeToggle;

    @FXML
    private ToggleButton wrappingToggle;
}
