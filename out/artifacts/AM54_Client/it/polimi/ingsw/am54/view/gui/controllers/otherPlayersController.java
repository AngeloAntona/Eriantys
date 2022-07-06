package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.List;

/**
 * manages the view of other players' status.
 */
public class otherPlayersController implements GUIInputHandler {
    @FXML public Text userName;
    @FXML public Text numIslands;
    @FXML public Text magicCard;
    @FXML public Text Coins;
    @FXML public AnchorPane professors;
    @FXML public ImageView towerColor;
    @FXML public ImageView assistantCard;
    @FXML public gameBoardController gameboardController;
    private GUIController guiController;
    private GUI gui;

    @FXML
    public void initialize(){
    }

    /**
     * @param userName
     */
    public void setUserName(String userName){
        this.userName.setText(userName);
    }

    /**
     * @param card
     */
    public void setMagicCard(Personality card){
        magicCard.setText(card.getName());
    }

    /**
     * @param n
     */
    public void setNumIslands(int n){
        numIslands.setText(String.valueOf(n));
    }

    /**
     * updates the view of other players' status.
     * @param player
     */
    public void update(Player player){
        //TODO update the rest
        Coins.setText(String.valueOf(player.getGameBoard().getCoins()));
        setProfessors(player.getGameBoard().getProf());
        //this relies on the fact that getTower().get(0) is not null
        setTowerColor(player.getGameBoard().getTowers().get(0).getColor());
        setAssistant(player.getHand().getCardPlayed());
        gameboardController.update(player.getGameBoard());
    }

    /**
     * sets the professors.
     * @param listProf
     */
    private void setProfessors(List<Professor> listProf){
        professors.getChildren().forEach(x->x.setOpacity(0));
        for(Professor p: listProf){
            professors.lookup("#"+p.getColor().toString().toLowerCase()+"Prof").setOpacity(1);
        }
    }

    /**
     *sets the tower color.
     * @param color
     */
    private void setTowerColor(TColor color){
        String url = String.valueOf(getClass().getResource(GuiResources.towerImage(color)));
        if(url.equals("null")){
            towerColor.setImage(new Image(getClass().getResource(GuiResources.YourTurnFalseSimbol).toString()));
            return;
        }
        towerColor.setImage(new Image(url));
    }

    /**
     * sets the assistant card.
     * @param cardPlayed
     */
    private void setAssistant(Card cardPlayed){
        if(cardPlayed == null){
            assistantCard.setImage(null);
            return;
        }
        String url = getClass().getResource(GuiResources.assistantCardImage(cardPlayed)).toString();
        assistantCard.setImage(new Image(url));
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
        this.guiController = controller;
    }
}