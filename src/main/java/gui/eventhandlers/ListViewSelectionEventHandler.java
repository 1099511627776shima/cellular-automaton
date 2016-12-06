package gui.eventhandlers;

import gui.controllers.InsertStructureStageController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Created by bzdeco on 06.12.16.
 */
public class ListViewSelectionEventHandler implements EventHandler<MouseEvent> {
    public ListViewSelectionEventHandler(InsertStructureStageController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(MouseEvent event) {
        controller.changeStructureNameLabel(controller.getListView().getSelectionModel().getSelectedItem());
    }

    private InsertStructureStageController controller;
}
