package gui.controllers;

import automatons.Automaton;
import gui.AutomatonMode;
import javafx.scene.control.Alert;

/**
 * Created by bzdeco on 30.11.16.
 */
public abstract class CreatorController implements Controller {
    public abstract Automaton createAutomaton();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public abstract AutomatonMode getMode();

    public abstract Alert inputIsValid();

    protected int width;
    protected int height;
}
