package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.TColor;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Mage;
import it.polimi.ingsw.am54.network.Messages;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;

import java.io.IOException;
import java.util.List;

/**
 * manages the interaction with the user when selecting the lobby settings.
 */
public class InitialPhaseController implements GUIInputHandler {
    private int numPlayers;
    private boolean isAdvancedMode;
    private static Thread thread;
    private GUIController mainController;
    private GUI gui;
    public InitialPhaseController(){} //this is for fxml
    public void nextScene(){
        //not very elegant but very easy to read, maybe will do a better one later
        switch (gui.getCurrentScenePath()){
            case GuiResources.startScreen -> gui.setScene(GuiResources.gameOptions);
            case GuiResources.gameOptions -> {
                gui.setScene(GuiResources.playerOptions);
                //updateUnavailableMages(); //i wish this worked but it don't
            }
            case GuiResources.playerOptions -> {
                gui.setScene(GuiResources.lobby);
                thread = new Thread(() -> GUI.getGuiController().startGame());
                thread.start();
            }
        }
    }

    public List<Mage> availableMages;
    public Mage selectedMage;
    @FXML public TextField ipTextField;
    @FXML public TextField serverTextField;
    @FXML public TextField nameTextField;
    @FXML public ChoiceBox<TColor> towerColorChoice;
    @FXML public CheckBox advancedModeSelection;
    @FXML public ToggleGroup numOfPlayersSelection;
    @FXML public Polygon continueButton;
    @FXML public AnchorPane magePane;
    @FXML public void choiceYellowMage(){ setAndUpdateMage(Mage.YellowKing); }
    @FXML public void choiceGreenMage(){ setAndUpdateMage(Mage.HerbMagician); }
    @FXML public void choiceBlueMage(){ setAndUpdateMage(Mage.SkyMagician); }
    @FXML public void choicePurpleMage(){ setAndUpdateMage(Mage.VioletWitch); }

    /**
     * general management of mages, through the updateSelectedMage and updateUnavailableMages methods.
     * @param select
     */
    public void setAndUpdateMage(Mage select){
        selectedMage = select;
        updateSelectedMage();
        updateUnavailableMages();
    }

    /**
     * disables interaction with unavailable mages and place a color effect on them.
     */
    public void updateUnavailableMages(){
        availableMages = Client.getAvailableMages();
        List<Node> images = magePane.getChildren()
                .filtered(x -> x instanceof ImageView);
        for (Node img : images){
            if(!availableMages.contains(Mage.valueOf(img.getId()))){
                img.setDisable(true);
                img.setEffect(new ColorAdjust(0.0, -1.0 , -0.3, 0.0));
                img.setOpacity(0.5);
            }
        }
        if(!availableMages.contains(selectedMage)){
            selectedMage = null;
        }
    }

    /**
     * checks if the user chooses a mage and applies a color effect over it.
     */
    public void updateSelectedMage(){
        List<Node> images = magePane.getChildren()
                .filtered(x -> x instanceof ImageView && !x.isDisable());
        for (Node img : images){
            if(selectedMage!= null && img.getId().equals(selectedMage.name())){
                img.setEffect(new DropShadow(10, new Color(0.0, 0.0, 0.0, 1.0)));
            } else {
                img.setEffect(null);
            }
        }
    }


    /**
     *attempts to connect with the server.
     * @param event
     */
    @FXML
    public void tryconnect(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            try {
                int ip = Integer.parseInt(ipTextField.getText());
                Client.connectToServer(serverTextField.getText(), ip);
                nextScene();
            } catch (IOException e) {
                gui.displayMessage("couldn't connect to server");
            } catch (NumberFormatException e){
                gui.displayMessage("the ip has to be a number");
            }
        }
    }

    /**
     * pass user choices to the gui and try to join a game.
     * @param event
     */
    @FXML
    public void joingame(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            try {
                RadioButton selected = (RadioButton) numOfPlayersSelection.getSelectedToggle();
                numPlayers = Integer.parseInt(selected.getText());
                isAdvancedMode = advancedModeSelection.isSelected();
                GUI.setNumPlayers(numPlayers);
                GUI.setAdvancedMode(isAdvancedMode);
                String options = numPlayers + " " + isAdvancedMode;
                GUI.setPlayerID(Client.joinGame(options));
                nextScene();
            } catch (ServerErrorException e){
                gui.displayMessage("unable to join game because: \n" + e.getMessage());
            }
        }
    }

    /**
     * check that the user has made all the choices correctly.
     * @param event
     */
    @FXML
    public void checkPlayerOptions(MouseEvent event){
        if(selectedMage == null){
            gui.displayMessage("please select a mage");
            return;
        }
        if(towerColorChoice.getValue() == null){
            gui.displayMessage("please select a valid tower");
            return;
        }
        if(nameTextField.getText() == null){
            gui.displayMessage("please insert a name");
            return;
        }
        if(event.getButton().equals(MouseButton.PRIMARY)) {
            try {
                Client.command(Messages.selectMage, selectedMage);
                GUIController.setMage(selectedMage);
                magePane.setDisable(true);
                Client.command(Messages.selectTower, towerColorChoice.getValue());
                towerColorChoice.setDisable(true);
                Client.command(Messages.setUsername, nameTextField.getText());
                gui.setName(nameTextField.getText());
                nextScene();
            } catch (ServerErrorException e) {
                gui.displayMessage(e.getMessage());
                updateUnavailableMages();
            }
        }
    }


    /**
     * manages the choice of towers.
     * @param event
     */
    @FXML
    public void checkTower(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            towerColorChoice.getItems().setAll(Client.getAvailableTowerColors());
            towerColorChoice.show();
            updateUnavailableMages();
        }
    }

    /**
     * manages the visual effects of the "forward" button.
     * @param event
     */
    @FXML
    public void exagonalButtonGraphics(MouseEvent event){
        EventType<? extends MouseEvent> eventType = event.getEventType();
        if (MouseEvent.MOUSE_ENTERED.equals(eventType)) {
            continueButton.setFill(new LinearGradient(
                    0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(0.637, 0.0, 0.5351, 1.0)),
                    new Stop(1.0, new Color(0.335, 0.0, 0.786, 1.0))));
        } else if (MouseEvent.MOUSE_EXITED.equals(eventType)) {
            continueButton.setFill(new LinearGradient(
                    0.0, 0.0, 1.0, 1.0, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, new Color(0.437, 0.0, 0.5351, 1.0)),
                    new Stop(1.0, new Color(0.2363, 0.0, 0.886, 1.0))));
        }
    }

    /**
     * @param gui
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * @param controller
     */
    @Override
    public void setGuiController(GUIController controller) {
        mainController = controller;
    }

    public static void end(){
        thread.interrupt();
    }
}
