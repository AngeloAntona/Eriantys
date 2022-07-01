package it.polimi.ingsw.am54.view.gui;
import it.polimi.ingsw.am54.model.Card;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.model.TColor;
import javafx.stage.Screen;

import java.util.Arrays;

public final class GuiResources {
    //general
    public final static double displayWidth = Screen.getPrimary().getBounds().getWidth();
    public final static double displayHeight = Screen.getPrimary().getBounds().getHeight();

    //initial phase
    public final static String startScreen = "/fxmls/start_screen.fxml";
    public final static String playerOptions = "/fxmls/player_options_screen.fxml";
    public final static String gameOptions = "/fxmls/game_options_screen.fxml";
    public final static String lobby = "/fxmls/lobby.fxml";

    //main game
    public static String topSplit(int numPlayers){
        return "/fxmls/TopSplits/top_split_" + numPlayers + "players";
    }
    public static String bottomSplit(boolean isAdvancedMode){
        return "/fxmls/BottomSplits/bottom_split" + (isAdvancedMode? "_AdvMode" : "") + ".fxml";
    }
    public static String rightSplit(int numPlayers, boolean isAdvancedMode){
        String url = "/fxmls/RightSplits/right_split_" + numPlayers + "players";
        return url + (isAdvancedMode? "_AdvMode" : "") + ".fxml";
    }
    public static String mainScreen(int numPlayers, boolean isAdvancedMode){
        String url =  "/fxmls/MainScreens/main_screen_" + numPlayers + "players";
        return url + (isAdvancedMode? "_AdvMode" : "") + ".fxml";
    }
    public static String YourTurnFalseSimbol = "/GUI_images/redX.png";
    public static String YourTurnTrueSimbol  = "/GUI_images/its_his_turn.png";
    public static String professorImage(Color profColor){
        return "/GUI_images/wooden_pieces/teacher_" + profColor.toString().toLowerCase() + ".png";
    }

    public static String towerImage(TColor tower){
        return "/GUI_images/wooden_pieces/" + tower.toString().toLowerCase() + "_tower.png";
    }

    public static String gettowerImage(TColor tower){
        switch (tower){
            case WHITE -> { return WhiteTowerImage; }
            case GREY -> { return GrayTowerImage; }
            case BLACK -> { return BlackTowerImage; }
        }
        return null;
    }

    public static String BlackTowerImage     = "/GUI_images/wooden_pieces/black_tower.png";
    public static String WhiteTowerImage     = "/GUI_images/wooden_pieces/white_tower.png";
    public static String GrayTowerImage      = "/GUI_images/wooden_pieces/grey_tower.png";
    public static String PersonalityImage(String personalityName){
        return "/GUI_images/graphical_assets/Personaggi/"+personalityName+".jpg";
    }
    public static String StudentImage(Color student){
        return "/GUI_images/wooden_pieces/student_"+student.toString().toLowerCase()+".png";
    }
    public static Color colorFromImage(String imgPath){
        String color = imgPath.substring(imgPath.length()-12).split("_")[1].split("\\.")[0];
        return Color.valueOf(color.toUpperCase());
    }
    public static String assistantCardImage(Card assistant){
        return "/GUI_images/graphical_assets/Assistenti/2x/Assistente ("+assistant.getMaxMoves()+").png";
    }
    public static Card assistantCardFromImage(String imgPath){
        String toparse = imgPath.substring(imgPath.length()-10);
        int cardValue = Integer.parseInt(toparse.split("\\(")[1].split("\\)")[0]);
        return new Card(cardValue);
    }
    public static String personality(String name){
        return "/fxmls/Personality/"+name+".fxml";
    }

    public static String personalityNameFromImage(String imgPath){
        return imgPath.substring(imgPath.length()-15).split("/")[1].split("\\.")[0];
    }

    //variables
    public static double scalefactor = displayWidth/1740;
    public static double bottomSplitClosedPosition = 0.85 * scalefactor;
    public static double bottomSplitOpenPosition = 0.4 * (1-scalefactor);
    public static double rightSplitClosedPosition = 0.82;
    public static double rightSplitOpenPosition = 0.4 * (1-scalefactor);
}
