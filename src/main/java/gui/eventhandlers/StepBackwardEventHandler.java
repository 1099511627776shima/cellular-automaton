package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;

public class StepBackwardEventHandler extends ComponentEventHandler {
    public StepBackwardEventHandler(MainStageController controller) {
        super(controller);
    }
    @Override
    public void handle(ActionEvent event) {
        controller.stepBackward();
    }
}
