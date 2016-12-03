package gui.eventhandlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

/**
 * Created by bzdeco on 03.12.16.
 */
public class SimpleToggleEventHandler implements EventHandler<ActionEvent> {
    public SimpleToggleEventHandler(ToggleButton toggle) {
        this.toggle = toggle;
    }

    @Override
    public void handle(ActionEvent event) {
        if(toggle.isSelected()) toggle.setText("Enabled");
        else toggle.setText("Disabled");
    }

    private ToggleButton toggle;
}
