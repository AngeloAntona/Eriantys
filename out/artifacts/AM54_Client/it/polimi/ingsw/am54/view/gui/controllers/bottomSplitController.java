package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Messages;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * manages the user's interaction with the bottomSplit,
 * containing his gameboard, the magic cards, the assistant cards and the possible moves.
 */
public class bottomSplitController implements GUIInputHandler {
    @FXML public Text coinsText;
    @FXML public ImageView YouTurnImage;
    @FXML public ImageView towerImage;
    @FXML public AnchorPane advancedModePane;
    @FXML public AnchorPane notAdvancedModePane;
    @FXML public ImageView magicCardPlayed;
    @FXML public AnchorPane availableMagicCards;
    @FXML public AnchorPane assistantCardsPane;
    @FXML public Line lineDivider;
    @FXML public gameBoardController gameboardAnchorController;
    private List<personalityController> personalityControllers = new ArrayList<>();
    private GUIController guiController;
    private GUI gui;

    @FXML public void initialize(){}
    /**
     * @return minimum expansion of the bottom panel
     */
    public double getMinHeight(){
        return lineDivider.getLayoutY();
    }

    /**
     * Disable interaction with the assistant card panel
     */
    public void setAssistantDisable(boolean assistantDisable){
        assistantCardsPane.setDisable(assistantDisable);
    }

    /**
     * makes the advanced mode options visible if the game is in advanced mode.
     * @param isAdvancedMode
     */
    public void setAdvancedMode(boolean isAdvancedMode){
        if(isAdvancedMode){
            advancedModePane.setOpacity(1);
            advancedModePane.setDisable(false);
            notAdvancedModePane.setOpacity(0);
            notAdvancedModePane.setDisable(true);
        } else {
            advancedModePane.setOpacity(0);
            advancedModePane.setDisable(true);
            notAdvancedModePane.setOpacity(1);
            notAdvancedModePane.setDisable(false);
        }
    }


    /**
     * TODO javaDoc
     */
    public void update(updateMessage update){
        Player myPlayer = update.players.stream()
                .filter(x->x.getPlayerId() == GUI.getPlayerID())
                .findAny().orElse(null);
        if (myPlayer == null){
            System.out.println("player not present in updateMessage");
            return;
        }
        gameboardAnchorController.update(myPlayer.getGameBoard());
        List<Personality> personalities = new ArrayList<>();
        personalities.addAll(update.containers);
        personalities.addAll(update.modifiers);
        setAvailablePersonalities(personalities);
        setTowerColor(myPlayer.getGameBoard().getTowers().get(0).getColor());
        setCoins(myPlayer.getGameBoard().getCoins());
        updatePersonalityScreens(update, myPlayer);
        //TODO setYourTurn
    }

    /**
     * sets available personality cards images from personality cards name.
     * it expects a list of 3 cards, if the list is longer the other elements will be ignored,
     * if the list is shorter the missing elements will not be replaced (will stay the same as before)
     * @param cards list of cards to be displayed
     */
    public void setAvailablePersonalities(List<Personality> cards){
        /*
        if(personalityControllers.size() == 0){
            personalityControllers = cards.stream().map(x->(personalityController)gui.loadScene(GuiResources.personality(x.getName().toLowerCase()))).toList();
        }*/
        for (int i=0; i<3 && i<cards.size(); i++){
            Personality card = cards.get(i);
            if(personalityControllers.size()<=i)
                personalityControllers.add((personalityController) gui.loadScene(GuiResources.personality(cards.get(i).getName().toLowerCase())));
            personalityControllers.get(i).setCost(card.getCost());
            System.out.println("personalityController: " + personalityControllers.get(i).Cost.getText());
            ImageView img = (ImageView) availableMagicCards.getChildren().get(i);
            String path = GuiResources.PersonalityImage(card.getName());
            img.setImage(new Image(getClass().getResource(path).toString()));
        }
    }

    public void updatePersonalityScreens(updateMessage update, Player myPlayer){
        for(personalityController personality: personalityControllers){
            personality.updateIslands(update);
            personality.updateGameBoard(myPlayer.getGameBoard());
        }
    }

    /**
     * set the color of the player's Towers.
     * @param color
     */
    public void setTowerColor(TColor color){
        String imgPath = GuiResources.towerImage(color);
        towerImage.setImage(new Image(getClass().getResource(imgPath).toString()));
    }

    /**
     * Set the number of coins.
     * @param coins
     */
    public void setCoins(Integer coins){
        coinsText.setText(coins.toString());
    }

    /**
     * if it's your turn sets the "yourTurnSymbol", else sets the "notYourTurnSymbol".
     * @param isYourTurn
     */
    public void setYourTurn(boolean isYourTurn){
        String imgPath = isYourTurn ? GuiResources.YourTurnTrueSimbol : GuiResources.YourTurnFalseSimbol;
        Image yourTurnimg = new Image(getClass().getResource(imgPath).toString());
        YouTurnImage.setImage(yourTurnimg);
    }

    /**
     * reaction to a click on an assistant card
     * @param event
     */
    @FXML
    public void onAssistanceCardClicked(MouseEvent event){
        ImageView card = (ImageView) event.getSource();
        card.setDisable(true);
        card.setEffect(new ColorAdjust(0,-1,-0.2,0));
        Card assistanceSelected = GuiResources.assistantCardFromImage(card.getImage().getUrl());
        try {
            Client.command(Messages.selectAssistantCard, assistanceSelected);
            GUIController.setMNMaxMoves(assistanceSelected.MNMaxMoves);
            assistantCardsPane.setDisable(true);
            guiController.getPhaseManager().waitState();
        } catch (ServerErrorException e) {
            gui.displayMessage("card not available, please select another card");
            //e.printStackTrace();
        }
    }

    /**
     * //todo javaDoc
     * @param event
     */
    @FXML
    public void onMagicCardClicked(MouseEvent event) {
        ImageView card = (ImageView) event.getSource();
        String personality = GuiResources.personality(GuiResources.personalityNameFromImage(card.getImage().getUrl()));
        personalityControllers.forEach(personalityController::firstscreen);
        gui.setPopUpScene(personality);
    }

    public void setMagicCardPlayed(String personalityName) {
        Image image = new Image(getClass().getResource(GuiResources.personality(personalityName)).toString());
        magicCardPlayed.setImage(image);
    }

    /**
     * @param gui
     */
    @Override
    public void setGui(GUI gui) { this.gui = gui; }

    /**
     * @param controller
     */
    @Override
    public void setGuiController(GUIController controller) { this.guiController = controller; }
}
