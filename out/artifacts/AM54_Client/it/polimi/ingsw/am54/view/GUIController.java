package it.polimi.ingsw.am54.view;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Messages;
import it.polimi.ingsw.am54.network.PhaseManager;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.controllers.mainController;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import it.polimi.ingsw.am54.view.gui.controllers.InitialPhaseController;
import javafx.application.Platform;

import java.util.List;

public class GUIController implements ViewController {
    private static int studentToMove;
    private int numPlayers;
    private boolean isAdvancedMode;
    private static boolean motherMoved = false;
    private static int MNmaxMoves;
    private InitialPhaseController initialPhaseController;
    private static mainController gameplayController;
    private PhaseManager phaseManager;
    private GUI gui;

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
    public static int getMNmaxMoves() { return MNmaxMoves; }
}
