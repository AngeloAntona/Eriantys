package it.polimi.ingsw.am54.view.gui;
import it.polimi.ingsw.am54.model.TColor;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Mage;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.View;
import it.polimi.ingsw.am54.view.gui.controllers.mainController;
import it.polimi.ingsw.am54.view.gui.controllers.personalityController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *this class implements the graphical interface.
 */
public class GUI extends Application implements View {
    private static int playerID;
    private static int numPlayers;
    private static boolean isAdvancedMode;
    private String username; //we could move this into Client
    private static Stage stage;
    private static Stage popUP;
    private GUIInputHandler popUPController;
    private Mage mage;
    private TColor towerColor;
    private String currentScenePath; //current scene that is showing
    private static final Map<String, Scene> loadedScenes = new HashMap<>();
    private static FXMLLoader fxmlLoader;
    private static GUIController guiController;

    //launch calls start (method from Application Class)
    public void initialize() {
        launch();
    }

    /**
     * starts the gui and creates a new GUIController. Then sets the current scene as
     * the start_screen scene.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception { //this method is called by launch()
        stage.setTitle("Eriantys");
        GUI.stage = stage;
        GUI.popUP = new Stage();
        //stage.setResizable(false);
        stage.setMaximized(true);
        guiController = new GUIController(this);
        Client.viewController = guiController;
        setScene(GuiResources.startScreen);
    }


    public void resetPosition(){
        GUI.getStage().setX(0);
        GUI.getStage().setY(0);
    }

    /**
     * this method loads a scene from an fxmlPath but doesn't show it to the user.
     * This method is used for preloading scenes.
     * @param fxmlPath string which contains the path to the fxmlScene
     */
    public GUIInputHandler loadScene(String fxmlPath){
        try {
            System.out.println("fxml: " + fxmlPath);
            fxmlLoader = new FXMLLoader(GUI.class.getResource(fxmlPath));
            loadedScenes.put(fxmlPath, new Scene(fxmlLoader.load()));
            GUIInputHandler controller = fxmlLoader.getController();
            controller.setGui(this);
            controller.setGuiController(guiController);
            if( controller instanceof mainController)
                guiController.setGameplayController((mainController) controller);
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * this method loads a scene in the scene cache and sets the scene and updates
     * the current stage to see the scene.
     * @param fxmlPath scene to be set
     */
    public void setScene(String fxmlPath){
        //if the scene to show is already the one chosen
        if(fxmlPath.equals(currentScenePath))
            return;

        //if the scene has to be loaded
        if(!loadedScenes.containsKey(fxmlPath)){
            loadScene(fxmlPath);
        }

        //when the scene is already loaded in memory
        currentScenePath = fxmlPath;
        Scene currentScene = loadedScenes.get(fxmlPath);
        stage.setScene(currentScene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * loads the scene in the scene cache and sets it as the popUP scene.
     * After that it calls popUP.show() to show the popUP.
     * @param fxmlPath scene to set as the popUP
     */
    public void setPopUpScene(String fxmlPath){
        //if the scene has to be loaded
        if(!loadedScenes.containsKey(fxmlPath)){
            loadScene(fxmlPath);
        }

        //when the scene is already loaded in memory
        Scene currentScene = loadedScenes.get(fxmlPath);
        popUP.setScene(currentScene);
        popUP.setTitle(GuiResources.personalityNameFromImage(fxmlPath));
        popUP.sizeToScene();
        popUP.show();
    }

    public void closePopUp(){
        popUP.close();
    }

    /**
     * displays a message to the user.
     * @param message to be displayed
     */
    public void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    /**
     * displays an error message to the user.
     * @param message to be displayed
     */
    public void displayErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }


    public void clearSceneCache(){
        loadedScenes.clear();
    }


    @Override
    public void stop() throws Exception {
        Client.end();
        guiController.end();
        super.stop();
    }

    /**
     * @return username
     */
    public String getName() { return this.username; }

    /**
     * set the username
     * @param name
     */
    public void setName(String name) { this.username = name;}

    /**
     * @return mage
     */
    public Mage getMage() { return mage; }

    /**
     * set the mage
     * @param mage
     */
    public void setMage(Mage mage) { this.mage = mage; }

    /**
     * @return numPlayers
     */
    public static int getNumPlayers() { return numPlayers; }

    /**
     * set the number of the player in the match
     * @param numPlayers
     */
    public static void setNumPlayers(int numPlayers) { GUI.numPlayers = numPlayers; }

    /**
     * @return isAdvancedMode boolean, that is true if the match is in advanced mode
     */
    public static boolean isAdvancedMode() { return isAdvancedMode; }

    /**
     * set the value of advancedMode boolean.
     * @param advancedMode
     */
    public static void setAdvancedMode(boolean advancedMode) { isAdvancedMode = advancedMode; }

    /**
     * @return towerColor
     */
    public TColor getTowerColor() { return towerColor; }

    /**
     * set the tower's Color
     * @param towerColor
     */
    public void setTowerColor(TColor towerColor) { this.towerColor = towerColor; }

    /**
     * @return currentScenePath
     */
    public String getCurrentScenePath() { return currentScenePath; }

    /**
     * @return guiController
     */
    public static GUIController getGuiController() { return guiController; }

    /**
     * @return stage
     */
    public static Stage getStage() { return stage; }

    /**
     * set Player's ID
     * @param id
     */
    public static void setPlayerID (int id) { playerID = id; }

    /**
     * @return playerID
     */
    public static int getPlayerID () { return playerID; }
}
