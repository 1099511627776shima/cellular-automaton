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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineJoin;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {
    final int CELL_SIZE = 20;

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
    private AnchorPane displayAnchor;

    @FXML
    private ScrollPane scrollableRegion;

    @FXML
    private Group scrollContent;

    @FXML
    private StackPane zoomableRegion;

    private Canvas displayCanvas;
    private Scene scene;

    private Automaton automaton;
    private CellStateFactory factory;
    private CellNeighbourhood neighbourhood;
    private int width;
    private int height;
    private int radius;
    private boolean wrapping;

    private Group automatonBoard;
    private ArrayList<Automaton> previousStates = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        width = 100;
        height = 100;
        radius = 1;
        wrapping = false;
        factory = new UniformStateFactory(BinaryState.DEAD);
        neighbourhood = new MooreNeighbourhood(width, height, radius, wrapping);

        // Drawing board
        displayCanvas = new Canvas();
        displayCanvas.setWidth(width*CELL_SIZE);
        displayCanvas.setHeight(height*CELL_SIZE);

        Group automatonDisplay = new Group();
        automatonDisplay.getChildren().add(displayCanvas);
        scrollableRegion.setContent(createZoomableAutomatonBoard(automatonDisplay));

        displayCanvas.getGraphicsContext2D().setStroke(Color.BLACK);
        displayCanvas.getGraphicsContext2D().setLineWidth(5);
        displayCanvas.getGraphicsContext2D().strokeRect(10, 10, width*CELL_SIZE - 20, height*CELL_SIZE - 20);

        automaton = new GameOfLife(factory, neighbourhood, width, height, "23/3", false);
        previousStates.add(automaton.copy()); // clear automaton

        simulationForward.setOnAction(new StepForwardEventHandler(this));
        simulationForward.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));

        // Testing
        automaton.insertStructure(new Structure().glider);
        //updateDisplay();
    }

    public void setStage(Scene scene) {
        this.scene = scene;
    }

    /* createZoomableAutomatonBoard(), figureScrollOffset() and repositionScroller() thanks to solution
     * on StackOverflow provided by jewelsea on http://stackoverflow.com/questions/16680295/javafx-correct-scaling
     * and zkristic's bug fix
     */

    private Parent createZoomableAutomatonBoard(final Group automaton) {
        final double SCALE_DELTA = 1.1;
        zoomableRegion.getChildren().add(automaton);

        scrollableRegion.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomableRegion.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        zoomableRegion.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

                // amount of scrolling in each direction in scrollContent coordinate
                // units
                Point2D scrollOffset = figureScrollOffset(scrollContent, scrollableRegion);

                automaton.setScaleX(automaton.getScaleX() * scaleFactor);
                automaton.setScaleY(automaton.getScaleY() * scaleFactor);

                // move viewport so that old center remains in the center after the
                // scaling
                repositionScroller(scrollContent, scrollableRegion, scaleFactor, scrollOffset);
            }
        });

        // Panning
        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            }
        });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.CLOSED_HAND);

                double deltaX = event.getX() - lastMouseCoordinates.get().getX();
                double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollableRegion.getViewportBounds().getWidth();
                double deltaH = deltaX * (scrollableRegion.getHmax() - scrollableRegion.getHmin()) / extraWidth;
                double desiredH = (Double.isNaN(scrollableRegion.getHvalue()) ? 0 : scrollableRegion.getHvalue()) - deltaH;
                scrollableRegion.setHvalue(Math.max(0, Math.min(scrollableRegion.getHmax(), desiredH)));

                double deltaY = event.getY() - lastMouseCoordinates.get().getY();
                double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollableRegion.getViewportBounds().getHeight();
                double deltaV = deltaY * (scrollableRegion.getVmax() - scrollableRegion.getVmin()) / extraHeight;
                double desiredV = (Double.isNaN(scrollableRegion.getVvalue()) ? 0 : scrollableRegion.getVvalue()) - deltaV;
                scrollableRegion.setVvalue(Math.max(0, Math.min(scrollableRegion.getVmax(), desiredV)));
            }
        });

        scrollContent.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                scene.setCursor(Cursor.DEFAULT);
            }
        });

        return scrollContent;
    }

    private Point2D figureScrollOffset(Node scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = ((Double.isNaN(scroller.getHvalue()) ? 0 : scroller.getHvalue()) - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = ((Double.isNaN(scroller.getVvalue()) ? 0 : scroller.getVvalue()) - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
    }

    private void repositionScroller(Node scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
            double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
            double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
            scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
            double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
            double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
            scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
            scroller.setHvalue(scroller.getHmin());
        }
    }

    private SVGPath createBoard() {
        SVGPath board = new SVGPath();
        String path = new String();

        for(int y = 0; y <= height; y++) {
            path += "M0," + y * CELL_SIZE + " l" + width * CELL_SIZE + ",0 ";
        }

        for(int x = 0; x <= width; x++) {
            path += "M" + x * CELL_SIZE + ",0 l0," + height * CELL_SIZE + " ";
        }

        board.setContent(path);
        board.setStroke(Color.BLACK);
        board.setStrokeWidth(1);

        return board;
    }

    private void createEmptyCells() {
        SVGPath cell;
        ObservableList<Node> nodes = automatonBoard.getChildren();

        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cell = new SVGPath();
                cell.setContent("M" + (x*CELL_SIZE+1) + "," + (y*CELL_SIZE+1) + " l" + (CELL_SIZE-2) + ",0" + " l0," + (CELL_SIZE-2) +
                                " l" + (-(CELL_SIZE-2)) + ",0 l0," + (-(CELL_SIZE-2)));
                cell.setFill(Color.WHITE);
                System.out.println(cell.getContent());
                nodes.add(cell);
            }
        }
    }

    private SVGPath fillWithBackgroundColor(Color color) {
        SVGPath background = new SVGPath();
        background.setContent("M0,0 l" + width*CELL_SIZE + ",0 l0," + height*CELL_SIZE + " l" + (-width*CELL_SIZE) + ",0 l0," + (-height*CELL_SIZE));
        background.setFill(color);

        return background;
    }

    private void updateDisplay() {
        Automaton.CellIterator iterator = automaton.cellIterator();
        Automaton.CellIterator previousIterator = previousStates.get(previousStates.size()-1).cellIterator();

        Cell currentCell, oldCell;
        Coords2D coords;

        while(iterator.hasNext()) {
            currentCell = iterator.next();
            oldCell = previousIterator.next();

            if(!currentCell.equals(oldCell)) {
                coords = (Coords2D) currentCell.getCoords();
                SVGPath cellPath = drawCellPath(coords.getX(), coords.getY());
                cellPath.setFill(fillCellState(currentCell.getState()));

                automatonBoard.getChildren().add(cellPath);
            }
        }
    }

    private SVGPath drawCellPath(int x, int y) {
        SVGPath cellPath = new SVGPath();
        cellPath.setContent("M" + (x * CELL_SIZE + 0.5) + "," + (y * CELL_SIZE + 0.5) +
                " l" + (CELL_SIZE - 1) + ",0" + " l0," + (CELL_SIZE - 1) +
                " l" + (-(CELL_SIZE - 1)) + ",0 " + "l0," + (-(CELL_SIZE - 1)));
        cellPath.setStrokeWidth(0);
        return cellPath;
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
        previousStates.add(automaton);
        automaton = automaton.nextState();
        updateDisplay();
    }
}
