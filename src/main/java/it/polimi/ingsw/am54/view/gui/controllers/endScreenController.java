package it.polimi.ingsw.am54.view.gui.controllers;

import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class endScreenController implements GUIInputHandler {
    private GUIController guiController;
    private GUI gui;
    @FXML public Text winnerText;

    @FXML
    public void initialize() {
        if (winnerText != null)
            winnerText.setText(GUIController.getWinner() + " wins.");
    }

    @FXML
    public void onExitButton(ActionEvent event){
        try {
            gui.stop();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @FXML
    public void onNewMatch(ActionEvent event){

    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void setGuiController(GUIController controller) {
        this.guiController = controller;
    }
}
