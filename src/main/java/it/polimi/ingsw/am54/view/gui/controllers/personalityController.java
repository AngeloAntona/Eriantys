package it.polimi.ingsw.am54.view.gui.controllers;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Messages;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * manages user interaction with magic cards.
 */
public class personalityController implements GUIInputHandler {
    private int islandChosen = 0;
    private GUI gui;
    private GUIController guiController;
    private List<ImageView> jesterSelection = new ArrayList<>();
    private Color gluttonSelection;
    private ImageView courtesanSelection;
    private ImageView botanistSelection;
    private ImageView winemakerSelection;
    private ImageView witchSelection;
    private Personality personality;
    @FXML public Text Cost;
    @FXML public AnchorPane tessereDivietoPane;
    @FXML public AnchorPane studentsPane;
    @FXML public AnchorPane gluttonPanel;
    @FXML public AnchorPane firstscreen;
    @FXML public AnchorPane secondscreen;
    @FXML public AnchorPane gameboard;
    @FXML public gameBoardController gameboardController;
    @FXML public topSplitController islandsController;

    @FXML
    public void initialize(){
        if(islandsController != null)
            islandsController.setPersonalityController(this);
        if(gameboardController != null)
            gameboardController.setPersonalityController(this);


    }

    public void setPersonality(Personality personality){
        this.personality = personality;
        //Botanist update
        if(tessereDivietoPane != null && personality.name == "botanist"){
            int divieto = ((Containers) personality).noEntry;
            System.out.println("tessere divieto: " + divieto);
            for(int i=0; i<tessereDivietoPane.getChildren().size(); i++){
                Node n = tessereDivietoPane.getChildren().get(i);
                if(i<divieto){
                    n.setOpacity(1);
                    n.setDisable(false);
                } else {
                    n.setOpacity(0);
                    n.setDisable(true);
                }
            }
        }
        //containers students update
        if(studentsPane != null){
            Containers pers = (Containers) personality;
            for(int i=0; i<studentsPane.getChildren().size(); i++){
                ImageView imageView = (ImageView) studentsPane.getChildren().get(i);
                if(i < pers.getStudents().size()){
                    String colorname = GuiResources.StudentImage(pers.getStudents().get(i));
                    System.out.println("color: " + colorname);
                    Image img = new Image(getClass().getResource(colorname).toString());
                    imageView.setImage(img);
                } else {
                    imageView.setImage(null);
                }
            }
        }
    }

    public void updateIslands(updateMessage update){
        if(islandsController != null){
            islandsController.updateIslands(update.islands);
            islandsController.updateClouds(update.clouds);
            islandsController.updateMother(update.mother);
        }
    }
    public void updateGameBoard(GameBoard gameBoard){
        if(gameboardController != null)
            gameboardController.update(gameBoard);
    }

    public void firstscreen(){
        if(firstscreen != null){
            firstscreen.setMouseTransparent(false);
            firstscreen.setVisible(true);
            firstscreen.setDisable(false);
        }
        if(secondscreen != null){
            secondscreen.setOpacity(0);
            secondscreen.setMouseTransparent(true);
            secondscreen.setDisable(true);
            secondscreen.setVisible(false);
        }
    }

    /**
     * allows you to buy a magic card.
     * @param personalityName
     */
    public void buyPersonality(String personalityName){
        JsonObject object = new JsonObject();
        buyPersonality(personalityName, object);
    }

    /**
     * used by buyPersonality to actually buy the magic card.
     * @param personalityName
     * @param object
     */
    public void buyPersonality(String personalityName, JsonObject object){
        object.addProperty("card", personalityName);
        try {
            Client.command(Messages.usePersonality, object);
            GUIController.getMainController().bottomAnchorController.setMagicCardPlayed(personalityName);
            Client.receiveSelfUpdate();
        } catch (ServerErrorException e) {
            gui.displayMessage("personality not available");
        }
        gui.closePopUp();
    }

    @FXML public void archerBuy(){
        buyPersonality("archer");
        GUIController.setAdditionalMaxMoves(2);
    }
    @FXML public void bakerBuy(){ buyPersonality("baker"); }
    @FXML public void faunBuy(){ buyPersonality("faun"); }
    @FXML public void knightBuy(){ buyPersonality("knight"); }

    /**
     * manages the "first" interaction with the Cantor card.
     * @param event
     */
    @FXML
    public void cantorConfirm(ActionEvent event){
        JsonObject object = new JsonObject();
        JsonArray fromHall = new JsonArray();
        JsonArray fromEntrance = new JsonArray();
        gameboardController.gethallSelection().forEach(x->fromHall.add(x.toString()));
        gameboardController.getEnterSelection().forEach(x->fromEntrance.add(x.toString()));
        System.out.println("cantor: ");
        System.out.println("hallselection: " + gameboardController.gethallSelection());
        System.out.println("enterselection: " + gameboardController.getEnterSelection());
        object.add("studentsFromHall", fromHall);
        object.add("studentsFromEntrance", fromEntrance);
        buyPersonality("cantor", object);
    }

    /**
     *manages the "first" interaction with the Pirate card.
     * @param event
     */
    @FXML
    public void pirateConfirm(ActionEvent event){
        JsonObject object = new JsonObject();
        int islandId = islandsController.getSelectedIslandID();
        System.out.println("pirate: " + islandId);
        object.addProperty("island", islandId);
        buyPersonality("pirate", object);
    }
    /**
     *manages the "first" interaction with the Jester card.
     * @param event
     */
    @FXML
    public void jesterConfirm(ActionEvent event){
        JsonArray fromCard = new JsonArray();
        JsonArray fromEntrance = new JsonArray();
        JsonObject object = new JsonObject();
        List<Color> boardSelection = gameboardController.getEnterSelection();
        List<Color> colorSelection = jesterSelection.stream().map(x-> GuiResources.colorFromImage(x.getImage().getUrl())).toList();
        System.out.println("jester colors: " + colorSelection);
        System.out.println("jester board colors: " + boardSelection);
        colorSelection.forEach(x->fromCard.add(x.toString()));
        boardSelection.forEach(x->fromEntrance.add(x.toString()));
        object.add("studentsFromCard", fromCard);
        object.add("studentsFromEntrance", fromEntrance);
        buyPersonality("jester", object);
    }

    /**
     *manages the "first" interaction with the Botanist card.
     * @param event
     */
    @FXML
    public void botanistConfirm(ActionEvent event){
        System.out.println(islandsController.getSelectedIslandID());
        JsonObject object = new JsonObject();
        object.addProperty("island", islandsController.getSelectedIslandID());
        buyPersonality("botanist", object);
    }

    /**
     *manages the "first" interaction with the Glutton card.
     */
    @FXML
    public void gluttonConfirmSelection(){
        System.out.println(gluttonSelection);
        JsonObject object = new JsonObject();
        object.addProperty("color", gluttonSelection.toString());
        buyPersonality("glutton", object);
    }

    /**
     *manages the "first" interaction with the Courtesan card.
     */
    @FXML
    public void courtesanConfirmSelection(){
        Color selectedColor = GuiResources.colorFromImage(courtesanSelection.getImage().getUrl());
        System.out.println(selectedColor);
        JsonObject object = new JsonObject();
        object.addProperty("color", selectedColor.toString());
        buyPersonality("courtesan", object);
    }

    /**
     *manages the "first" interaction with the Witch card.
     * @param event
     */
    @FXML
    public void witchConfirm(ActionEvent event){
        JsonObject object = new JsonObject();
        Color selectedColor = GuiResources.colorFromImage(witchSelection.getImage().getUrl());
        System.out.println("witchselection:" + selectedColor);
        object.addProperty("color", selectedColor.toString());
        buyPersonality("witch", object);
    }

    /**
     *manages the "first" interaction with the Winemaker card.
     * @param event
     */
    @FXML
    public void winemakerConfirm(ActionEvent event){
        JsonObject object = new JsonObject();
        Color selectedColor = GuiResources.colorFromImage(winemakerSelection.getImage().getUrl());
        int islandId = islandsController.getSelectedIslandID();
        System.out.println("winemaker: " + selectedColor);
        System.out.println("winemaker: " + islandId);
        object.addProperty("island", islandId);
        object.addProperty("color", selectedColor.toString());
        buyPersonality("winemaker", object);
    }

    /**
     *manages the "selection" interaction with the Jester card.
     * @param event
     */
    @FXML
    public void jesterSelectStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        if(jesterSelection.contains(student)){
            student.setEffect(null);
            jesterSelection.remove(student);
        } else {
            if(jesterSelection.size()>=3)
                return; //maybe add a text that says "max 3 students"
            student.setEffect(new DropShadow());
            jesterSelection.add(student);
        }
        gameboardController.setMaxHallSelection(0);
        gameboardController.setMaxEntranceSelection(jesterSelection.size());
        gameboardController.towersPane.setEffect(new ColorAdjust(0,-1,0,0));
        gameboardController.professorsPane.setEffect(new ColorAdjust(0,-1,0,0));
        System.out.println("size: " + jesterSelection.size());
        event.consume();
    }

    /**
     *manages the "selection" interaction with the Glutton card.
     * @param event
     */
    @FXML
    public void gluttonSelect(MouseEvent event){
        Node colorNode = (Node) event.getSource();
        Node prevSelection = gluttonPanel.lookup("#"+String.valueOf(gluttonSelection).toLowerCase());
        Color selection = Color.valueOf(colorNode.getId().toUpperCase());
        if(gluttonSelection!= null && prevSelection!= null)
            prevSelection.setEffect(null);
        colorNode.setEffect(new DropShadow());
        gluttonSelection = selection;
    }

    /**
     *manages the "selection" interaction with the Courtesan card.
     * @param event
     */
    @FXML
    public void courtesanSelect(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        if(courtesanSelection != null)
            courtesanSelection.setEffect(null);
        courtesanSelection = student;
        courtesanSelection.setEffect(new DropShadow());
    }

    /**
     *manages the "selection" interaction with the Botanist card.
     * @param event
     */
    @FXML
    public void botanistSelect(MouseEvent event){
        ImageView tessera = (ImageView) event.getSource();
        if(botanistSelection != null)
            botanistSelection.setEffect(null);
        botanistSelection = tessera;
        botanistSelection.setEffect(new DropShadow());
    }

    /**
     * manages the passage to the next screen of the interaction interface of the cantor card.
     * @param event
     */
    @FXML
    public void cantorNextScreen(ActionEvent event){
        gameboardController.setMaxHallSelection(2);
        gameboardController.setMaxEntranceSelection(2);
        nextScreen();
    }

    /**manages the "selection" interaction with the Winemaker card.
     * @param event
     */
    @FXML
    public void winemakerSelect(MouseEvent event){
        ImageView tessera = (ImageView) event.getSource();
        if( winemakerSelection != null)
            winemakerSelection.setEffect(null);
        winemakerSelection = tessera;
        winemakerSelection.setEffect(new DropShadow());
    }

    /**manages the "selection" interaction with the Witch card.
     * @param event
     */
    @FXML
    public void witchSelect(MouseEvent event){
        ImageView tessera = (ImageView) event.getSource();
        if( witchSelection != null)
            witchSelection.setEffect(null);
        witchSelection = tessera;
        witchSelection.setEffect(new DropShadow());
    }

    /**
     * manages the passage to the next screen of the interaction with the cards.
     */
    @FXML
    public void nextScreen(){
        firstscreen.setMouseTransparent(true);
        firstscreen.setVisible(false);
        firstscreen.setDisable(true);
        secondscreen.setOpacity(1);
        secondscreen.setMouseTransparent(false);
        secondscreen.setDisable(false);
        secondscreen.setVisible(true);
    }

    /**
     * set the cost of the cards
     * @param n
     */
    public void setCost(int n) { Cost.setText(String.valueOf(n)); }

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
