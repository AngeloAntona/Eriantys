package it.polimi.ingsw.am54.model;

import it.polimi.ingsw.am54.model.controllers.GameMessageHandler;
import it.polimi.ingsw.am54.network.ConnectionManager;
import it.polimi.ingsw.am54.network.Mage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class controls game and asks players for input that is directly connected to gameplay
 */
public class GameThread extends Thread {
    private boolean advancedMode;
    private boolean disconnected;
    private final int gameId; //id of the specific match OK
    private final int numPlayers; // type of match OK
    private final List<ConnectionManager> clients; //list of connected client's socket.
    private final ArrayList<Mage> mages; //colour of the back of assistant cards available for client's choice
    private final ArrayList<TColor> towerColors; //colour of towers available for client's choice
    private ArrayList<Card> playedCards; //list of already played cards in current turn
    private  Game game;

    /**
     * Constructor that sets gameID and number of players, also it initialises data necessary for the game
     * @param id game identification
     * @param numPlayers total number of players
     */
    public GameThread(int id, int numPlayers) {
        this.gameId = id;
        this.numPlayers = numPlayers;
        this.disconnected = false;
        clients = new ArrayList<>();
        mages = new ArrayList<>(List.of(Mage.values()));//List.of("SkyMagician", "VioletWitch", "YellowKing", "HerbMagician"));
        towerColors = new ArrayList<>();
        playedCards = new ArrayList<>();
        // generate a list of available towers in the case of 2/4 or 3 players
        if(numPlayers == 2 || numPlayers == 4)
        {
            for(TColor t : TColor.values())
                if(!t.equals(TColor.GRAY))
                    towerColors.add(t);
        }
        else if(numPlayers == 3)
            towerColors.addAll(List.of(TColor.values()));
    }

    /**
     * this method checks if all users are ready (have finished the setup phase)
     * if all users are ready calls the start() method. This method is invoked by
     * the commandHandler every time a player finished his initial phase and sends
     * a player_ready message
     * @see GameMessageHandler#commandHandler(String, String)
     */
    public synchronized void checkAndStart(){
        if(clients.size() == numPlayers) {
            boolean start = clients.stream().allMatch(ConnectionManager::isReadyToStart);
            if (start && !this.isAlive()) {
                this.start();
            }
        }
    }

    // the game does not have to start until the lobby is full:
    @Override
    public void run() {
        game = new Game(gameId, numPlayers,getColorMap());
        System.out.println("Game with id:" + game.getGameID() + " started");

        for (ConnectionManager cm: clients) {
            game.getPlayerById(cm.getClientID()).getGameBoard().setUsername(cm.getUsername());
        }
        while (game.winner == 0 && !disconnected) {
            game.nextRound();
            game.clouds = game.getClouds();
            updateAll();
            planningPhase(game.listPlayers);
            actionPhase(game.listPlayers);
        }
        messageBroadcast("Winner is " + getClientById(game.winner).getUsername());


        /*
        while (countUsernames() != numPlayers) {
            try {
                Thread.sleep(1000); // waits  a second before next check
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        int cnt;
        do //controls if all players have finished initial phase (tower color is lest element to be selected)
        {
            cnt = 0;
            for (ConnectionManager cm : clients)
                if (cm.getSelectedTower() != null)
                    cnt++;
            try {
                Thread.sleep(1000); // waits  a second before next check
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } while (cnt != numPlayers);

        while (game.winner == 0) {
            game.nextRound();
            planningPhase(game.listPlayers);
        }

        */

        /*Game game = new Game(gameId, numPlayers);
        ArrayList<Player> gameOrder = new ArrayList<>(game.listPlayers);

        for(CommunicationThread ct: communication)
            ct.setActive(false);

       /while (game.winner == 0)
        {
            game.nextRound();
            for (Player player : gameOrder) {
                try {
                    play(getConnectionWithID(player.getPlayerId()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                game.checkWinner();
            }

        }*/
    }


    private void updateAll() {
        for(ConnectionManager cm : clients)
            cm.update();
    }

    private void planningPhase(List<Player> listPlayers) {
        playedCards.clear(); // list of played cards should be emptied every turn
        for (Player p: listPlayers) {
            int old = playedCards.size();
            ConnectionManager cm = getClientById(p.getPlayerId());
            cm.sendText("planning_turn");

            while (playedCards.size() != old+1) {
                try {
                    Thread.sleep(1000); // checks every second if player has selected card
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            cm.sendText("wait");
        }
    }

    private void actionPhase(List<Player> listPlayers){
        for (Player p: listPlayers) {
            ConnectionManager cm = getClientById(p.getPlayerId());
            cm.sendText("next_turn");
            cm.setTurnEnd(false);
            while (!cm.isTurnEnd())
            {
                try {
                    Thread.sleep(1000); // checks every second if player has finished their turn
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            cm.sendText("wait");
            updateAll();
        }

    }

    private Map<Integer, TColor> getColorMap() {
        Map<Integer, TColor> res = new HashMap<>();
        for (ConnectionManager cm: clients) {
            res.put(cm.getClientID(), cm.getSelectedTower());
        }
        return res;
    }
    /*
    private void play(ConnectionManager currentPlayer) throws IOException {
        ArrayList<String> availableCommands = new ArrayList<>(List.of("move_students", "end_turn", "use_personality"));
        currentPlayer.setActive(true);

        while (!availableCommands.isEmpty())
        {
           String input = currentPlayer.receiveText();
           if(!availableCommands.contains(input))
               currentPlayer.sendText("ERR");

        }
        currentPlayer.setActive(false);

    }
    */
   /* private CommunicationThread getConnectionWithID(int id){
        return communication.stream() //finds player who is owner of card
                .filter(ct -> id == (ct.getClientID()))
                .findAny()
                .orElse(null);
    }*/

    /**
     * this method sends a copy of the model to all clients. This copy of the model
     * has to be readable from the view
     */
    public void sendUpdates(){
        //TODO
    }

    public ArrayList<Mage> getMages() {
        return mages;
    }

    public ArrayList<TColor> getTowerColors() {
        return towerColors;
    }

    /**
     * this method sends a "player_disconnected" message to all clients (excepts the disconnected client)
     * with attached the disconnected client username.
     * @param disconnectedCli client that has disconnected
     */
    public void playerDisconnected(ConnectionManager disconnectedCli){
        for (ConnectionManager cm: clients){
            if(!cm.equals(disconnectedCli))
                cm.sendObject("player_disconnected", disconnectedCli.getUsername());
        }
    }

    /**
     * Returns total number of players
     * @return total number of players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Returns list of currently connected clients
     * @return list of connected clients
     */
    public synchronized List<ConnectionManager> getClients() {
        return List.copyOf(clients);
    }

    /**
     * Allows adding new client to the game
     * @param cli client's socket
     */
    public void addClient(ConnectionManager cli) {
        clients.add(cli); //add the socket of the new client
    }

    /**
     * Returns ID of game
     * @return game identification
     */
    public int getGameId() {
        return gameId;
    }

    public boolean isAdvancedMode() {
        return advancedMode;
    }

    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    public Game getGame() {
        return game;
    }

    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    public void addPlayedCards(Card newCard) {
        this.playedCards.add(newCard);
    }

    protected ConnectionManager getClientById(int cid){
        return clients.stream() //finds player who is owner of card
                .filter(cl -> cid == (cl.getClientID()))
                .findAny()
                .orElse(null);
    }

    private void messageBroadcast(String s){
        for(ConnectionManager cm : clients)
            cm.sendText(s);
    }
}
