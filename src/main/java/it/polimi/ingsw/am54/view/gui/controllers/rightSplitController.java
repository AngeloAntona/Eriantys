package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import javafx.fxml.FXML;
import java.util.List;

/**
 * manages the user's interaction with the rightSplit.
 */
public class rightSplitController {
    @FXML public otherPlayersController player1Controller;
    @FXML public otherPlayersController player2Controller;
    @FXML public otherPlayersController player3Controller;
    private GUI gui;
    private GUIController guiController;

    @FXML
    public void initialize(){
    }

    /**
     * this method takes as a parameter the list of all players and updates
     * gameboards and informations of only the players that have a different ID
     * from GUI.getPlayerID()
     * @see GUI#getPlayerID()
     * @param players list of all players in the game
     */
    public void update(List<Player> players, String moveDescription, String currentPlayer){
        players.removeIf(x->x.getPlayerId() == GUI.getPlayerID());
        int numPlayer = players.size();
        player1Controller.update(players.get(0), moveDescription, currentPlayer);
        if(numPlayer > 1)
            player2Controller.update(players.get(1), moveDescription, currentPlayer);
        if(numPlayer > 2)
            player3Controller.update(players.get(2), moveDescription, currentPlayer);
    }

    /**
     * @param gui
     */
    public void setGui(GUI gui){
        this.gui = gui;
    }

    /**
     * @param controller
     */
    public void setGuiController(GUIController controller) {
        this.guiController = controller;
    }
}
