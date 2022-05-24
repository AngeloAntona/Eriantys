package it.polimi.ingsw.am54.model.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.*;
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
public class GameMessageHandler {
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

        Game game = currentGame.getGame();
        int selectedCloud = gson.fromJson(cloudJson, new TypeToken<Integer>(){}.getType());
        if(!game.clouds.containsKey(selectedCloud)){
            cm.sendObject("ERR", "cloud not available");
            return;
        }
        cm.sendText("ACK");
        game.getPlayerById(cm.getClientID()).getGameBoard().addStudentsEnter(game.clouds.get(selectedCloud));
        game.clouds.remove(selectedCloud);
        cm.setTurnEnd(true);
        //currentGame.getGame().nextRound();
    }

    /**
     * Buys and uses the personalityCard chosen by client.
     * @param personalityJson
     */
    private void usePersonality(String personalityJson){
        JsonObject object =gson.fromJson(personalityJson, new TypeToken<JsonObject>(){}.getType());
        String PersonalityString = gson.fromJson(object.get("card") .toString(), new TypeToken<String>(){}.getType());
        if(PersonalityString.isBlank()) {
            cm.sendObject("ERR", "chosenPersonality cannot be blank");
            return;
        }
        //check that chosen personality exists
        Personality chosenPersonality = currentGame.getGame().getPersonalityWithName(PersonalityString);
        if(chosenPersonality == null) {
            cm.sendObject("ERR", "chosenPersonality doesn't exist");
            return;
        }
        //buy the chosen personality
        int playerID = cm.getClientID();

        //use the chosen personality
        String out = currentGame.getGame().usePersonalityPower(chosenPersonality,playerID, object);

        if(!out.equals("OK")){
            cm.sendObject("ERR", out);
            return;
        }
        cm.sendText("ACK");
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

    public void setCurrentGame(GameThread currentGame) {
        this.currentGame = currentGame;
    }

    public void setCm(ConnectionManager cm) {
        this.cm = cm;
    }
}


