package it.polimi.ingsw.am54.view.gui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

public class GameplayController implements GUIInputHandler {
    private int numPlayers;
    private boolean isAdvancedMode;
    private GUI gui;
    public GameplayController(){}
    public GameplayController(GUI gui) {
        this.gui = gui;
    }

    public void initialize(int numPlayers, boolean isAdvancedMode){
        this.numPlayers = numPlayers;
        this.isAdvancedMode = isAdvancedMode;
        gui.setScene(GuiResources.mainScreen(numPlayers, isAdvancedMode));
    }


    @FXML public SplitPane horizSplit;
    @FXML public SplitPane vertSplit;

    @FXML
    public void onMouseEnterRightSplit(){
        double[] dividerPos = horizSplit.getDividerPositions();
        System.out.println("dividerPos[0] "+dividerPos[0]+" dividerPos[1]"+dividerPos[1]);
        //horizSplit.setDividerPosition(1,);
    }

    public void resetPosition(){
        gui.resetPosition();
        horizSplit.setDividerPosition(1,0.8);
        vertSplit.setDividerPosition(1,0.8);
    }

    @FXML
    public void fullScreen(){
        resetPosition();
        GUI.getStage().setFullScreen(true);
    }

    @FXML
    public void prova(){

    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
