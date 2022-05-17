package it.polimi.ingsw.am54.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.network.ConnectionManager;
import it.polimi.ingsw.am54.network.Mage;
import it.polimi.ingsw.am54.network.updateMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;



/**
 * This class parses client commands and decides the correct action to perform.
 * @see ConnectionManager
 * @see GameThread
 */
public class ControlHandler {
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
            // tmp messages
            case "get_cards" -> sendCardsInHand();
            case "get_students" -> sendStudents();
            //game messages
            case "select_assistant_card" -> selectAssistantCard(parameter);
            case "move_students" -> moveStudents(parameter);
            case "move_mn" -> moveMotherNature(parameter);
            case "select_cloud" -> selectCloud(parameter); //this also makes turn end
            //advenced game messages
            case "use_personality" -> usePersonality(parameter);
            //other
            case "end" -> end();
            default -> cm.sendText("ERR");
        }
    }



    /**
     * sends the list of students that are in the player's entrance  to client.
     * NOTE: TEMPORARY METHOD
     */
    private void sendStudents() {
        List<Color> tmp = new ArrayList<>();
        for(int i = 0; i < 5 ; i++) {
           tmp.add(currentGame.getGame().bag.getNextStudent());
        }
        currentGame.getGame().getPlayerById(cm.getClientID()).getGameBoard().addStudentsEnter(tmp);
        System.out.println(tmp);
        System.out.println(currentGame.getGame().getPlayerById(cm.getClientID()).getGameBoard().getStudentsEnter());
        List<Color> students =currentGame.getGame().getPlayerById(cm.getClientID()).getGameBoard().getStudentsEnter();
        if(students == null || students.isEmpty()) {
            cm.sendObject("ERR", "No cards in hand");
            return;
        }
        cm.sendObject("students_enter",students);
    }


    /**
     * just sets the setReadyToStart boolean as true.
     */
    private void startGameIfReady(){
        cm.setReadyToStart(true);
        currentGame.checkAndStart();
    }
    /**
     * sends the list of card that are in the player's hand  to client.
     * NOTE: TEMPORARY METHOD
     */
    private void sendCardsInHand() {
        List<Card> cards =currentGame.getGame().getPlayerById(cm.getClientID()).getHand().getAllCards();
        if(cards == null || cards.isEmpty()) {
            cm.sendObject("ERR", "No cards in hand");
            return;
        }
        cm.sendObject("cards_hand",cards);

    }


    private void end() {
        cm.setAlive(false);
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
     * sets the AssistantCard chosen by client.
     * @param cardJson
     */
    private void selectAssistantCard(String cardJson){
        Card selectedCard = gson.fromJson(cardJson, new TypeToken<Card>(){}.getType());
        Hand currentHand = currentGame.getGame().getPlayerById(cm.getClientID()).getHand();

        if(!valueCheck(currentGame.getPlayedCards(), selectedCard)  && !onlyOption(currentHand.getAllCards(), currentGame.getPlayedCards())) {
            cm.sendObject("ERR", "Card already played by other player");
            return;
        }

        if(valueCheck(currentHand.getAllCards(),selectedCard)) {
            cm.sendObject("ERR", "You do not have select card in your hand");
            return;
        }

        currentGame.addPlayedCards(selectedCard);
        currentHand.setCardPlayed(selectedCard);
        currentHand.removeFromCards(selectedCard);
        cm.sendText("ACK");
    }

    /**
     * checks if played card is only option.
     * @param allCards
     * @param playedCards
     */
    private boolean onlyOption(List<Card> allCards, ArrayList<Card> playedCards) {
        for(Card option : allCards) {
            boolean test = false;
            for (Card played : playedCards) {
                if (option.getValue() == played.getValue()) {
                    test = true;
                    break;
                }
            }
            if (!test)
                return false;
        }
        return true;
    }
    // returns true if there is no card with same value as selectedCard in list of cards
    private boolean valueCheck(List<Card> playedCards, Card selectedCard) {
        for (Card c : playedCards) {
            if(c.getValue() == selectedCard.getValue())
                return false;
        }
        return true;
    }


    /**
     * Moves students from player's Enter to islands/player's hall.
     * @param mvStdntJson
     */
    private void moveStudents(String mvStdntJson){
        int playerID = cm.getClientID();
        Game game = currentGame.getGame();
        GameBoard gb = game.getPlayerById(playerID).getGameBoard();


        JsonObject jobject = gson.fromJson(mvStdntJson, new TypeToken<JsonObject>(){}.getType());
        int where = Integer.parseInt(jobject.get("location").toString());

        String json = jobject.get("students").toString();

        ArrayList<Color> students = gson.fromJson(json, new TypeToken<List<Color>>(){}.getType());

        /* if one or more students aren't in the student's enter we send an error to the client*/
        if(!gb.getStudentsEnter().containsAll(students)) {
            cm.sendObject("ERR", "students not available");
            return;
        }

        //I check the conditions
        if(where != 0 && game.getIslandPosition(where) == -1){
            cm.sendObject("ERR", "this position doesn't exist");
            return;
        }

        // if all conditions are ok, I move all selected students
        for (Color student: students) {
            game.moveStudents(playerID, where, student);
        }
        cm.sendText("ACK");

    }


    private void moveMotherNature(String moves) {
        int selectedMoves = gson.fromJson(moves, new TypeToken<Integer>(){}.getType());
        Game game =  currentGame.getGame();
        Hand hand = game.getPlayerById(cm.getClientID()).getHand();

        if(hand.getCardPlayed() == null)
        {
            cm.sendObject("ERR", "No assistant card selected");
            return;
        }

        if(selectedMoves <= 0 || selectedMoves > hand.getCardPlayed().getMaxMoves())
        {
            cm.sendObject("ERR", "Incorrect number of moves");
            return;
        }

        game.moveMN(cm.getClientID(), selectedMoves);
        cm.sendText("ACK");
    }

    /**
     * gives to each client the possibility to choose a cloud (and its students) at the end of his turn.
     * @param cloudJson
     */
    private void selectCloud(String cloudJson){
        /*
        in case we need to check if players send the right command
        if(currentGame.getGame().listPlayers.get(0).getPlayerId() != cm.getClientID())
            cm.sendObject("ERR", "command not possible");*/
        int selectedCloud = gson.fromJson(cloudJson, new TypeToken<Integer>(){}.getType());
        if(!currentGame.getGame().clouds.containsKey(selectedCloud)){
            cm.sendObject("ERR", "cloud not available");
            return;
        }
        cm.sendText("ACK");
        currentGame.getGame().clouds.remove(selectedCloud);
        cm.setTurnEnd(true);
        //currentGame.getGame().nextRound();
    }

    /**
     * Buys and uses the personalityCard chosen by client.
     * @param personalityJson
     */
    private void usePersonality(String personalityJson){
        //TODO
        String PersonalityString =gson.fromJson(personalityJson, new TypeToken<String>(){}.getType());
        //check that string isn't empty
        if(PersonalityString.isBlank()) {
            cm.sendObject("ERR", "chosenPersonality cannot be blank");
            return;
        }
        //check that chosen personality exists
        Personality chosenPersonality = currentGame.getGame().getPersonalityWithName(PersonalityString);
        if(!currentGame.getGame().listPersonality.contains(chosenPersonality)) {
            cm.sendObject("ERR", "chosenPersonality doesn't exist");
            return;
        }
        //buy the chosen personality
        int playerID = cm.getClientID();
        currentGame.getGame().buyPersonality(playerID,chosenPersonality);
        //use the chosen personality
        currentGame.getGame().usePersonalityPower(chosenPersonality);
        cm.sendText("ACK");
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
     * checks if the username in input already exists.
     * @param username
     */
    private  boolean nameInUse(String username) {
        for(ConnectionManager cm: currentGame.getClients())
            if(cm.getUsername()!= null && cm.getUsername().equals(username))
                return true;
        return false;
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

    public void update(){
        Game game = currentGame.getGame();
        if(currentGame == null || game == null)
            return;

        updateMessage update = new updateMessage(game);

        cm.sendObject("update", update);

    }

    /**
     * this method returns the gameThread that this controlHandler is currently linked to
     * @return currentGame
     */
    public GameThread getGameThread() {
        return currentGame;
    }
}


