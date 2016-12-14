package gui.controllers;

import automatons.Automaton;
import gui.AutomatonMode;
import javafx.scene.control.Alert;

/**
 * This is an abstract class for any type of controller that is responsible for setting parameters of the newly created automaton.
 */
public abstract class CreatorController implements Controller {
    /**
     * Gathers the user input and creates automaton with specified parameters.
     * @return automaton with parameters set according to user input
     */
    public abstract Automaton createAutomaton();

    /**
     * Gets the number of cells in width of the created automaton.
     *
     * @return number of cells in width of the created automaton
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the number of cells in height of the created automaton.
     *
     * @return number of cells in height of the created automaton
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the mode of the created automaton.
     *
     * @return mode of the created automaton used to set UI properly
     */
    public abstract AutomatonMode getMode();

    /**
     * Validate the user input according to rules which a created automaton must fulfill.
     *
     * @return Alert popup window which will be <code>null</code> if the user input was valid
     */
    public abstract Alert inputIsValid();

    /**
     * Number of cells in width of the created automaton.
     */
    protected int width;

    /**
     * Number of cells in height of the created automaton.
     */
    protected int height;
}
