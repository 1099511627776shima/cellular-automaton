package gui.controllers;

import automatons.Automaton;
import automatons.ElementaryAutomaton;
import cells.Cell;
import cells.states.*;
import gui.AutomatonDisplay;
import gui.AutomatonDisplay1D;
import gui.AutomatonDisplay2D;
import gui.eventhandlers.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Time;
import java.util.*;

public class MainStageController implements Initializable, Controller {
    final int CELL_SIZE = 20;
    final int HISTORY_LIMIT = 5;
    final double FRAME_DURATION = 25; // in milliseconds

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

    private Group automatonGroup;
    private AutomatonDisplay automatonDisplay;
    private Scene main;
    private Stage createNewAutomaton;
    private Stage insertStructure;
    private Timeline simulationTimeline;

    private Automaton currentAutomaton;

    IntegerProperty generation = new SimpleIntegerProperty(0);
    IntegerProperty liveCells = new SimpleIntegerProperty(0);

    private ArrayList<Automaton> previousStates = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUIElementsListners();
    }

    public void setStage(Scene main, Stage createNewAutomaton, Stage insertStructure) {
        this.main = main;
        this.createNewAutomaton = createNewAutomaton;
        this.insertStructure = insertStructure;
    }

    /**
     * Creates currentAutomaton according to given parameters and displays it
     */
    public void createAutomaton(Automaton automaton, int width, int height) {
        this.currentAutomaton = automaton;

        // Setting up canvas for drawing and displaying board
        if(automaton instanceof ElementaryAutomaton) {
            automatonDisplay = new AutomatonDisplay1D(automaton, width, CELL_SIZE);
        }
        else
            automatonDisplay = new AutomatonDisplay2D(automaton, width, height, CELL_SIZE);

        // Attaching Canvas to Group for panning and zooming
        automatonGroup = new Group(automatonDisplay.getCanvas());
        // Delete if there was any automaton displayed earlier
        zoomableRegion.getChildren().clear();
        scrollableRegion.setContent(createZoomableAutomatonBoard(automatonGroup));

        // Positions canvas in the center of the viewport
        zoomableRegion.setMinSize(
                scrollableRegion.viewportBoundsProperty().get().getWidth(),
                scrollableRegion.viewportBoundsProperty().get().getHeight()
        );

        // Set drawing parameters and draw board (with no cells)
        automatonDisplay.setDrawParameters(Color.GRAY, 2);
        automatonDisplay.drawBoard();

        // Display currentAutomaton
        //displayAutomaton(currentAutomaton);
        automatonDisplay.display();

        // Set UI elements to track changes in currentAutomaton
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
                main.setCursor(Cursor.CLOSED_HAND);

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
                main.setCursor(Cursor.DEFAULT);
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
        liveCells.setValue(calculateLiveCellsValue());
        liveCellsLabel.setText("Live cells: " + liveCells.get());
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

        // Run simulation
        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_DURATION), new TimelineEventHandler(this)));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
        simulationRun.setOnAction(new SimulationRunEventHandler(this));
        simulationRun.setAccelerator(KeyCombination.keyCombination("R"));
        simulationRunBtn.setOnAction(new SimulationRunEventHandler(this));

        // Pause simulation
        simulationPause.setOnAction(new SimulationPauseEventHandler(this));
        simulationPause.setAccelerator(KeyCombination.keyCombination("P"));
        simulationPauseBtn.setOnAction(new SimulationPauseEventHandler(this));

        // New
        newBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createNewAutomaton.show();
            }
        });

        // Insert
        insertBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                insertStructure.show();
            }
        });
    }

    /**
     * Displays current currentAutomaton's state
     * @param currentAutomaton Automaton to be displayed in the viewport
     */
    /* private void displayAutomaton(Automaton currentAutomaton) {
        Automaton.CellIterator iterator = currentAutomaton.cellIterator();

        while(iterator.hasNext()) {
            automatonDisplay.updateCell(iterator.next());
        }
    } */

    /**
     * Similar to @see MainStageController#displayAutomaton(), but only redraws cells, if their state changed
     * @param automaton Automaton to be updated and displayed in the viewport
     * @return Born/died balance - how cell population changed since the last currentAutomaton's state
     */
    /* private int updateAutomatonDisplay(Automaton currentAutomaton) {
        Automaton.CellIterator iterator = currentAutomaton.cellIterator();
        // [OPTIMIZATION] Get the latest currentAutomaton state to iterate through to compere if cell state change and needs to be updated
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
    } */

    private boolean isAlive(Cell cell) {
        CellState state = cell.getState();
        if(state == BinaryState.ALIVE || state == QuadState.BLUE || state == QuadState.GREEN || state == QuadState.RED || state == QuadState.YELLOW)
            return true;
        else
            return false;
    }

    /**
     * Counts all of the living cells (used when currentAutomaton is initialized and in @see MainStageController#stepBackward(), NOT during the simulation)
     * @return Returns the number of living cells in the currentAutomaton
     */
    private int calculateLiveCellsValue() {
        Automaton.CellIterator iterator = currentAutomaton.cellIterator();

        int aliveCellsCount = 0;
        while(iterator.hasNext()) {
            if(isAlive(iterator.next())) {
                aliveCellsCount++;
            }
        }

        return aliveCellsCount;
    }

    /**
     * Calculates and display the next state (next generation) of the currentAutomaton
     */
    public void stepForward() {
        previousStates.add(currentAutomaton); // Add current currentAutomaton to previous states
        if(previousStates.size() > HISTORY_LIMIT)
            previousStates.remove(0);
        automatonDisplay.updateDisplayHistory(); // Memorize recent state for further stepping backwards
        currentAutomaton = currentAutomaton.nextState(); // Set next currentAutomaton state to be current state
        automatonDisplay.updateAutomaton(currentAutomaton); // Set new current state to be current satate for display
        generation.setValue(generation.get() + 1); // Increment generation

        automatonDisplay.display(); // Display current currentAutomaton
    }

    /**
     * Loads and displays the previous state (older generation) of the currentAutomaton.
     * This function is working very slowly and may highly influence app performance.
     */
    public void stepBackward() {
        // If we can go backwards
        if(generation.get() > 0 && previousStates.size() > 0) {
            currentAutomaton = previousStates.remove(previousStates.size()-1);

            automatonDisplay.updateAutomaton(currentAutomaton);
            generation.setValue(generation.get() - 1); // Decrement generation

            automatonDisplay.retrieveFromDisplayHistoryAndDisplay(); // Display previous currentAutomaton
        }
        else {
            System.out.println("Cannot go backwards");
        }
    }

    public void runSimulation() {
        simulationTimeline.play();
    }

    public void pauseSimulation() {
        simulationTimeline.stop();
    }
}
