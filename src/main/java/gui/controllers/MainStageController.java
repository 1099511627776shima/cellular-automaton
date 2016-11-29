package gui.controllers;

import automatons.Automaton;
import automatons.GameOfLife;
import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.neighbourhood.CellNeighbourhood;
import cells.neighbourhood.MooreNeighbourhood;
import cells.neighbourhood.VonNeumannNeighbourhood;
import cells.states.*;
import gui.AutomatonDisplay;
import gui.Structures;
import gui.eventhandlers.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.*;
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

public class MainStageController implements Initializable {
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
    private Button newBtn;
    @FXML
    private Button insertBtn;
    @FXML
    private Button simulationRunBtn;
    @FXML
    private Button simulationPauseBtn;
    @FXML
    private Button simulationResetBtn;
    @FXML
    private Button simulationForwardBtn;
    @FXML
    private Button simulationBackwardBtn;
    @FXML
    private Label generationLabel;
    @FXML
    private Label liveCellsLabel;

    @FXML
    private ScrollPane scrollableRegion;

    @FXML
    private Group scrollContent;

    @FXML
    private StackPane zoomableRegion;

    private AutomatonDisplay automatonDisplay;
    private Scene scene;

    private Automaton automaton;
    private CellStateFactory factory;
    private CellNeighbourhood neighbourhood;
    private int width;
    private int height;
    private int radius;
    private boolean wrapping;

    IntegerProperty generation = new SimpleIntegerProperty(0);
    IntegerProperty liveCells = new SimpleIntegerProperty(0);

    private ArrayList<Automaton> previousStates = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Insert structures
        //automaton.insertStructure(Structures.GLIDER);

        setUIElementsListners();
    }

    public void setStage(Scene scene) {
        this.scene = scene;
    }

    /**
     * Creates automaton according to given parameters and displays it
     */
    public void createAutomaton(String automatonType, int width, int height, String neighbourhoodType, int neighbourhoodRadius, boolean quadLifeEnabled, boolean wrappingEnabled) {
        // Creating neighbourhood strategy
        CellNeighbourhood neighbourhoodStrategy = null;
        if(neighbourhoodType.equals("Von Neumann Neighbourhood")) neighbourhoodStrategy = new VonNeumannNeighbourhood(width, height, neighbourhoodRadius, wrappingEnabled);
        else if(neighbourhoodType.equals("Moore Neighbourhood")) neighbourhoodStrategy = new MooreNeighbourhood(width, height, neighbourhoodRadius, wrappingEnabled);

        // Game of Life
        if(automatonType.equals("Game of Life")) {
            automaton = new GameOfLife(new UniformStateFactory(BinaryState.DEAD), neighbourhoodStrategy, width, height, "23/3", quadLifeEnabled); // FIXME hardcoded rule
        }

        // Setting up canvas for drawing and displaying board
        automatonDisplay = new AutomatonDisplay(width, height, CELL_SIZE);

        // Attaching Canvas to Group for panning and zooming
        Group automatonGroup = new Group(automatonDisplay.getCanvas());
        scrollableRegion.setContent(createZoomableAutomatonBoard(automatonGroup));

        // Positions canvas in the center of the viewport
        zoomableRegion.setMinSize(
                scrollableRegion.viewportBoundsProperty().get().getWidth(),
                scrollableRegion.viewportBoundsProperty().get().getHeight()
        );

        // Set drawing parameters and draw board (with no cells)
        automatonDisplay.setDrawParameters(Color.BLACK, 2);
        automatonDisplay.drawBoard();

        // Display automaton
        displayAutomaton(automaton);

        // Set UI elements to track changes in automaton
        setUIElementsBindings();
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

    private void setUIElementsBindings() {
        // Generation
        generation.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                generationLabel.setText("Generation: " + newValue);
            }
        });
        generationLabel.setText("Generation: " + generation.get());

        // Live cells
        liveCells.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                liveCellsLabel.setText("Live cells: " + newValue);
            }
        });
        liveCellsLabel.setText("Live cells: " + calculateLiveCellsValue());
    }

    private void setUIElementsListners() {
        // Step Forward
        simulationForward.setOnAction(new StepForwardEventHandler(this));
        simulationForward.setAccelerator(KeyCombination.keyCombination("K"));
        simulationForwardBtn.setOnAction(new StepForwardEventHandler(this));

        // Step Backward
        simulationBackward.setOnAction(new StepBackwardEventHandler(this));
        simulationBackward.setAccelerator(KeyCombination.keyCombination("J"));
        simulationBackwardBtn.setOnAction(new StepBackwardEventHandler(this));
    }

    /**
     * Displays current automaton's state
     * @param automaton Automaton to be displayed in the viewport
     */
    private void displayAutomaton(Automaton automaton) {
        Automaton.CellIterator iterator = automaton.cellIterator();

        while(iterator.hasNext()) {
            automatonDisplay.updateCell(iterator.next());
        }
    }

    /**
     * Similar to @see MainStageController#displayAutomaton(), but only redraws cells, if their state changed
     * @param automaton Automaton to be updated and displayed in the viewport
     * @return Born/died balance - how cell population changed since last automaton's state
     */
    private int updateAutomatonDisplay(Automaton automaton) {
        Automaton.CellIterator iterator = automaton.cellIterator();
        // [OPTIMIZATION] Get the latest automaton state to iterate through to compere if cell state change and needs to be updated
        // [OPTIMIZATION] Update live cells value with balance instead of counting all alive cells on board
        Automaton.CellIterator oldIterator = previousStates.get(previousStates.size()-1).cellIterator();
        Cell currentCell, oldCell;
        int bornDiedBalance = 0;

        while(iterator.hasNext() && oldIterator.hasNext()) {
            currentCell = iterator.next();
            oldCell = oldIterator.next();

            if(!currentCell.equals(oldCell)) {
                automatonDisplay.updateCell(currentCell);
                if(!isAlive(oldCell) && isAlive(currentCell)) bornDiedBalance++;
                else if(isAlive(oldCell) && !isAlive(currentCell)) bornDiedBalance--;
            }
        }

        return bornDiedBalance;
    }

    private boolean isAlive(Cell cell) {
        CellState state = cell.getState();
        if(state == BinaryState.ALIVE || state == QuadState.BLUE || state == QuadState.GREEN || state == QuadState.RED || state == QuadState.YELLOW)
            return true;
        else
            return false;
    }

    /**
     * Counts all of the living cells (used when automaton is initialized and in @see MainStageController#stepBackward(), NOT during the simulation)
     * @return Returns the number of living cells in the automaton
     */
    private int calculateLiveCellsValue() {
        Automaton.CellIterator iterator = automaton.cellIterator();

        int aliveCellsCount = 0;
        while(iterator.hasNext()) {
            if(isAlive(iterator.next())) {
                aliveCellsCount++;
            }
        }

        return aliveCellsCount;
    }

    /**
     * Calculates and display the next state (next generation) of the automaton
     */
    public void stepForward() {
        previousStates.add(automaton);
        automaton = automaton.nextState();
        int bornDiedBalance = updateAutomatonDisplay(automaton);
        generation.setValue(generation.get() + 1);
        liveCells.setValue(liveCells.get() + bornDiedBalance);
    }

    /**
     * Loads and displays the previous state (older generation) of the automaton
     */
    public void stepBackward() {
        if(previousStates.size() > 1) automaton = previousStates.remove(previousStates.size()-1);
        else automaton = previousStates.get(0); // initial state
        displayAutomaton(automaton);
        if(generation.get() > 0) generation.setValue(generation.get() - 1);
        liveCells.setValue(calculateLiveCellsValue());
    }
}
