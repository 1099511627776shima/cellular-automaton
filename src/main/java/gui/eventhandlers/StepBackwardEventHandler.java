package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class StepBackwardEventHandler implements EventHandler<ActionEvent> {
    public StepBackwardEventHandler(MainStageController controller) {
        this.controller = controller;
    }
    @Override
    public void handle(ActionEvent event) {
        controller.stepBackward();
    }

    private MainStageController controller;
}
