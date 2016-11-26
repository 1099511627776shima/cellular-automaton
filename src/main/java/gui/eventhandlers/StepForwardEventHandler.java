package gui.eventhandlers;

import gui.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class StepForwardEventHandler implements EventHandler<ActionEvent> {
    public StepForwardEventHandler(Controller controller) {
        this.controller = controller;
    }
    @Override
    public void handle(ActionEvent event) {
        controller.stepForward();
    }

    private Controller controller;
}
