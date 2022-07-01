package it.polimi.ingsw.am54.view.gui.controllers;

import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class GameplayController implements GUIInputHandler {
    private int numPlayers;
    private boolean isAdvancedMode;
    private GUI gui;
    public GameplayController(){}

    @FXML public SplitPane horizSplit;
    @FXML public SplitPane vertSplit;
    @FXML public AnchorPane bottomSplitPane;
    @FXML public AnchorPane rightSplitPane;
    @FXML public AnchorPane topSplitpPane;


    @FXML
    public void initialize(){
        numPlayers = GUI.getNumPlayers();
        isAdvancedMode = GUI.isAdvancedMode();
    }

    @FXML
    public void onMouseEnterRightSplit(){
        double[] dividerPos = horizSplit.getDividerPositions();
        System.out.println("dividerPos[0] "+dividerPos[0]);
    }

    @FXML
    public void onMouseEnterBottomSplit(){
        vertSplit.setDividerPosition(0,0.4);
    }
    @FXML
    public void onMouseExitBottomSplit(){
        vertSplit.setDividerPosition(0,0.8);
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
