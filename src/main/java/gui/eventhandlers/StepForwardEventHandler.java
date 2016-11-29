package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class StepForwardEventHandler implements EventHandler<ActionEvent> {
    public StepForwardEventHandler(MainStageController controller) {
        this.controller = controller;
    }
    @Override
    public void handle(ActionEvent event) {
        controller.stepForward();
    }

    private MainStageController controller;
}
