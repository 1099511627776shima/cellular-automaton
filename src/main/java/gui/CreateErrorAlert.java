package gui;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

/**
 * This is a wrapper class for creating JavaFX Alert popups for automaton creator windows.
 */
public class CreateErrorAlert {
    /**
     * Sole constructor of this class. Sets the title of the popup, error header and Modality of the displayed window.
     *
     * @see javafx.stage.Modality
     */
    public CreateErrorAlert() {
        this.alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Create New Automaton - Error");
        alert.setHeaderText("Automaton parameters set incorrectly");
        alert.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Adds detailed message to the alert box which will be displayed bellow the header. Each message will be displayed in a new line.
     * @param msg message to be displayed in Alert popup window.
     */
    public void addMessage(String msg) {
        content += msg + "\n";
    }

    /**
     * Gets the Alert object with provided alert messages.
     *
     * @return Alert object with provided detailed information about invalidly set parameters. If no detailed information was added it will return <code>null</code>.
     */
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
