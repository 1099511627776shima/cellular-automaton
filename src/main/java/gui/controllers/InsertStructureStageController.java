package gui.controllers;

import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.CellState;
import gui.BinaryStructures;
import gui.CellStateColor;
import gui.Structure;
import gui.eventhandlers.ListViewSelectionEventHandler;
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
        structuresList.getItems().addAll(structuresNames);

        width = 270;
        height = 270;

        setupNewCanvas();

        setUIElementsListners();
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

    public void displaySelectedStructure(Structure structure) {
        setStructureNameLabel(structure.getName().substring(0,1).toUpperCase() + structure.getName().substring(1));
        setStructureDescriptionLabel(structure.getDescription());

        // Create display on Canvas
        int size;
        if(structure.getWidth() <= 9) {
            if(structure.getWidth() % 2 == 0)
                size = 8;
            else
                size = 9;
        }
        else
            size = structure.getWidth();

        canvasAnchorPane.getChildren().remove(structureCanvas);
        setupNewCanvas();
        drawBoard(size);
        displayStructureCells(structure.getPositionedStructure((size - 1)/2, (size - 1)/2));
    }

    private void setupNewCanvas() {
        structureCanvas = new Canvas(width, height);
        canvasAnchorPane.getChildren().add(structureCanvas);
        draw = structureCanvas.getGraphicsContext2D();
        draw.setStroke(Color.GRAY);
        draw.setFill(Color.WHITE);
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
        draw.fillRect(0, 0, width, height);

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

    private void setUIElementsListners() {
        structuresList.setOnMouseClicked(new ListViewSelectionEventHandler(this));
        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainController.turnOnInsertMode(BinaryStructures.getStructure(structuresList.getSelectionModel().getSelectedItem().toLowerCase()));
                stage.close();
            }
        });
    }

    private Stage stage;
    private MainStageController mainController;
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
