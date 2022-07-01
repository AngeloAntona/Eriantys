package it.polimi.ingsw.am54.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.model.Player;
import it.polimi.ingsw.am54.network.*;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.controllers.mainController;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import it.polimi.ingsw.am54.view.gui.controllers.InitialPhaseController;
import javafx.application.Platform;

import java.util.List;

public class GUIController implements ViewController {
    private static int additionalMaxMoves;
    private static int studentToMove;
    private static int numPlayers;
    private static String winner;
    private static Mage mage;
    private boolean isAdvancedMode;
    private static boolean motherMoved = false;
    private static int MNmaxMoves;
    private static mainController gameplayController;
    private final PhaseManager phaseManager;
    private final GUI gui;
    public GUIController(GUI gui) {
        this.gui = gui;
        phaseManager = new PhaseManager(this);
    }

    public void startGame(){
        numPlayers = GUI.getNumPlayers();
        isAdvancedMode = GUI.isAdvancedMode();
        gui.clearSceneCache();
        phaseManager.startGame();
    }

    @Override
    public void initGame(){
        Platform.runLater(() -> {
            gui.resetPosition();
            gui.setScene(GuiResources.mainScreen(numPlayers, isAdvancedMode));
            gameplayController.setGuiController(this);
            gameplayController.setGui(gui);
            gameplayController.allDisable();
        });
    }

    @Override
    public void initialPhase() {
        //maybe doesn't matter
    }

    @Override
    public void waitPhase() {
        Platform.runLater(()-> gameplayController.waitPhase());
        phaseManager.waitState();
    }

    @Override
    public void displayWin(String input) {
        winner = input.split("Winner ")[1];
        System.out.println("in: " + input);
        System.out.println("getName: " + gui.getName());
        if(winner.equals(gui.getName())){
            Platform.runLater(()-> gui.setScene(GuiResources.winScreen));
        } else {
            Platform.runLater(()-> gui.setScene(GuiResources.loseScreen));
        }
    }

    @Override
    public void planningPhase() {
        System.out.println("planning phase");
        Platform.runLater(()-> gameplayController.planningPhase());
    }

    @Override
    public void actionPhase() {
        studentToMove = 3;
        motherMoved = false;
        Platform.runLater(()-> gameplayController.actionPhase());
    }

    @Override
    public void displayMessage(String message) {
        Platform.runLater(()-> gameplayController.alert(message));
    }

    @Override
    public void update(updateMessage update) {
        Player myPlayer = update.players.stream().filter(x->x.playerId == GUI.getPlayerID()).findAny().orElse(null);
        if(myPlayer != null && myPlayer.getHand() != null && myPlayer.getHand().getCardPlayed() != null )
            setMNMaxMoves(myPlayer.getHand().getCardPlayed().getMaxMoves());
        Platform.runLater(()-> gameplayController.update(update));
    }

    @Override
    public void playerDisconnected(String playerName) {
        displayMessage("Player: " + playerName + " has disconnected");
        end();
    }

    public void end() {
        //InitialPhaseController.end();
        Thread.interrupted();
    }

    public void setGameplayController(mainController controller){
        gameplayController = controller;
    }

    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    public static void moveStudents(int where, List<Color> students){
        if(studentToMove <= 0)
            return;
        JsonObject out = new JsonObject();
        JsonArray jsonStudents = new JsonArray();
        students.forEach(x->jsonStudents.add(x.toString()));

        out.addProperty("location", where);
        out.add("students", jsonStudents);
        try {
            Client.command(Messages.moveStudents, out);
            studentToMove -= students.size();
        } catch (ServerErrorException e) {
            e.printStackTrace();
        }
        checkNoMoreMoves();
        Client.receiveSelfUpdate();
    }

    public static void moveMN(int n){
        try{
            Client.command(Messages.moveMn, n);
            motherMoved = true;
        } catch (ServerErrorException e){
            e.printStackTrace();
        }
        checkNoMoreMoves();
        Client.receiveSelfUpdate();
    }


    public static void checkNoMoreMoves(){
        if(motherMoved && studentToMove <= 0)
            Platform.runLater(gameplayController::cloudSelectPhase);
    }

    public static int getStudentToMove() {
        return studentToMove;
    }
    public static mainController getMainController() { return gameplayController; }
    public static boolean motherMoved() { return motherMoved; }
    public static void setMNMaxMoves(int n) { MNmaxMoves = n; }
    public static int getMNmaxMoves() { return MNmaxMoves + additionalMaxMoves; }
    public static String getWinner() { return winner; }
    public static int getNumPlayers() { return numPlayers; }
    public static int getAdditionalMaxMoves() { return additionalMaxMoves; }
    public static void setAdditionalMaxMoves(int additionalMaxMoves) { GUIController.additionalMaxMoves = additionalMaxMoves; }
    public static Mage getMage() { return mage; }
    public static void setMage(Mage mage) { GUIController.mage = mage;}


}
