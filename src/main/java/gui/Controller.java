package gui;

import automatons.Automaton;
import automatons.GameOfLife;
import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.MooreNeighbourhood;
import cells.states.*;
import gui.eventhandlers.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu menuFile;

    @FXML
    private MenuItem fileExit;

    @FXML
    private Menu menuEdit;

    @FXML
    private MenuItem editInsert;

    @FXML
    private Menu menuEditInsert;

    @FXML
    private MenuItem insertGlider;

    @FXML
    private Menu menuSimulation;

    @FXML
    private MenuItem simulationRun;

    @FXML
    private MenuItem simulationPause;

    @FXML
    private MenuItem simulationRestart;

    @FXML
    private MenuItem simulationForward;

    @FXML
    private MenuItem simulationBackward;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem helpAbout;

    @FXML
    private Canvas displayCanvas;

    private GraphicsContext gc;

    private Automaton automaton;

    private CellStateFactory factory;

    private CellNeighbourhood neighbourhood;

    private int width;

    private int height;

    private int radius;

    private boolean wrapping;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        width = 20;
        height = 20;
        radius = 1;
        wrapping = false;
        factory = new UniformStateFactory(BinaryState.DEAD);
        neighbourhood = new MooreNeighbourhood(width, height, radius, wrapping);

        automaton = new GameOfLife(factory, neighbourhood, width, height, "23/3", false);

        gc = displayCanvas.getGraphicsContext2D();
        simulationForward.setOnAction(new StepForwardEventHandler(this));
        simulationForward.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));

        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(1);

        automaton.insertStructure((new Structure()).glider);
        displayAutomaton();


    }

    public void displayAutomaton() {
        int cellSize = 20;
        Automaton.CellIterator iterator = automaton.cellIterator();

        Cell currentCell;
        Coords2D cellCoords;
        while(iterator.hasNext()) {
            currentCell = iterator.next();
            cellCoords = (Coords2D)currentCell.getCoords();
            gc.strokeRect(cellCoords.getX() * cellSize, cellCoords.getY() * cellSize, cellSize, cellSize);
            gc.setFill(fillCellState(currentCell.getState()));
            gc.fillRect(cellCoords.getX() * cellSize + 1, cellCoords.getY() * cellSize + 1, cellSize - 2, cellSize - 2);
        }
    }

    public Color fillCellState(CellState state) {
        if(state.equals(BinaryState.ALIVE)) {
            return Color.BLACK;
        }
        else if(state.equals(BinaryState.DEAD)) {
            return Color.WHITE;
        }
        else if(state.equals(QuadState.GREEN)) {
            return Color.GREEN;
        }
        else if(state.equals(QuadState.YELLOW)) {
            return Color.YELLOW;
        }
        else if(state.equals(QuadState.BLUE)) {
            return Color.BLUE;
        }
        else if(state.equals(QuadState.RED)) {
            return Color.RED;
        }

        return Color.TRANSPARENT;
    }

    public void stepForward() {
        automaton = automaton.nextState();
        displayAutomaton();
    }
}
