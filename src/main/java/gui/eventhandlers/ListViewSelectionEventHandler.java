package gui.eventhandlers;

import gui.Structures;
import gui.Structure;
import gui.controllers.InsertStructureStageController;
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
        String selectedStructureName = controller.getListView().getSelectionModel().getSelectedItem();
        Structure selectedStructure = Structures.getStructure(selectedStructureName.toLowerCase(), controller.isQuadLifeEnabled());

        controller.displaySelectedStructure(selectedStructure);
    }

    private InsertStructureStageController controller;
}
