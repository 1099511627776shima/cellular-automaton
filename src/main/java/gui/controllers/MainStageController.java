package gui.controllers;

import automatons.Automaton;
import automatons.ElementaryAutomaton;
import automatons.LangtonAnt;
import cells.Cell;
import cells.coordinates.CellCoordinates;
import cells.coordinates.Coords2D;
import cells.states.*;
import gui.*;
import gui.eventhandlers.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class MainStageController implements Initializable, Controller {
    final int CELL_SIZE = 20;
    final int HISTORY_LIMIT = 5;

    @FXML private BorderPane window;

    @FXML private MenuBar menuBar;

    @FXML private Menu menuFile;

    @FXML private MenuItem fileExit;

    @FXML private Menu menuEdit;

    @FXML private MenuItem editInsertLibrary;

    @FXML private CheckMenuItem editManualInsertCheck;

    @FXML
    private CheckMenuItem editAddAntCheck;

    @FXML private Menu menuSimulation;

    @FXML private MenuItem simulationRun;

    @FXML private MenuItem simulationPause;

    @FXML private MenuItem simulationForward;

    @FXML private MenuItem simulationBackward;

    @FXML private Menu menuHelp;

    @FXML private MenuItem helpAbout;

    @FXML private Label generationValueLabel;

    @FXML private Label liveCellsValueLabel;

    @FXML private AnchorPane displayAnchor;

    @FXML private Button newBtn;

    @FXML private Button insertBtn;

    @FXML private Button simulationRunBtn;

    @FXML private Button simulationPauseBtn;

    @FXML private Button simulationForwardBtn;

    @FXML private Button simulationBackwardBtn;

    @FXML private ScrollPane scrollableRegion;

    @FXML private Group scrollContent;

    @FXML private StackPane zoomableRegion;

    @FXML private Label insertModeLabel;

    @FXML private Rectangle statePickerRect;

    @FXML private AnchorPane antPickerPane;

    @FXML private TextField stepTextField;

    @FXML private HBox antPickerBox;

    private Group automatonGroup;
    private AutomatonDisplay automatonDisplay;
    private Scene main;
    private Stage createNewAutomaton;
    private Stage insertStructure;
    private InsertStructureStageController insertStructureController;
    private Timeline simulationTimeline;
    private StatePicker statePicker;
    private AntPicker antPicker;

    private Automaton currentAutomaton;
    private Structure structureToInsert;

    private IntegerProperty generation = new SimpleIntegerProperty(0);
    private IntegerProperty liveCells = new SimpleIntegerProperty(0);
    private IntegerProperty frameDuration = new SimpleIntegerProperty(250); // in milliseconds
    private BooleanProperty insertModeEnabled = new SimpleBooleanProperty(false);
    private BooleanProperty manualInsertModeEnabled = new SimpleBooleanProperty(false);
    private BooleanProperty addAntModeEnabled = new SimpleBooleanProperty(false);
    private String mode;

    private ArrayList<Automaton> previousStates = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setUIEventHandlers();
    }

    public void setStage(Scene main, Stage createNewAutomaton, Stage insertStructure, InsertStructureStageController insertStructureController) {
        this.main = main;
        this.createNewAutomaton = createNewAutomaton;
        this.insertStructure = insertStructure;
        this.insertStructureController = insertStructureController;
    }

    public String getCurrentAutomatonMode() {
        return mode;
    }

    /**
     * Creates currentAutomaton according to given parameters and displays it
     */
    void createAutomaton(Automaton automaton, int width, int height, String mode) {
        this.currentAutomaton = automaton;
        this.mode = mode;
        generation.setValue(0);

        // Setup StatePicker
        statePicker = new StatePicker(statePickerRect, mode);
        if(!manualInsertModeEnabled.getValue()) statePicker.disable();

        // Setup UI for ant mode
        if(mode.equals("ant")) {
            // Ant Picker
            antPicker = new AntPicker(antPickerPane, CELL_SIZE);
            antPickerBox.setVisible(true);
            antPickerBox.disableProperty().setValue(false);
            if(!addAntModeEnabled.getValue()) antPicker.disable();

            // Add ant menu option
            editAddAntCheck.disableProperty().setValue(false);
        }
        else {
            antPickerBox.setVisible(false);
            antPickerBox.disableProperty().setValue(true);

            // Disable add ant mode (also deselects Edit > Add ant)
            addAntModeEnabled.setValue(false);

            // Disable Edit > Add ant
            editAddAntCheck.disableProperty().setValue(true);
        }

        // Setting up canvas for drawing and displaying board
        if(automaton instanceof ElementaryAutomaton) {
            automatonDisplay = new AutomatonDisplay1D(automaton, width, CELL_SIZE);
        }
        else if(automaton instanceof LangtonAnt) {
            automatonDisplay = new AutomatonDisplayLangtonsAnt(automaton, width, height, CELL_SIZE);
        }
        else
            automatonDisplay = new AutomatonDisplay2D(automaton, width, height, CELL_SIZE);

        if(insertModeEnabled.getValue()) automatonDisplay.getCanvas().setCursor(Cursor.CROSSHAIR);
        setAutomatonDisplayMouseClickDetection();

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

                // amount of scrolling in each direction in scrollContent coordinate units
                Point2D scrollOffset = figureScrollOffset(scrollContent, scrollableRegion);

                automaton.setScaleX(automaton.getScaleX() * scaleFactor);
                automaton.setScaleY(automaton.getScaleY() * scaleFactor);

                // move viewport so that old center remains in the center after the scaling
                repositionScroller(scrollContent, scrollableRegion, scaleFactor, scrollOffset);
            }
        });

        final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
            }
        });

        // Panning
        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.isMiddleButtonDown()) {
                    // Disable insert mode (both manual and normal) when dragging detected
                    manualInsertModeEnabled.setValue(false);

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
                generationValueLabel.setText("" + newValue);
            }
        });

        // Live cells
        liveCells.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                liveCellsValueLabel.setText("" + newValue);
            }
        });
        liveCells.setValue(calculateLiveCellsValue());

        // Insert mode
        insertModeEnabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    automatonDisplay.getCanvas().setCursor(Cursor.CROSSHAIR);
                }
                else {
                    automatonDisplay.getCanvas().setCursor(Cursor.DEFAULT);
                }
            }
        });

        // Manual insert mode
        manualInsertModeEnabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    insertModeEnabled.setValue(true);
                    insertModeLabel.setText("Insert Mode: ON");
                    statePicker.enable();
                    editManualInsertCheck.setSelected(true);
                }
                else {
                    insertModeEnabled.setValue(false);
                    insertModeLabel.setText("Insert Mode: OFF");
                    statePicker.disable();
                    editManualInsertCheck.setSelected(false);
                }
            }
        });

        // Add ant mode
        addAntModeEnabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    antPicker.enable();
                    editAddAntCheck.setSelected(true);
                    automatonDisplay.getCanvas().setCursor(Cursor.CROSSHAIR);
                }
                else {
                    antPicker.disable();
                    editAddAntCheck.setSelected(false);
                    automatonDisplay.getCanvas().setCursor(Cursor.DEFAULT);
                }
            }
        });

        // Step text field
        stepTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Check if its a number
                if(newValue.matches("\\d+")) frameDuration.setValue(Integer.parseInt(newValue));
            }
        });

        // Frame duration
        frameDuration.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                simulationTimeline.stop();
                simulationTimeline = new Timeline(new KeyFrame(Duration.millis(newValue.doubleValue()), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        stepForward();
                    }
                }));
                simulationTimeline.setCycleCount(Timeline.INDEFINITE);
            }
        });
        // For first timeline setup
        simulationTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration.get()), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stepForward();
            }
        }));
        simulationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setUIEventHandlers() {
        // Step Forward
        simulationForward.setOnAction(new StepForwardEventHandler(this));
        simulationForward.setAccelerator(KeyCombination.keyCombination("K"));
        simulationForwardBtn.setOnAction(new StepForwardEventHandler(this));

        // Step Backward
        simulationBackward.setOnAction(new StepBackwardEventHandler(this));
        simulationBackward.setAccelerator(KeyCombination.keyCombination("J"));
        simulationBackwardBtn.setOnAction(new StepBackwardEventHandler(this));

        // Run simulation
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
                if(currentAutomaton != null) {
                    insertStructure.show();
                    insertStructureController.configureStructureListOnWindowDisplay();
                }
            }
        });

        // Manual insert mode
        editManualInsertCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // selected
                if(editManualInsertCheck.isSelected()) {
                    manualInsertModeEnabled.setValue(true);
                }
                // deselected
                else {
                    manualInsertModeEnabled.setValue(false);
                }
            }
        });

        // Add ant mode
        editAddAntCheck.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(editAddAntCheck.isSelected()) {
                    addAntModeEnabled.setValue(true);
                }
                else {
                    addAntModeEnabled.setValue(false);
                }
            }
        });
    }

    private void setAutomatonDisplayMouseClickDetection() {

        // Clicking on displayed automaton when insert mode is enabled
        automatonDisplay.getCanvas().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Get mouse click coords
                int clickedCellXCoord = (int) (event.getX() / CELL_SIZE);
                int clickedCellYCoord = (int) (event.getY() / CELL_SIZE);

                // If we are in insert mode
                if(insertModeEnabled.getValue()) {
                    if(manualInsertModeEnabled.getValue()) {
                        Map<Coords2D, CellState> singleCell = new HashMap<>();
                        singleCell.put(new Coords2D(0, 0), statePicker.getState());
                        structureToInsert = new Structure("", "", singleCell, 1, 1);
                    }

                    currentAutomaton.insertStructure(structureToInsert.getPositionedStructure(
                            clickedCellXCoord,
                            clickedCellYCoord
                    ));
                    automatonDisplay.updateAutomaton(currentAutomaton);
                    automatonDisplay.display();

                    // Disable insert possibility if structure was inserted
                    if(!manualInsertModeEnabled.getValue()) insertModeEnabled.setValue(false);
                }
                // If we are in add ant mode
                if(addAntModeEnabled.getValue()) {
                    Automaton.CellIterator iterator = currentAutomaton.cellIterator();
                    Coords2D clickedCoords = new Coords2D(clickedCellXCoord, clickedCellYCoord);
                    Map<CellCoordinates, CellState> newLangtonCell = new HashMap<>();

                    Cell currentCell;
                    Coords2D coords2D;
                    LangtonCell state;
                    LangtonCell newState = null;
                    // Find the clicked cell in automaton
                    while(iterator.hasNext()) {
                        currentCell = iterator.next();
                        coords2D = (Coords2D)currentCell.getCoords();
                        state = (LangtonCell)currentCell.getState();
                        if(coords2D.equals(clickedCoords)) {
                            // Update it's state
                            newState = new LangtonCell(state.cellState);
                            newState.antStates.putAll(state.antStates);
                            newState.addAnt(antPicker.getNewID(), antPicker.getAnt());
                        }
                    }

                    newLangtonCell.put(clickedCoords, newState);
                    currentAutomaton.insertStructure(newLangtonCell);
                    automatonDisplay.updateAutomaton(currentAutomaton);
                    automatonDisplay.display();
                }
            }
        });

        // Clicking anywhere else than canvas with insert mode enabled
        window.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // If user clicked outside of automatonDisplay canvas
                // In manual insert it can be only turned off by user
                if(insertModeEnabled.getValue() && !manualInsertModeEnabled.getValue() && !(event.getPickResult().getIntersectedNode() instanceof Canvas)) {
                    // Turn off insert mode
                    insertModeEnabled.setValue(false);
                    structureToInsert = null;
                }
                else
                    event.consume();
            }
        });
    }

    public void turnOnInsertStructureMode(Structure insertedStructure) {
        structureToInsert = insertedStructure;
        // Disable manual insert mode if it was enabled
        manualInsertModeEnabled.setValue(false);
        insertModeEnabled.setValue(true);
    }

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
            // TODO add loger on bottom toolbar
            //System.out.println("Cannot go backwards");
        }
    }

    public void runSimulation() {
        simulationTimeline.play();
    }

    public void pauseSimulation() {
        simulationTimeline.stop();
    }
}
