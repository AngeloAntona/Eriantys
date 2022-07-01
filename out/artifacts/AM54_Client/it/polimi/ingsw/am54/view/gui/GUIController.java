package it.polimi.ingsw.am54.view.gui;

import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.ViewController;
import it.polimi.ingsw.am54.view.gui.GUI;

public class GUIController implements ViewController {
    private GUI gui;
    // public GUIController(GUI gui) {this.gui = gui;}

    @Override
    public void initializeView() {
        gui = new GUI();
        gui.initialize();
    }

    @Override
    public void initialPhase() {

    }

    @Override
    public void planningPhase() {

    }

    @Override
    public void actionPhase() {

    }

    @Override
    public void waitPhase() {

    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void update(updateMessage update) {

    }

    @Override
    public void playerDisconnected(String playerName) {

    }
}
