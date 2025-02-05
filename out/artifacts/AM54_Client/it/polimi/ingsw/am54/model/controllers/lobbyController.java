package it.polimi.ingsw.am54.model.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.GameThread;
import it.polimi.ingsw.am54.model.TColor;
import it.polimi.ingsw.am54.network.ConnectionManager;
import it.polimi.ingsw.am54.network.Mage;

public class lobbyController {
    private ConnectionManager cm;
    private GameThread currentGame = null;
    private static final Gson gson = new GsonBuilder().create();

    /**
     * commandHandler selects the right function to use based on the command given
     * @param command
     * @param parameter
     */
    public void commandHandler(String command, String parameter) {
        switch (command) {
            // initial phase messages
            case "set_username" -> setUsername(parameter);
            case "get_available_mages" -> sendAvailableMages();
            case "select_mage" -> selectMage(parameter);
            case "get_available_towers" -> sendAvailableTowers();
            case "select_tower" -> selectTower(parameter);
            case "player_ready" -> startGameIfReady();
            default -> cm.sendText("ERR");
        }
    }

    /**
     * sets the connectionManager username or sends the client an error if the username.
     * is already in use/isn't valid
     * @param name
     */
    private void setUsername(String name) {
        String username =gson.fromJson(name, new TypeToken<String>(){}.getType());
        if(username.isBlank()) {
            cm.sendObject("ERR", "username cannot be blank");
            return;
        }
        if(nameInUse(username)) {
            cm.sendObject("ERR", "username already in use");
            return;
        }
        cm.setUsername(username);
        cm.sendText("ACK");
    }

    /**
     * sends a list of available mages to client.
     */
    private void sendAvailableMages(){
        if(currentGame.getMages() == null || currentGame.getMages().size() == 0){
            cm.sendObject("ERR", "no available mages");
            return;
        }
        cm.sendObject("available_mages",currentGame.getMages());
    }

    /**
     * sends a list of tower's available colours to client.
     */
    private void sendAvailableTowers(){
        if(currentGame.getTowerColors().size() == 0){
            cm.sendObject("ERR", "no available towers");
            return;
        }
        cm.sendObject("available_towers", currentGame.getTowerColors());
    }

    /**
     * gives to each client the possibility to choose one of the tower's colours at the start of the game.
     * @param towerJson
     */
    private void selectTower(String towerJson) {
        TColor selectedTower = gson.fromJson(towerJson, new TypeToken<TColor>(){}.getType());
        if(!currentGame.getTowerColors().contains(selectedTower)){
            cm.sendObject("ERR", "tower color not available");
            return;
        }
        currentGame.getTowerColors().remove(selectedTower);
        cm.sendText("ACK");
        cm.setSelectedTower(selectedTower);
        cm.sendText("wait");
    }

    /**
     * gives to each client the possibility to choose one of the 4 mages at the start of the game.
     * @param mageJson
     */
    private void selectMage(String mageJson) {
        Mage selectedMage = gson.fromJson(mageJson, new TypeToken<Mage>(){}.getType());
        if(!currentGame.getMages().contains(selectedMage)){
            cm.sendObject("ERR", "mage not available");
            return;
        }
        currentGame.getMages().remove(selectedMage);
        cm.sendText("ACK");
    }


    /**
     * just sets the setReadyToStart boolean as true.
     */
    private void startGameIfReady(){
        cm.setReadyToStart(true);
        currentGame.checkAndStart();
    }

    /**
     * checks if the username in input already exists.
     * @param username
     */
    private  boolean nameInUse(String username) {
        for(ConnectionManager cm: currentGame.getClients())
            if(cm.getUsername()!= null && cm.getUsername().equals(username))
                return true;
        return false;
    }
}
