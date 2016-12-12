package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;

public class StepForwardEventHandler extends MainStageEventHandler {
    public StepForwardEventHandler(MainStageController controller) {
        super(controller);
    }
    @Override
    public void handle(ActionEvent event) {
        controller.stepForward();
    }
}
