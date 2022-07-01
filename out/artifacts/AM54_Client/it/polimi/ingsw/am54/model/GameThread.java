package it.polimi.ingsw.am54.model;

import it.polimi.ingsw.am54.model.controllers.GameMessageHandler;
import it.polimi.ingsw.am54.network.ConnectionManager;
import it.polimi.ingsw.am54.network.Mage;

import java.util.*;

/**
 * This class controls game and asks players for input that is directly connected to gameplay.
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
    private String currentPlayer;
    private String moveDescription;

    /**
     * Constructor that sets gameID and number of players, also it initialises data necessary for the game.
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
                if(!t.equals(TColor.GREY))
                    towerColors.add(t);
        }
        else if(numPlayers == 3)
            towerColors.addAll(List.of(TColor.values()));
    }

    /**
     * this method checks if all users are ready (have finished the setup phase).
     * if all users are ready calls the start() method. This method is invoked by
     * the commandHandler every time a player finished his initial phase and sends
     * a player_ready message.
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

    /**
     * manages the start and the lifetime of the game.
     */
    @Override
    public void run() {
        messageBroadcast("wait");
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
    }

    /**
     * Sends an update to all clients.
     */
    public void updateAll() {
        for(ConnectionManager cm : clients)
            cm.update();
    }

    /**
     * manages the planning phase of the players.
     * @param listPlayers
     */
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

    /**
     * manages the action phase of the players.
     * @param listPlayers
     */
    private void actionPhase(List<Player> listPlayers){
        for (Player p: listPlayers) {
            ConnectionManager cm = getClientById(p.getPlayerId());
            currentPlayer = cm.getUsername();
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
            //updateAll();
        }

    }

    /**
     * Create a map for the association between the client ID and the chosen tower color.
     */
    private Map<Integer, TColor> getColorMap() {
        Map<Integer, TColor> res = new HashMap<>();
        for (ConnectionManager cm: clients) {
            res.put(cm.getClientID(), cm.getSelectedTower());
        }
        return res;
    }

    /**
     * returns the list of available wizards.
     * @return mage
     */
    public ArrayList<Mage> getMages() {
        return mages;
    }

    /**
     * returns the list of available towers.
     * @return towerColors
     */
    public ArrayList<TColor> getTowerColors() {
        return towerColors;
    }

    /**
     * this method sends a "player_disconnected" message to all clients (excepts the disconnected client)
     * with attached the disconnected client username.
     * @param disconnectedCli client that has disconnected
     */
    public void playerDisconnected(ConnectionManager disconnectedCli){
        disconnectedCli.interrupt();


        for (ConnectionManager cm : clients) {
            if (cm.isAlive() && !cm.equals(disconnectedCli))
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

    /**
     * indicates whether the game is in advanced mode or not.
     */
    public boolean isAdvancedMode() {
        return advancedMode;
    }

    /**
     * sets the game in advanced mode.
     * @param advancedMode
     */
    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

    /**
     * returns the game.
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * returns the list of playedCard.
     * @return playedCards
     */
    public ArrayList<Card> getPlayedCards() {
        return playedCards;
    }

    /**
     * Adds a card to the list of playedCard.
     * @param newCard
     */
    public void addPlayedCards(Card newCard) {
        this.playedCards.add(newCard);
    }

    /**
     * returns the client that matches with the ID in input.
     * @param cid
     */
    protected ConnectionManager getClientById(int cid){
        return clients.stream() //finds player who is owner of card
                .filter(cl -> cid == (cl.getClientID()))
                .findAny()
                .orElse(null);
    }

    /**
     * sends a broadcast message.
     * @param s
     */
    private void messageBroadcast(String s){
        for(ConnectionManager cm : clients)
            cm.sendText(s);
    }

    /**
     * returns current player of the turn.
     * @return currentPlayer
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * returns the move contained in the moveDescription variable.
     * @return moveDescription
     */
    public String getMoveDescription() {
        return moveDescription;
    }

    /**
     * save the move received as a message in the moveDescription variable.
     * @param moveDescription
     */
    public void setMoveDescription(String moveDescription) {
        this.moveDescription = moveDescription;
    }

}
