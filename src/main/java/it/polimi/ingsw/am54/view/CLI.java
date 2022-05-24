package it.polimi.ingsw.am54.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.Mage;
import it.polimi.ingsw.am54.network.updateMessage;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * This class contains all the methods to interface with a CLI client
 */
public class CLI {
    private Scanner userInput;
    private final PrintStream out;

    int  id;
    private  static int movedInTurn = 0;
    private  GameBoard myGameBoard = null;
    private  ArrayList<GameBoard> othersGameBoards;
    private  ArrayList<Card> othersPlayedCards;
    private List<Island> islands;
    protected  ArrayList<Card> cardsInHand;
    protected  Card cardPlayed;
    private List<String> availableCommands;
    private List<Personality> personalities;
    private boolean advanced;
    private int motherNature;
    private  Map<Integer, List<Color>> clouds;
    private static final Gson gson = new GsonBuilder().create();

    public CLI() {
        this.out = System.out;
        this.userInput = new Scanner(System.in);
        this.cardPlayed = null;
        this.myGameBoard = null;
        this.othersGameBoards = new ArrayList<>();
        this.othersPlayedCards = new ArrayList<>();
        this.personalities = new ArrayList<>();
        this.islands = new ArrayList<>();
        this.id = 0;
    }

    public void displayWait(){
        displayMessage("\rplease wait for your turn");
    }

    /**
     * method to display assistant Cards with a format
     * @param cards cards to display
     */
    public void displayAssistantCards(List<Card> cards){
        String leftAlignFormat = "| %-10d | %-11d | %-8d |%n";
        out.format("+------------+-------------+----------+%n");
        out.format("| Card index | Card values | MN moves |%n");
        out.format("+------------+-------------+----------+%n");
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            out.format(leftAlignFormat,i, c.getValue(), c.getMaxMoves());
        }
        out.format("+------------+-------------+----------+%n");
    }

    /**
     * prints to screen without new line
     * @param message message to print
     */
    public void displayString(String message){
        out.print(message);
    }

    /**
     * prints to screen with new line
     * @param message message to print
     */
    public void displayMessage(String message){
        out.println(message);
    }


    public String getLine() {
        return userInput.nextLine();
    }

    /**
     * this method receives a [yes/no] response from the user. If the response is
     * not a valid response it will ask again.
     * @return true if the answer is "yes" false if the answer is "no"
     */
    public boolean getYesOrNo(){
        String response = userInput.nextLine();
        while(!(response.equals("yes")||response.equals("no"))){
            out.print("please enter yes or no: ");
            response = userInput.nextLine();
        }
        return response.equals("yes");
    }

    /**
     * this method receives an int from the user. If the input is not a number
     * it will print "please insert a valid number".
     * @return number inserted by the user
     * @see CLI#getInt(Integer, Integer)
     */
    public int getInt(){
        return getInt(null, null);
    }

    /**
     * this method receives an int from the user and parses it as an int.
     * If the input is not a number or is not between lowerBound and upperBound
     * this method prints "please insert a valid number:" to screen. If lowerBound
     * or upperBound are null they will be replaced with MIN_VALUE and MAX_VALUE
     * respectively. <br>!! Don't forget this is a blocking method
     * @param lowerBound lower Bound accepted
     * @param upperBound Upper Bound accepted
     * @return integer selected by the user (only if between the low and up Bounds)
     */
    public int getInt(Integer lowerBound, Integer upperBound){
        int value;
        if(lowerBound == null)
            lowerBound = Integer.MIN_VALUE;
        if(upperBound == null)
            upperBound = Integer.MAX_VALUE;
        try{
            value = Integer.parseInt(userInput.nextLine());
            while(value<lowerBound || value>upperBound){
                out.print("please insert a valid number: ");
                value = Integer.parseInt(userInput.nextLine());
            }
            return value;
        }catch (NumberFormatException e){
            out.print("please insert a number: ");
            return getInt(lowerBound, upperBound);
        }
    }

    public void printFile(String fileLocation){
        try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket connectToServer() {
        String address;
        int port;
        Socket socket = new Socket();
        displayMessage("\rConnect to server");
        displayString("IP address: " );
        address = getLine();
        displayString("Port: ");
        port = getInt();
        try{
            socket.connect(new InetSocketAddress(address,port), 500);
            return socket;

        } catch (UnknownHostException e) {
            displayMessage("Cannot connect to server with address "+address+":"+port);
            displayMessage("Check provided information and try again");
            socket = connectToServer();
        } catch (IOException e) {
            displayMessage("ERROR: Unable to open socket");
            displayMessage("Check provided information and try again");
            socket = connectToServer();
        }
        return socket;
    }

    public String joinGame() {
        boolean isAdvancedMode;
        int numPlayer;
        String selection;
        //asks for number of players:
        displayString("How many players in game: ");
        numPlayer = getInt(2,4);
        //asks for advanced rules
        displayString("Do you want to use advanced rules in this game[yes/no]: ");
        isAdvancedMode = getYesOrNo();
        selection = numPlayer + " " + isAdvancedMode;

        return  selection;
    }

    public String getUsername() {
        String username;
        do{
            displayString("Username: ");
            username = getLine();
            if(username.isBlank())
                displayMessage("Username cannot be blank");
        }while (username.isBlank());

        return username;
    }

    public int selectMage(List<Mage> available) {
        displayMessage("Available mages: ");

        for(int i = 0; i < available.size(); i ++)
            displayMessage(i + " - " + available.get(i));

        displayString("Select index: ");
        return getInt(0, available.size()-1);
    }

    public int selectTower(List<TColor> available) {
        displayMessage("Available towers: ");

        for(int i = 0; i < available.size(); i ++)
            displayMessage(i + " - " + available.get(i));

        displayString("Select index: ");
        return getInt(0, available.size()-1);
    }

    public Card selectAssistantCard() {
        displayAssistantCards(cardsInHand);
        displayString("Index of card ");
        int index = getInt(0, cardsInHand.size()-1);
        return cardsInHand.get(index);
    }

    public JsonObject moveStudents() {
        JsonObject out = new JsonObject();
        JsonArray list ;


        ArrayList<Color> students = new ArrayList<>(myGameBoard.getStudentsEnter());
        int location;

        displayString("Select location: ");
        location = getInt(0,12);
        out.addProperty("location", location);

        displayString("How many students would you like to move to this location (1 - "+ (3- movedInTurn) +") : ");
        int numSt = getInt(1,3- movedInTurn);

        list = selectStudents(students, numSt);

        out.add("students", list);
        out.addProperty("num_stud", numSt);
        return out;
    }



    public int moveMN() {
        displayString("For how many places would you like to move Mother Nature (1 - "+ cardPlayed.getMaxMoves()+") : ");
        return getInt(1,cardPlayed.getMaxMoves());

    }

    public void update(updateMessage update) {
        motherNature = update.mother;
        clouds = update.clouds;
        othersPlayedCards.clear();
        othersGameBoards.clear();
        islands = update.islands;
        if(advanced)
        {
            personalities.addAll(update.modifiers);
            personalities.addAll(update.containers);
        }

        for(Player p : update.players) {
            if (p.getPlayerId() == id) {
                myGameBoard = p.getGameBoard();
                if (cardsInHand == null || cardsInHand.isEmpty())
                    cardsInHand = new ArrayList<>(p.getHand().getAllCards());
            } else {
                othersGameBoards.add(p.getGameBoard());
                othersPlayedCards.add(p.getHand().getCardPlayed());
            }
        }

        showUpdate();
    }

    private void showUpdate() {
        out.format("+----------My GameBoard----------+%n");
        showGameBoard(myGameBoard);

        for(GameBoard gb : othersGameBoards){
            out.format("+----------"+gb.getUsername()+"'s GameBoard----------+%n");
            showGameBoard(gb);
        }

        out.format("+------------Islands------------+%n");
        showIslands();
    }

    private void showIslands() {
        String islandsFormat = "| %-2s | %-23s | %-14s | %-15s | %-3s |%n";
        out.format("+------+------------------------+------------------+-----------------+------------+%n");
        out.format("|  Id  | Students on the island | Number of Towers | Color of Towers | MN present |%n");
        out.format("+------+------------------------+------------------+-----------------+------------+%n");
        for (Island is: islands) {
            String mn;
            if(motherNature == is.getID())
                mn = "yes";
            else
                mn = "no";
            List<Tower> towers = is.getTowers();
            TColor color;
            if(towers.isEmpty()){
                color = null;
            }else{
                color = towers.get(0).color;
            }
            out.format(islandsFormat,is.getID(), getStudentString(is.getStudents()), towers.size(), color,mn);
        }
        out.format("+------+------------------------+------------------+-----------------+------------+%n");
    }

    private void showGameBoard(GameBoard gb) {
        String entrance = "| %-24s |%n";
        out.format("+--------------------------+%n");
        out.format("| Students in the entrance |%n");
        out.format("+--------------------------+%n");
        out.format(entrance, "      " + getStudentString(gb.getStudentsEnter()) + "      ");
        out.format("+--------------------------+%n");



        String hall = "| %-7s | %-18s |%n";
        out.format("+---------+----------------------+%n");
        out.format("|  Color  | Number of students |%n");
        out.format("+---------+----------------------+%n");
        for (Color c: Color.values()) {
            out.format(hall,c, gb.getStudentsHall(c));
        }
        out.format("+---------+----------------------+%n");

    }

    public  Card getCardPlayed() {
        return cardPlayed;
    }

    public void setCardPlayed(Card card) {
         cardPlayed = card;
    }

    public void removeFromHand(Card played){
        cardsInHand.remove(played);
    }

    public void setMovedInTurn(int moved){
        movedInTurn = moved;
    }

    public int getMovedInTurn() {
        return movedInTurn;
    }
    public void setId(int id) {
        this.id = id;
    }


    public void nextRound() {
        availableCommands = new ArrayList<>(List.of("move_students", "move_mn"));
        if(advanced) {
           availableCommands.add("use_personality");
        }
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public String commandSelection() {
        displayMessage("Please select your next move");
        String selection;
        do {
            displayMessage("Available commands are : " + availableCommands);
            selection = getLine();
            selection = selection.toLowerCase();
            if(!availableCommands.contains(selection))
                displayMessage("Selected command not found");
        }while(!availableCommands.contains(selection));
        return selection;
    }

    public void removeCommand(String s){
        availableCommands.remove(s);
        possibleTurnEnd();
    }

    public int selectCloud() {
        String leftAlignFormat = "| %-11d | %-17s |%n";
        out.format("+------------+--------------------+%n");
        out.format("| Cloud index | Students on cloud |%n");
        out.format("+------------+--------------------+%n");
        for (int i : clouds.keySet()) {
            out.format(leftAlignFormat,i, getStudentString(clouds.get(i)));
        }
        out.format("+------------+-------------+------+%n");
        int selection = getInt(0, clouds.size());
        return selection;
    }

    private String getStudentString(List<Color> students){
        String out = "";

        for(Color c : students) {
            String color = null;

            switch (c) {
                case RED -> color = "\u001B[31m";
                case YELLOW -> color = "\u001B[33m";
                case GREEN -> color = "\u001B[32m";
                case BLUE -> color = "\u001B[34m";
                case PINK -> color = "\u001B[35m";
            }
            out = out + color + "\u2b24";
        }
        out = out + "\u001b[0m";

        return String.format("%-20s",out);
    }

    public JsonObject usePersonality() {
        JsonObject object = new JsonObject();
        List<String> names = new ArrayList<>();
        String leftAlignFormat = "| %-16s |  %-2d  | %-16s |%n";
        out.format("+------------------+------+------------------+%n");
        out.format("| Personality name | Cost | Elements on card |%n");
        out.format("+------------------+------+------------------+%n");
        for (Personality p : personalities) {
            names.add(p.getName());
            if(p.getName().equals("botanist"))
            {
                Containers botanist = (Containers) p;
                out.format(leftAlignFormat,botanist.getName().substring(0,1).toUpperCase() + botanist.getName().substring(1), botanist.getCost(),botanist.getNoEntry() + " NoEntry tiles");
            }
            else if (p.getClass() == Containers.class) {
                Containers container = (Containers) p;
                out.format(leftAlignFormat,container.getName(), container.getCost(),getStudentString(container.getStudents()));
            }
            else {
                out.format(leftAlignFormat,p.getName(), p.getCost(),"Empty card");
            }

        }
        out.format("+------------------+------+------------------+%n");
        out.format("+ back                                       +%n");
        out.format("+--------------------------------------------+%n");
        String personality;
        do {
            displayString("Select personality:");
            personality = getLine();
            personality = personality.toLowerCase();
            if(personality.equals("exit") || personality.equals("back"))
                return null; //this exists this state and goes back to the selection
            if(!names.contains(personality))
            {
                displayMessage("Selected personality not available");
            }
        }while(!names.contains(personality));

        object.addProperty("card", personality);


        personalityHandler(getPersonalityWithName(personality), object);


        return object;
    }

    private Personality getPersonalityWithName(String name) {
        return personalities.stream() //finds player who is owner of card
                .filter(pers -> name.equals(pers.getName()))
                .findAny()
                .orElse(null);
    }

    private void personalityHandler(Personality personality, JsonObject object) {

        switch (personality.name) {
            case "botanist", "pirate" -> {
                int islandId = islandSelection();
                object.addProperty("island", islandId);
            }
            case "winemaker" -> {
                int islandId = islandSelection();
                object.addProperty("island", islandId);
                Color selectedColor = colorSelection();
                object.addProperty("color", selectedColor.toString());
            }
            case "jester" -> {
                Containers jester = (Containers) personality;
                displayString("How many students would you like to exchange (1-3): ");
                int num = getInt(1, 3);
                displayMessage("Select students from card: ");
                JsonArray fromCard = selectStudents(jester.getStudents(), num);

                displayMessage("Select students from entrance: ");
                object.add("studentsFromCard", fromCard);
                JsonArray fromEntrance = selectStudents(myGameBoard.getStudentsEnter(), num);
                object.add("studentsFromEntrance", fromEntrance);
            }

            case "courtesan" -> {
                Containers courtesan = (Containers) personality;
                Color selectedColor = colorSelection(courtesan.getStudents());
                object.addProperty("color", selectedColor.toString());
            }

            case "glutton", "witch" -> {
                Color selectedColor = colorSelection();
                object.addProperty("color", selectedColor.toString());
            }

            case "cantor" -> {
                displayString("How many students would you like to exchange (1-2): ");
                int num = getInt(1, 2);
                displayMessage("Select students from hall: ");
                JsonArray fromCard = selectStudents(myGameBoard.getAllStudentsHall(), num);
                object.add("studentsFromHall", fromCard);
                displayMessage("Select students from entrance: ");
                JsonArray fromEntrance = selectStudents(myGameBoard.getStudentsEnter(), num);
                object.add("studentsFromEntrance", fromEntrance);
            }

        }

    }

    private Color colorSelection(List<Color> available) {

        Color selection;
        String col;
        do{
            displayMessage("Available colors : ");

            for (Color c : available)
                System.out.print(c + " ");
            System.out.println("\n");

            displayString("Color : ");
            col = getLine();
            col = col.toUpperCase();
            String finalCol = col;
            selection = available.stream()//finds player who is owner of card
                    .filter(cl -> finalCol.equals(cl.toString()))
                    .findAny()
                    .orElse(null);
            if(selection == null)
                displayMessage("Please select valid student");
        }while (selection == null);
        return selection;
    }

    private Color colorSelection() {

        Color selection;
        String col;
        do{
            displayMessage("Available colors : ");

            for (Color c : Color.values())
                System.out.print(c + " ");
            System.out.println("\n");



            displayString("Color : ");
            col = getLine();
            col = col.toUpperCase();
            String finalCol = col;
            selection = Arrays.stream(Color.values()).sequential()//finds player who is owner of card
                    .filter(cl -> finalCol.equals(cl.toString()))
                    .findAny()
                    .orElse(null);
            if(selection == null)
                displayMessage("Please select valid student");
        }while (selection == null);
        return selection;
    }

    private int islandSelection() {
        int islandId;
        boolean check = false;
        do {
            displayMessage("Available islands : ");
            for (Island is : islands)
                System.out.print(is.getID() + " ");
            System.out.println("\n");
            displayString("Please select ID of island on which you wish to put NoEntry tile: ");
            islandId = getInt(1, 12);
            check = existingIslandId(islandId);
            if(!check)
                displayMessage("Island with selected ID doesn't exist, please select other one");
        }while(!check);
        return islandId;
    }

    private JsonArray selectStudents(List<Color> in, int numSt) {
        ArrayList<Color> students = new ArrayList<>(in);
        displayMessage("Available students: " + students);
        JsonArray list = new JsonArray();
        Color selection;
        String col;
        for(int i = 0; i < numSt; i++){
            do{
                displayString("Student : ");
                col = getLine();
                col = col.toUpperCase();
                String finalCol = col;
                selection = Arrays.stream(Color.values()).sequential()//finds player who is owner of card
                        .filter(cl -> finalCol.equals(cl.toString()))
                        .findAny()
                        .orElse(null);
                if(selection == null || !students.contains(selection))
                    displayMessage("Please select valid student");
            }while (selection == null || !students.contains(selection));

            students.remove(selection);
            list.add(selection.toString());
        }
        return list;
    }


    private boolean existingIslandId(int id) {
        for(Island i : islands)
            if(i.getID() == id)
                return true;

        return false;
    }

    private void possibleTurnEnd(){
        if(!availableCommands.contains("select_cloud") && (availableCommands.size() == 0 || (availableCommands.size() == 1 && availableCommands.contains("use_personality"))))
            availableCommands.add("select_cloud");
    }

    public void removeStudents(JsonObject jobject) {
        if(!jobject.has("students"))
        {
            return;
        }

        String json = jobject.get("students").toString();
        ArrayList<Color> students = gson.fromJson(json, new TypeToken<List<Color>>(){}.getType());
        myGameBoard.removeStudentsEnter(students);
    }
}
