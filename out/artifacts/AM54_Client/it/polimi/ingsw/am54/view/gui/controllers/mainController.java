package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import static it.polimi.ingsw.am54.view.gui.GuiResources.*;


public class mainController implements GUIInputHandler {
    private int numPlayers;
    private boolean isAdvancedMode;
    private GUIController guiController;
    private GUI gui;
    public mainController(){}

    @FXML public AnchorPane bottom;
    @FXML public AnchorPane bottomAnchor;
    @FXML public AnchorPane rightAnchor;
    @FXML public AnchorPane topAnchor;
    @FXML public bottomSplitController bottomAnchorController;
    @FXML public rightSplitController rightAnchorController;
    @FXML public topSplitController topAnchorController;
    @FXML public SplitPane rightSplit;
    @FXML public SplitPane bottomSplit;


    /**
     *initializes the game interface.
     */
    @FXML
    public void initialize(){
        numPlayers = GUI.getNumPlayers();
        isAdvancedMode = GUI.isAdvancedMode();
        bottomAnchorController.setAdvancedMode(isAdvancedMode);
        bottomAnchorController.setGui(gui);
        bottomAnchor.getTransforms().add(new Scale(scalefactor, scalefactor, 0, 0));
        rightAnchor.getTransforms().add(new Scale(scalefactor, scalefactor, 0, 0));
    }

    /**
     *disables all interactions.
     */
    public void allDisable(){
        bottom.getChildren().removeIf(x->"anotherPlayerText".equals(x.getId()));
        bottomAnchor.setEffect(null);
        topAnchor.setDisable(true);
        rightAnchor.setDisable(true);
        bottomAnchor.setDisable(true);
    }

    /**
     *manages the interactions for the planning phase.
     */
    public void planningPhase(){
        allDisable();
        splitsAnimation(rightSplitClosedPosition,bottomSplitOpenPosition).play();
        gui.displayMessage("select an assistant Card");
        bottomAnchor.setDisable(false);
        bottomAnchor.getChildren().forEach(x->x.setDisable(true));
        bottomAnchorController.setAssistantDisable(false);
    }

    /**
     *manages interactions for the action phase.
     */
    public void actionPhase(){
        allDisable();
        gui.displayMessage("move a student or mother nature");
        bottomAnchor.setDisable(false);
        topAnchor.setDisable(false);
        topAnchorController.cloudsPane.setDisable(true);
        topAnchorController.islandsPane.setDisable(false);
        bottomAnchor.getChildren().forEach(x->x.setDisable(false));
        bottomAnchorController.setAssistantDisable(true);
        bottomAnchorController.availableMagicCards.setDisable(false);
        bottomAnchorController.availableMagicCards.getChildren().forEach(x->x.setDisable(false));
    }

    /**
     *manages interactions for selecting clouds.
     */
    public void cloudSelectPhase(){
        allDisable();
        gui.displayMessage("select a cloud");
        topAnchor.setDisable(false);
        topAnchorController.islandsPane.setDisable(true);
        topAnchorController.cloudsPane.setDisable(false);
    }

    /**
     *updates the interface with the information contained in the update message.
     */
    public void update(updateMessage update){
        bottomAnchorController.update(update);
        rightAnchorController.update(update.players);
        topAnchorController.updateClouds(update.clouds);
        topAnchorController.updateIslands(update.islands);
        topAnchorController.updateMother(update.mother);
    }

    /**
     *disables all interactions because it's another player's turn.
     */
    public void waitPhase(){
        bottomAnchor.setDisable(true);
        bottomAnchor.setEffect(new GaussianBlur());
        Text text = new Text("Another player is playing..");
        text.setId("anotherPlayerText");
        text.setX(bottom.getWidth()/2);
        text.setY(bottom.getHeight()/2);
        bottom.getChildren().add(text);
    }

    /**
     *prints an alert message on the screen.
     */
    public void alert(String message){
        Alert alert= new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    /**
     * this method returns a timeline that animates the splits to the positions required.
     * If the position entered is negative it ignores that split.
     * @param rightSplitEndPos end position of rightSplit
     * @param bottomSplitEndPos end position of bottomSplit
     * @return Timeline constructed to animate the splits
     */
    private Timeline splitsAnimation(double rightSplitEndPos, double bottomSplitEndPos){
        Timeline timeline = new Timeline(90);
        KeyValue kv1 = new KeyValue(rightSplit.getDividers().get(0).positionProperty(), rightSplitEndPos, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(bottomSplit.getDividers().get(0).positionProperty(), bottomSplitEndPos, Interpolator.EASE_IN);
        KeyFrame kf1 = new KeyFrame(Duration.millis(150), kv1);
        KeyFrame kf2 = new KeyFrame(Duration.millis(150), kv2);
        if(rightSplitEndPos > 0)
            timeline.getKeyFrames().add(kf1);
        if(bottomSplitEndPos > 0)
            timeline.getKeyFrames().add(kf2);
        return timeline;
    }

    /**
     *when you click on the BottomPanel, it opens.
     */
    @FXML public void onBottomClick(){
        splitsAnimation(rightSplitClosedPosition,bottomSplitOpenPosition).play();
    }

    /**
     *when you click on the RightPanel, it opens.
     */
    @FXML public void onRightClick(){
        splitsAnimation(rightSplitOpenPosition,bottomSplitClosedPosition).play();
    }

    /**
     *when you click on the TopPanel, it opens.
     */
    @FXML public void onTopClick(){
        splitsAnimation(rightSplitClosedPosition,bottomSplitClosedPosition).play();
    }

    /**
     * @param gui
     */
    @Override public void setGui(GUI gui) {
        this.gui = gui;
        bottomAnchorController.setGui(gui);
    }

    /**
     * @param controller
     */
    @Override public void setGuiController(GUIController controller) {
        guiController = controller;
        bottomAnchorController.setGuiController(guiController);
        rightAnchorController.setGuiController(guiController);
        topAnchorController.setGuiController(guiController);
    }
}
