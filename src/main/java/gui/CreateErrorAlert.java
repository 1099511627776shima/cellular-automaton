package gui;

import javafx.beans.NamedArg;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

/**
 * Created by bzdeco on 02.12.16.
 */
public class CreateErrorAlert {
    public CreateErrorAlert() {
        this.alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Create New Automaton - Error");
        alert.setHeaderText("Automaton parameters set incorrectly");
        alert.initModality(Modality.APPLICATION_MODAL);
    }

    public void addMessage(String msg) {
        content += msg + "\n";
    }

    public Alert getAlert() {
        if(content.isEmpty()) return null;
        else {
            alert.setContentText(content);
            return alert;
        }
    }

    private Alert alert;
    private String content = "";
}
