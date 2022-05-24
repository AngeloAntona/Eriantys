package it.polimi.ingsw.am54.model.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.GameThread;
import it.polimi.ingsw.am54.network.ConnectionManager;

import java.util.List;

public class MessageHandler {
    private ConnectionManager cm;
    private GameThread currentGame = null;
    private static final Gson gson = new GsonBuilder().create();
    private  GameMessageHandler gameHandler = new GameMessageHandler();
    private  LobbyMessageHandler lobbyHandler = new LobbyMessageHandler();

    public void Handle(String command, String parameter){
        if(currentGame != null){
            if(currentGame.isAlive()){
                //if thread is running (= if game has started) then use GameMessageHandler
                gameHandler.commandHandler(command, parameter);
            } else {
                //otherwise use lobbyMessageHandler
                lobbyHandler.commandHandler(command, parameter);
            }
        }
    }

    /**
     * Adds the connectionManager(the client) to a GameThread with the number of players and advanced mode option
     * asked for by the corresponding client. If the game doesn't exist it creates a new one.
     * @param input
     * @param games
     * @param connectionManager
     */
    public void joinGame(String input, List<GameThread> games, ConnectionManager connectionManager) {
        this.cm = connectionManager;
        gameHandler.setCm(connectionManager);
        lobbyHandler.setCm(connectionManager);

        String options = gson.fromJson(input, new TypeToken<String>(){}.getType());
        String isAdvancedMode = cm.getParameter(options);
        String numberOfPlayers = cm.getCommand(options);
        System.out.println(numberOfPlayers);
        if(numberOfPlayers.isEmpty() || !isValidPlayerNumber(numberOfPlayers) || isAdvancedMode.isEmpty()){
            cm.sendText("ERR");
        } else{
            boolean advancedMode = Boolean.parseBoolean(isAdvancedMode);
            int numPlayers = Integer.parseInt(numberOfPlayers);
            for (GameThread gt: games) {
                if(gt.getNumPlayers() == numPlayers && gt.getClients().size() < numPlayers
                        && gt.isAdvancedMode() == advancedMode) {
                    gt.addClient(cm);
                    this.currentGame = gt;
                    gameHandler.setCurrentGame(gt);
                    lobbyHandler.setCurrentGame(gt);
                    cm.sendObject("ACK", gt.getClients().size());
                    cm.setClientID(gt.getClients().size());
                    return;
                }

            }
            GameThread gt = new GameThread(games.size(), numPlayers);
            //gt.start();
            games.add(gt);
            gt.addClient(cm);
            this.currentGame = gt;
            gameHandler.setCurrentGame(gt);
            lobbyHandler.setCurrentGame(gt);
            currentGame.setAdvancedMode(advancedMode);
            cm.sendObject("ACK", gt.getClients().size());
            cm.setClientID(gt.getClients().size());
        }
    }

    /**
     * parses the string to an int and returns true if this number
     * is a valid number of player for a game.
     * @param str
     */
    public static boolean isValidPlayerNumber(String str) {
        if(str == null)
            return false;
        try {
            int test = Integer.parseInt(str);
            return (test <= 4 && test >= 2);
        } catch(NumberFormatException e){
            return false;
        }
    }

    public GameThread getGameThread() {
        return currentGame;
    }

    public void update(){
        gameHandler.update();
    }
}
