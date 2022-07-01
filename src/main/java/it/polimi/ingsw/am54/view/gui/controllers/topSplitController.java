package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Messages;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GUIInputHandler;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
/**
 * manages the user's interaction with the topSplit,
 * containing the islands and the clouds.
 */
public class topSplitController implements GUIInputHandler{
    private personalityController personalityController;
    private GUIController guiController;
    private GUI gui;
    private int selectedIsland = 0;
    @FXML public AnchorPane islandsPane;
    @FXML public AnchorPane cloudsPane;

    @FXML
    public void initialize(){}

    /**
     *updates the state of mother nature.
     * @param motherPos
     */
    public void updateMother(int motherPos){
        for(Node island: islandsPane.getChildren()){
            if(!island.getId().equals("island" + motherPos)){
                island.lookup("#mother").setOpacity(0);
                island.lookup("#mother").setDisable(true);
            }
            else{
                island.lookup("#mother").setOpacity(1);
                island.lookup("#mother").setDisable(false);
            }
        }
    }

    /**
     *updates the status of the islands.
     * @param islands
     */
    public void updateIslands(List<Island> islands){
        islandsPane.getChildren().forEach(x->{x.setOpacity(0); x.setDisable(true);});
        for(Island island: islands) {
            Node islandNode = islandsPane.lookup("#island"+island.getID());
            islandNode.setOpacity(1);
            islandNode.setDisable(false);
            setIslandTower(islandNode, island.getTowers());
            setIslandStudents(islandNode, island.getStudents());
            ImageView noEntryCard = (ImageView) islandNode.lookup("#noEntryCard");
            Image noEntryImage = new Image(getClass().getResource(GuiResources.YourTurnFalseSimbol).toString());
            noEntryCard.setImage( island.noEntry ? noEntryImage : null);
        }
    }

    /**
     *sets a colorTower on an island.
     * @param islandNode
     * @param towers
     */
    private void setIslandTower(Node islandNode, List<Tower> towers){
        ImageView towerImg = (ImageView) islandNode.lookup("#tower");
        Image towerImage = null;
        if(towers != null && towers.size() != 0 )
            towerImage = new Image(getClass().getResource(GuiResources.towerImage(towers.get(0).getColor())).toString());
        towerImg.setImage(towerImage);
        Text towerText = (Text) islandNode.lookup("#towerNum");
        if(towers == null || towers.size()== 0){
            towerImg.setOpacity(0);
            towerText.setOpacity(0);
        } else if(towers.size() == 1){
            towerImg.setOpacity(1);
            towerText.setOpacity(0);
        } else {
            towerImg.setOpacity(1);
            towerText.setOpacity(1);
            towerText.setText(String.valueOf(towers.size()));
        }
    }

    /**
     *set students on an island.
     * @param islandNode
     * @param students
     */
    private void setIslandStudents(Node islandNode, List<Color> students){
        for(Color color : Color.values()){
            int count = (int) students.stream().filter(x->x.equals(color)).count();
            Text number = (Text) islandNode.lookup("#"+color.toString().toLowerCase()+"StudentsNum");
            number.setText(String.valueOf(count));
            if(count == 0){
                islandNode.lookup("#"+color.name().toLowerCase()+"Student").setOpacity(0);
                number.setOpacity(0);
            } else if(count == 1) {
                islandNode.lookup("#"+color.name().toLowerCase()+"Student").setOpacity(1);
                number.setOpacity(0);
            } else{
                islandNode.lookup("#"+color.name().toLowerCase()+"Student").setOpacity(1);
                number.setOpacity(1);
            }
        }
    }

    /**
     *adds a student to an island.
     * @param island
     * @param student
     */
    private void addStudentToIsland(AnchorPane island, Color student){
        Text number = (Text) island.lookup("#"+student.toString().toLowerCase()+"StudentsNum");
        int count = Integer.parseInt(number.getText()) + 1;
        island.lookup("#"+student.name().toLowerCase()+"Student").setOpacity(1);
        number.setText(String.valueOf(count));
        if(count == 1) {
            number.setOpacity(0);
        } else{
            number.setOpacity(1);
        }
    }

    /**
     *updates the status of the clouds.
     * @param clouds
     */
    public void updateClouds(Map<Integer,List<Color>> clouds){
        if(cloudsPane == null)
            return;
        for (int i=0; i<cloudsPane.getChildren().size(); i++){
            AnchorPane cloud = (AnchorPane) cloudsPane.lookup("#cloud"+i);
            List<Node> slots = cloud.getChildren().filtered(x->!"cloudimg".equals(x.getId()));
            if(clouds.get(i) == null){
                cloud.setDisable(true);
            } else {
                cloud.setDisable(false);
            }
            for(int j=0; j<slots.size(); j++){
                ImageView slot = (ImageView) slots.get(j);
                if(clouds.get(i) == null || j >= clouds.get(i).size()){
                    slot.setImage(null);
                } else {
                    String studPath = GuiResources.StudentImage(clouds.get(i).get(j));
                    Image student = new Image(getClass().getResource(studPath).toString());
                    slot.setImage(student);
                }
            }
        }
    }

    /**
     *manages the archipelago when the player drags something over the islands
     * @param event
     */
    @FXML
    public void onDragOver(DragEvent event){ //necessary for drag and drop
        String dbString = event.getDragboard().getString();
        if (dbString == null) {
            event.consume();
            return;
        }
        if(dbString.contains("Student") && GUIController.getStudentToMove() > 0){
            event.acceptTransferModes(TransferMode.ANY);
            Node islandOver = (Node) event.getSource();
            islandOver.setEffect(new Glow(0.5));
        }
        if(dbString.contains("mother") && !GUIController.motherMoved()) {
            int thisID = Integer.parseInt(((Node)event.getSource()).getId().split("island")[1]);
            int motherID = Integer.parseInt(dbString.split("mother")[1].split("island")[1]);
            int maxMoves = GUIController.getMNmaxMoves();
            int deactivatedIslands = 0;
            for(int id = motherID; id<=thisID; id++){
                if(islandsPane.lookup("#island" + id).isDisable())
                    deactivatedIslands++;
            }
            //minID is the minimum ID that has a distance maxMoves from thisID
            int minID = thisID - maxMoves - deactivatedIslands;
            /*  If minID is positive then motherID has to be between thisID and minID to be a
                possible island to move to.
                If minID is negative then either motherID is bigger than 12 + minID (minID is negative)
                or motherID is less than thisID */
            if(thisID == motherID){
                event.consume();
                return;
            }

            if((minID > 0 && minID <= motherID && motherID <= thisID) ||
                minID <= 0 && (12 + minID <= motherID || motherID <= thisID)){
                event.acceptTransferModes(TransferMode.ANY);
                Node islandOver = (Node) event.getSource();
                islandOver.setEffect(new Glow(0.5));
            }
        }
        event.consume();
    }

    /**
     *manages the archipelago in case of drag exit.
     * @param event
     */
    @FXML
    public void onDragExit(DragEvent event){
        Node islandOver = (Node) event.getSource();
        islandOver.setEffect(null);
        event.consume();
    }

    /**
     *manages the archipelago when the player drags something into an island.
     * @param event
     */
    @FXML
    public void onDragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString() && db.getString().contains("mother")){
            String islandID = "#" + db.getString().split("mother")[1];
            AnchorPane islandFrom = (AnchorPane) islandsPane.lookup(islandID);
            AnchorPane islandTo = (AnchorPane) event.getSource();
            islandFrom.lookup("#mother").setOpacity(0);
            islandTo.lookup("#mother").setOpacity(1);
            int fromID = Integer.parseInt(islandID.split("island")[1]);
            int toID = Integer.parseInt(islandTo.getId().split("island")[1]);
            int deactivatedIslands = 0;
            for(int id = fromID; id<=toID; id++){
                if(islandsPane.lookup("#island" + id).isDisable())
                    deactivatedIslands++;
            }
            int moves =  toID - fromID - deactivatedIslands;
            if( moves < 0 ) moves += 12;
            System.out.println("deactivatedIslands: " + deactivatedIslands);
            System.out.println("toID: " + toID + "fromID" + fromID);
            System.out.println("mother moves: " + moves);
            GUIController.moveMN(moves);
            GUIController.setAdditionalMaxMoves(0);
        }
        if (db.hasString() && db.getString().contains("Student")) {
            AnchorPane enter = (AnchorPane) event.getGestureSource();
            ImageView student = (ImageView) enter.lookup("#" + db.getString());
            //move student to island clientSide
            Color colorSelected = GuiResources.colorFromImage(student.getImage().getUrl());
            AnchorPane island = (AnchorPane) event.getSource();
            addStudentToIsland(island, colorSelected);

            int islandID = Integer.parseInt(island.getId().split("island")[1]);
            GUIController.moveStudents(islandID, List.of(colorSelected));

            //remove student from enter
            student.setImage(null);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     * @param controller
     */
    public void setPersonalityController(personalityController controller){
        this.personalityController = controller;
    }

    /**
     *allows to select an island.
     * @param event
     */
    @FXML
    public void selectIsland(MouseEvent event){
        AnchorPane island = (AnchorPane) event.getSource();
        AnchorPane islandPrec = (AnchorPane) islandsPane.lookup("#island"+selectedIsland);
        island.setEffect(new DropShadow());
        if(islandPrec != null && !islandPrec.equals(island))
            islandPrec.setEffect(null);

        int islandId = Integer.parseInt(island.getId().split("island")[1]);
        selectedIsland = islandId;
    }

    /**
     * @return selectedIslandID
     */
    public int getSelectedIslandID(){
        return selectedIsland;
    }

    /**
     *allows to select a cloud.
     * @param event
     */
    @FXML
    public void selectCloud(MouseEvent event){
        AnchorPane anchor = (AnchorPane) event.getSource();
        int cloudID = Integer.parseInt(anchor.getId().split("cloud")[1]);
        anchor.setEffect(new DropShadow());
        try {
            Client.command(Messages.selectCloud, cloudID);
            cloudsPane.setDisable(true);
            guiController.waitPhase();
        } catch (ServerErrorException e) {
            e.printStackTrace();
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
        this.guiController = controller;
    }
}
