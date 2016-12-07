package gui.eventhandlers;

import cells.states.BinaryState;
import gui.BinaryStructures;
import gui.Structure;
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
        String selectedStructureName = controller.getListView().getSelectionModel().getSelectedItem();
        Structure selectedStructure = BinaryStructures.getStructure(selectedStructureName.toLowerCase());

        controller.displaySelectedStructure(selectedStructure);
    }

    private InsertStructureStageController controller;
}
