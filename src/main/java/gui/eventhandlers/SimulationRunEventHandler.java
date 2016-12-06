package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;

/**
 * Created by bzdeco on 06.12.16.
 */
public class SimulationRunEventHandler extends ComponentEventHandler {
    public SimulationRunEventHandler(MainStageController controller) {
        super(controller);
    }

    @Override
    public void handle(ActionEvent event) {
        controller.runSimulation();
    }
}
