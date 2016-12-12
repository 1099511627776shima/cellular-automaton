package gui.eventhandlers;

import gui.controllers.MainStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by bzdeco on 06.12.16.
 */
public abstract class MainStageEventHandler implements EventHandler<ActionEvent> {
    public MainStageEventHandler(MainStageController controller) {
        this.controller = controller;
    }

    protected MainStageController controller;
}
