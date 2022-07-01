package it.polimi.ingsw.am54.view.gui.controllers;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.gui.GUI;
import it.polimi.ingsw.am54.view.gui.GuiResources;
import javafx.fxml.FXML;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * manages the user's interaction with the gameboard.
 */
public class gameBoardController {
    @FXML public AnchorPane entrance;
    @FXML public AnchorPane towersPane;
    @FXML public AnchorPane hallPane;
    @FXML public AnchorPane professorsPane;
    private int maxHallSelection;
    private int maxEntranceSelection;
    private List<ImageView> entranceSelection = new ArrayList<>();
    private List<ImageView> hallSelection = new ArrayList<>();
    private personalityController personalityController;
    private GUIController guiController;
    private GUI gui;
    private Map<Color, Integer> hallStudents;

    @FXML
    public void initialize(){
        hallStudents = new HashMap<>();
    }

    /**
     * updates the status of the gameboard with the data contained in the update message.
     * @param gb
     */
    public void update(GameBoard gb){
        if(gb == null)
            return;
        setTowers(gb.getTowers());
        setProfessors(gb.getProf());
        setStudentsHall(gb.hall);
        setStudentsEnter(gb.getStudentsEnter());
    }

    /**
     * sets students at the entrance to the gameboard.
     * @param students
     */
    public void setStudentsEnter(List<Color> students){
        for(int i=0; i<entrance.getChildren().size(); i++){
            ImageView imgview = (ImageView) entrance.getChildren().get(i);
            if(i<students.size()){
                String studentPath = getClass().getResource(GuiResources.StudentImage(students.get(i))).toString();
                imgview.setImage(new Image(studentPath));
                imgview.setFitWidth(29);
                imgview.setFitHeight(29);
            } else {
                imgview.setImage(null);
            }
        }
    }

    /**
     * adds a student to the hall.
     * @param students
     */
    public void addStudentsHall(List<Color> students){
        for(Color student: students){
            int n = hallStudents.get(student);
            hallStudents.put(student, n+1);
        }
        setStudentsHall(hallStudents);
    }

    /**
     * sets the students present in the hall.
     * @param students
     */
    private void setStudentsHall(Map<Color, Integer> students){
        hallStudents = students;
        for(Color c : students.keySet()){
            String id = "#"+c.toString().toLowerCase() + "Students";
            AnchorPane studentsPane = (AnchorPane) hallPane.lookup(id);
            for(int i=0; i<studentsPane.getChildren().size(); i++){
                if(i<students.get(c)){
                    studentsPane.getChildren().get(i).setOpacity(1);
                    studentsPane.getChildren().get(i).setDisable(false);
                }
                if(i>=students.get(c)){
                    studentsPane.getChildren().get(i).setOpacity(0);
                    studentsPane.getChildren().get(i).setDisable(true);
                }
            }
        }
    }

    /**
     * sets the professors in input to the gameboard.
     * @param professors
     */
    public void setProfessors(List<Professor> professors){
        professorsPane.getChildren().forEach(x->x.setOpacity(0));
        for (Professor p: professors){
            professorsPane.lookup("#" + p.getColor().toString().toLowerCase()+"Prof").setOpacity(1);
        }
    }

    /**
     * sets the towers in input to the gameboard.
     * @param towers
     */
    public void setTowers(List<Tower> towers){
        for(int i = 0; i<towers.size(); i++){
            String path = GuiResources.towerImage(towers.get(i).getColor());
            Image towImg = new Image(getClass().getResource(path).toString());
            ImageView tow = (ImageView) towersPane.getChildren().get(i);
            tow.setImage(towImg);
        }
        for(int j = towers.size(); towers.size() <= j && j <towersPane.getChildren().size(); j++){
            ImageView tow = (ImageView) towersPane.getChildren().get(j);
            tow.setImage(null);
        }
    }

    /**
     *manages the behavior of the interface following a drag that starts from the entry
     * @param event
     */
    @FXML
    public void onDragFromEntrance(MouseEvent event){
        Dragboard db = entrance.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        ImageView student = (ImageView) event.getSource();
        String stud = student.getId();
        content.putString(stud);
        db.setContent(content);
        db.setDragView(student.getImage(), student.getFitHeight()/2 , student.getFitWidth()/2);
        event.consume();
    }

    /**
     *manages the behavior of the interface and of the system following a drag that starts from the entry and ends on the hall.
     * @param event
     */
    @FXML
    public void onDragHallDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            AnchorPane enter = (AnchorPane) event.getGestureSource();
            ImageView student = (ImageView) enter.lookup("#" + db.getString());

            //get student color
            Color colorSelected = GuiResources.colorFromImage(student.getImage().getUrl());

            //move student to hall in the view
            addStudentsHall(List.of(colorSelected));
            System.out.println("before movestudents");
            GUIController.moveStudents(0, List.of(colorSelected));

            //remove student from enter
            student.setImage(null);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    /**
     /**
     * manages the behavior of the interface and the system following an "empty" drag.
     * @param event
     */
    @FXML
    public void onDragOver(DragEvent event){ //necessary for drag and drop
        if(event.getDragboard().hasString() && GUIController.getStudentToMove() > 0){
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    /**
     * sets the maximum number of students selectable by the entrance.
     * @param max
     */
    public void setMaxEntranceSelection(int max){
        if(max == 0)
            entrance.setEffect(new ColorAdjust(0,-1,0,0));
        maxEntranceSelection = max;
    }

    /**
     * allows you to select a student from the entrance.
     * @param event
     */
    @FXML
    public void enterSelectStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        //Color colorSelected = GuiResources.colorFromImage(student.getImage().getUrl());
        if (entranceSelection.contains(student)){
            student.setEffect(null);
            entranceSelection.remove(student);
        } else {
            if(entranceSelection.size()>= maxEntranceSelection)
                return;
            student.setEffect(new DropShadow());
            entranceSelection.add(student);
        }
    }

    /**
     * sets the maximum number of students selectable by the hall.
     * @param max
     */
    public void setMaxHallSelection(int max){
        if(max == 0)
            hallPane.setEffect(new ColorAdjust(0,-1,0,0));
        maxHallSelection = max;
    }

    /**
     * allows you to select a student from the hall.
     * @param event
     */
    @FXML
    public void hallSelectStudent(MouseEvent event){
        ImageView student = (ImageView) event.getSource();
        //Color colorSelected = GuiResources.colorFromImage(student.getImage().getUrl());
        if (hallSelection.contains(student)){
            student.setEffect(null);
            hallSelection.remove(student);
        } else {
            if(hallSelection.size()>= maxHallSelection)
                return;
            student.setEffect(new DropShadow());
            hallSelection.add(student);
        }
    }

    /**
     * @return students selected by the user
     */
    public List<Color> gethallSelection(){
        return hallSelection.stream().map(x-> GuiResources.colorFromImage(x.getImage().getUrl())).toList();
    }

    /**
     * @return students selected by the user
     */
    public List<Color> getEnterSelection(){
        return entranceSelection.stream().map(x-> GuiResources.colorFromImage(x.getImage().getUrl())).toList();
    }

    /**
     * @param gui
     */
    public void setGui(GUI gui){
        this.gui = gui;
    }

    /**
     * @param controller
     */
    public void setGuiController(GUIController controller) {
        this.guiController = controller;
    }

    /**
     * @param personalityController
     */
    public void setPersonalityController(personalityController personalityController){this.personalityController = personalityController; }
}
