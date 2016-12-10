package gui.controllers;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.CellState;
import gui.Structures;
import gui.CellStateColor;
import gui.Structure;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by bzdeco on 06.12.16.
 */
public class InsertStructureStageController implements Initializable, Controller {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        width = 270;
        height = 270;

        setupNewCanvas();

        setUIElementsListeners();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMasterController(MainStageController mainController) {
        this.mainController = mainController;
    }

    public ListView<String> getListView() {
        return structuresList;
    }

    public void displaySelectedStructure() {
        setStructureNameLabel(selectedStructure.getName().substring(0,1).toUpperCase() + selectedStructure.getName().substring(1));
        setStructureDescriptionLabel(selectedStructure.getDescription());

        // Create display on Canvas
        int size;
        if(Math.max(selectedStructure.getWidth(), selectedStructure.getHeight()) <= 9) {
            if(selectedStructure.getWidth() % 2 == 0)
                size = 8;
            else
                size = 9;
        }
        else
            size = Math.max(selectedStructure.getWidth(),selectedStructure.getHeight());

        canvasAnchorPane.getChildren().remove(structureCanvas);
        setupNewCanvas();
        drawBoard(size);
        displayStructureCells(selectedStructure.getPositionedStructure((size - 1)/2, (size - 1)/2));
    }

    public void configureStructureListOnWindowDisplay() {
        mode = mainController.getCurrentAutomatonMode();

        structuresList.getItems().clear();
        if(mode.equals("wireworld")) {
            structuresList.getItems().addAll(structuresNamesWW);
        }
        else
            structuresList.getItems().addAll(structuresNamesGoL);
        structuresList.getSelectionModel().select(0);
        selectedStructure = Structures.getStructure(structuresList.getSelectionModel().getSelectedItem().toLowerCase(), mode);
        displaySelectedStructure();
    }

    private void setupNewCanvas() {
        structureCanvas = new Canvas(width, height);
        canvasAnchorPane.getChildren().add(structureCanvas);
        draw = structureCanvas.getGraphicsContext2D();
        draw.setStroke(Color.GRAY);
        draw.setLineWidth(2);
        offset = 1;
    }

    private void setStructureNameLabel(String newLabel) {
        structureName.setText(newLabel);
    }

    private void setStructureDescriptionLabel(String structureDescriptionLabel) {
        structureDescription.setText(structureDescriptionLabel);
    }

    private void displayStructureCells(Map<CellCoordinates, CellState> structure) {
        Coords2D coords2D;
        for(CellCoordinates coords : structure.keySet()) {
            coords2D = (Coords2D)coords;
            draw.setFill(CellStateColor.get(structure.get(coords)));
            draw.fillRect(offset + coords2D.getX() * cellSize,offset + coords2D.getY() * cellSize, cellSize - 2*offset, cellSize - 2*offset);
        }
    }

    private void drawBoard(int size) {
        cellSize = width / size;

        if(mode.equals("wireworld")) draw.setFill(Color.BLACK);
        else draw.setFill(Color.WHITE);

        // Background
        draw.fillRect(0, 0, width, height);

        // Grid
        for(int y = 0; y < size; y++) {
            draw.moveTo(0, y * cellSize);
            draw.lineTo(size * cellSize, y * cellSize);
            draw.stroke();
        }
        for(int x = 0; x <= size; x++) {
            draw.moveTo(x * cellSize, 0);
            draw.lineTo(x * cellSize, size * cellSize);
            draw.stroke();
        }
    }

    private void setUIElementsListeners() {
        structuresList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selectedStructureName = structuresList.getSelectionModel().getSelectedItem();
                selectedStructure = Structures.getStructure(selectedStructureName.toLowerCase(), mode);

                displaySelectedStructure();
            }
        });
        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.turnOnInsertStructureMode(Structures.getStructure(structuresList.getSelectionModel().getSelectedItem().toLowerCase(), mode));
                stage.close();
            }
        });
    }

    private Stage stage;
    private MainStageController mainController;
    private String mode;
    private Structure selectedStructure;

    private Canvas structureCanvas;
    @FXML private AnchorPane canvasAnchorPane;
    private double width;
    private double height;
    private double cellSize;
    private double offset;
    private GraphicsContext draw;
    @FXML private Label structureName;
    @FXML private Label structureDescription;
    @FXML private Button insertBtn;
    @FXML private ListView<String> structuresList;
    private ObservableList<String> structuresNamesGoL = FXCollections.observableArrayList(
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
            "Acorn"
    );
    private ObservableList<String> structuresNamesWW = FXCollections.observableArrayList(
            "AND",
            "OR",
            "XOR",
            "NAND",
            "NOT",
            "Clock"
    );
}
