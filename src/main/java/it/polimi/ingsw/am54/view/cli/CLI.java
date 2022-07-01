package it.polimi.ingsw.am54.view.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.network.Client;
import it.polimi.ingsw.am54.network.Mage;
import it.polimi.ingsw.am54.network.updateMessage;
import it.polimi.ingsw.am54.view.CLIController;
import it.polimi.ingsw.am54.view.View;

import java.io.*;
import java.util.*;

import static java.lang.Integer.parseInt;

import com.sun.jna.*;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinNT.HANDLE;

/**
 * This class contains all the methods to interface with a CLI client.
 */
public class CLI implements View {
    private Scanner userInput;
    private final PrintStream out;

    int id;
    private static int movedInTurn = 0;
    public GameBoard myGameBoard;
    private ArrayList<GameBoard> othersGameBoards;
    private ArrayList<Card> othersPlayedCards;
    private List<Island> islands;
    protected  ArrayList<Card> cardsInHand;
    protected  Card cardPlayed;
    private List<String> availableCommands;
    private List<Personality> personalities;
    private boolean advanced;
    private int motherNature;
    private  Map<Integer, List<Color>> clouds;
    private static final Gson gson = new GsonBuilder().create();
    private int additionalMN = 0;

    /**
     * Constructor for class CLI.
     */
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
        checkWindows();
    }
    /**
     * Enables ANSI character on Clients running Windows 10 and 11
     * source: https://stackoverflow.com/a/52767586/6814247
     */

    private void checkWindows() {
        if(System.getProperty("os.name").startsWith("Windows"))
        {
            // Set output mode to handle virtual terminal sequences
            Function GetStdHandleFunc = Function.getFunction("kernel32", "GetStdHandle");
            DWORD STD_OUTPUT_HANDLE = new DWORD(-11);
            HANDLE hOut = (HANDLE)GetStdHandleFunc.invoke(HANDLE.class, new Object[]{STD_OUTPUT_HANDLE});

            DWORDByReference p_dwMode = new DWORDByReference(new DWORD(0));
            Function GetConsoleModeFunc = Function.getFunction("kernel32", "GetConsoleMode");
            GetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, p_dwMode});

            int ENABLE_VIRTUAL_TERMINAL_PROCESSING = 4;
            DWORD dwMode = p_dwMode.getValue();
            dwMode.setValue(dwMode.intValue() | ENABLE_VIRTUAL_TERMINAL_PROCESSING);
            Function SetConsoleModeFunc = Function.getFunction("kernel32", "SetConsoleMode");
            SetConsoleModeFunc.invoke(BOOL.class, new Object[]{hOut, dwMode});
        }
    }

    /**
     * Displays wait message to user.
     * @see CLI#displayMessage(String)
     */
    public void displayWait(){
        displayMessage("\rplease wait for your turn");
    }

    /**
     * method to display assistant Cards in specific format (tabel)
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
     * prints to screen without new line.
     * @param message message to print
     */
    public void displayString(String message){
        out.print(message);
    }

    /**
     * Method that initializes CLI Controller
     * @see CLIController
     */
    @Override
    public void initialize() {
        CLIController controller = new CLIController(this);

    }


    /**
     * prints to screen with new line.
     * @param message message to print
     */
    public void displayMessage(String message){
        out.println(message);
    }

    /**
     * Displays error message to user.
     * @param message error message to be displayed
     * @see CLI#displayMessage(String)
     */
    @Override
    public void displayErrorMessage(String message) {
            displayMessage(message);
    }

    /**
     * Gets line of text that user has entered.
     * @return user's textual input
     */
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
            out.print("please insert a valid number: ");
            return getInt(lowerBound, upperBound);
        }
    }

    /**
     * Asks user to enter parameters of the game to which to join.
     * @return prepared data to be sent to server
     */
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

    /**
     * Asks user to select username.
     * @return selected username
     */
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

    /**
     * Asks user to select a mage.
     * @param available list of available mages
     * @return index of selected mage for available
     */
    public int selectMage(List<Mage> available) {
        displayMessage("Available mages: ");

        for(int i = 0; i < available.size(); i ++)
            displayMessage(i + " - " + available.get(i));

        displayString("Select index: ");
        return getInt(0, available.size()-1);
    }

    /**
     * Asks user to select a color of tower.
     * @param available list of available colors
     * @return index of selected color for available
     */
    public int selectTower(List<TColor> available) {
        displayMessage("Available towers: ");

        for(int i = 0; i < available.size(); i ++)
            displayMessage(i + " - " + available.get(i));

        displayString("Select index: ");
        return getInt(0, available.size()-1);
    }

    /**
     * allows to choose the assistantCard from the player's hand.
     * @return chosen card
     */
    public Card selectAssistantCard() {
        displayAssistantCards(cardsInHand);
        displayString("Index of card ");
        int index = getInt(0, cardsInHand.size()-1);
        return cardsInHand.get(index);
    }

    /**
     * allows the player to move a student.
     */
    public JsonObject moveStudents() {
        JsonObject out = new JsonObject();
        JsonArray list ;


        ArrayList<Color> students = new ArrayList<>(myGameBoard.getStudentsEnter());
        int location;

        displayString("Select location(Hall - 0, island - islandID): ");
        location = getInt(0,12);
        out.addProperty("location", location);

        displayString("How many students would you like to move to this location (1 - "+ (3- movedInTurn) +") : ");
        int numSt = getInt(1,3- movedInTurn);

        list = selectStudents(students, numSt);

        out.add("students", list);
        out.addProperty("num_stud", numSt);
        return out;
    }

    /**
     * allows to move MotherNature.
     */
    public int moveMN() {
        displayString("For how many places would you like to move Mother Nature (1 - "+ (additionalMN + cardPlayed.getMaxMoves())+") : ");
        return getInt(1,cardPlayed.getMaxMoves()+additionalMN);

    }

    /**
     *updates the data with the information contained in the updateMessage.
     * @param update
     */
    public void dataUpdate(updateMessage update){
        motherNature = update.mother;
        clouds = update.clouds;
        othersPlayedCards.clear();
        othersGameBoards.clear();
        personalities.clear();
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
    }

    /**
     * updates current player and move with the information contained in the update message.
     * @param update
     */
    public void update(updateMessage update) {
        dataUpdate(update);
        if(update.currentPlayer == null || update.currentPlayer.equals(myGameBoard.getUsername()))
            showUpdate();
        else if(update.description.contains("Moved Mother nature")){
            displayMessage("Current player: " + update.currentPlayer);
            displayMessage("Move description: " + update.description);
        }
        else
            showChange(update.currentPlayer, update.description);
    }

    /**
     *shows the changes in the game state on the display (currentPlayer, move).
     * @param currentPlayer
     * @param description
     */
    private void showChange(String currentPlayer, String description) {
        clearScreen();
        displayMessage("Current player: " + currentPlayer);
        displayMessage("Move description: " + description);

        for(GameBoard gb : othersGameBoards)

            if(gb.getUsername().equals(currentPlayer))
            {
                out.format("+------------------------------"+gb.getUsername()+"'s GameBoard-------------------------------+%n");
                showGameBoard(gb);
                break;
            }
        out.format("+------------Islands------------+%n");
        showIslands();
    }

    /**
     *manages the disconnection of a player from the game
     * @param playerName
     */
    public void playerDisconnected(String playerName) {
        displayMessage("Player "+ playerName + "is disconnected");
        displayMessage("Closing game");
        Client.end();
        System.exit(808);
    }

    /**
     *shows the changes in the game state on the display (gameboard and islands).
     */
    public void showUpdate() {
        clearScreen();
        showMyBoard();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        otherBoards();

        out.format("+------------Islands------------+%n");
        showIslands();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * show the islands on the display
     */
    public void showIslands() {
        String islandsFormat = "| %-2s | %-22s | %-16s |      %-5s      |     %-3s    |%n";
        out.format("+----+------------------------+------------------+-----------------+------------+%n");
        out.format("| Id | Students on the island | Number of Towers | Color of Towers | MN present |%n");
        out.format("+----+------------------------+------------------+-----------------+------------+%n");
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
            out.format(islandsFormat,is.getID(), getStudentString(is.getStudents(),22), towers.size(), color,mn);
        }
        out.format("+----+------------------------+------------------+-----------------+------------+%n");
    }

    /**
     *show the gameboard given in input on the display
     * @param gb
     */
    private void showGameBoard(GameBoard gb) {
        Iterator<Color> entranceStudents = gb.getStudentsEnter().iterator();
        List<Tower> towers = gb.getTowers();
        String leftAlignFormat = "| %-7s  |%-40s |    %-3s    |%n";
        System.out.format("+----------+-----------------------------------------+-----------+%n");
        System.out.format("+ Towers: "+ gb.getTowColor() +" "+towers.size()+"                                       Coins: "+gb.getCoins()+" +%n");
        System.out.format("+----------+-----------------------------------------+-----------+%n");
        System.out.format("| Entrance |                  Hall                   | Professor |%n");
        System.out.format("+----------+-----------------------------------------+-----------+%n");
        for (int i = 0; i < 5; i++) {
            System.out.format(leftAlignFormat, entranceString(entranceStudents, i), hallString(gb, i), profString(gb, i));
        }
        System.out.format("+----------+-----------------------------------------+-----------+%n");

    }

    /**
     * checks if a professor is contained in the gameboard (using the containsProf function) and, if so,
     * returns the professor's "dot",otherwise it returns an empty "cell".
     * @param gb
     * @param i
     */
    private Object profString(GameBoard gb, int i) {
        String color = null;
        String key = null;
        switch (i) {
            case 0 -> {color = "\u001B[32m"; key = "GREEN";}
            case 1 -> {color = "\u001B[31m"; key = "RED"; }
            case 2 -> {color = "\u001B[33m"; key = "YELLOW";}
            case 3 -> {color = "\u001B[35m"; key = "PINK";}
            case 4 -> {color = "\u001B[34m"; key = "BLUE";}
        }
        if(containsProf(gb, Color.valueOf(key))){
            return ("["+color+"█\u001b[0m]");
        }
        return "[ ]";
    }

    /**
     * creates a string representing the section of the hall corresponding to the input color (int i)
     * @param gb
     * @param i
     */
    private String hallString(GameBoard gb, int i) {
        StringBuilder out = new StringBuilder();
        String color = null;
        String key = null;
        switch (i) {
            case 0 -> {color = "\u001B[32m"; key = "GREEN";}
            case 1 -> {color = "\u001B[31m"; key = "RED"; }
            case 2 -> {color = "\u001B[33m"; key = "YELLOW";}
            case 3 -> {color = "\u001B[35m"; key = "PINK";}
            case 4 -> {color = "\u001B[34m"; key = "BLUE";}
        }
        int num = gb.getStudentsHall(Color.valueOf(key));
        out.append(color);
        out.append(" [█]".repeat(num));
        for(int j = num+1; j <= 10; j++){
            if(j % 3 == 0)
                out.append(" [x]");
            else
                out.append(" [ ]");
        }
        out.append("\u001b[0m");
        return out.toString();
    }

    /**
     *creates a string representing the entrance of the gameboard
     * @param entranceStudents
     * @param i
     */
    private String entranceString(Iterator<Color> entranceStudents, int i) {
        StringBuilder out = new StringBuilder();
        if(i == 0){
            out.append("   ");
        }
        else{
            if(entranceStudents.hasNext())
                out.append("[").append(colorString(entranceStudents.next())).append("█\u001b[0m]");
            else
                out.append("[ ]");
        }
        if(entranceStudents.hasNext())
            out.append(" [").append(colorString(entranceStudents.next())).append("█\u001b[0m]");
        else
            out.append(" [ ]");

        return out.toString();
    }

    /**
     * @return the "representative dot" of one of the 5 colors of the students
     * @param c
     */
    private String colorString(Color c)
    {
        switch (c) {
            case RED -> {
                return "\u001B[31m";
            }
            case YELLOW -> {
                return "\u001B[33m";
            }
            case GREEN -> {
                return "\u001B[32m";
            }
            case BLUE -> {
                return "\u001B[34m";
            }
            case PINK -> {
                return "\u001B[35m";
            }
            default -> throw new IllegalStateException("Unexpected value: " + c);
        }
    }

    /**
     * @return cardPlayed
     */
    public  Card getCardPlayed() {
        return cardPlayed;
    }

    /**
     * @param card
     */
    public void setCardPlayed(Card card) {
        cardPlayed = card;
    }

    /**
     * allows to remove a card from player's hand
     * @param played
     */
    public void removeFromHand(Card played){
        cardsInHand.remove(played);
    }

    /**
     * @param moved
     */
    public void setMovedInTurn(int moved){
        movedInTurn = moved;
    }

    /**
     * @return moveInTurn
     */
    public int getMovedInTurn() {
        return movedInTurn;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * sets the commands executable in the round.
     */
    public void nextRound() {
        availableCommands = new ArrayList<>(List.of("move_students", "move_mn","show_my_board", "show_other_boards", "show_islands"));
        if(advanced) {
            availableCommands.add("use_personality");
        }
    }

    /**
     * sets the type of match (true if the match is an advancedMode match).
     * @param advanced
     */
    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    /**
     *allows player to choose his next move
     */
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

    /**
     * remove a command from the availableCommand list.
     * @param s
     */
    public void removeCommand(String s){
        availableCommands.remove(s);
        possibleTurnEnd();
    }

    /**
     *manages the choiceIsland screen.
     */
    public int selectCloud() {
        String leftAlignFormat = "| %-11d | %-17s |%n";
        out.format("+------------+--------------------+%n");
        out.format("| Cloud index | Students on cloud |%n");
        out.format("+------------+--------------------+%n");
        for (int i : clouds.keySet()) {
            out.format(leftAlignFormat,i, getStudentString(clouds.get(i),17));
        }
        out.format("+------------+-------------+------+%n");
        return getInt(0, clouds.size());
    }

    /**
     *returns the representation of a list of students.
     */
    private String getStudentString(List<Color> students, int num){
        StringBuilder out = new StringBuilder();
        int size = num - 2*students.size();

        out.append(" ".repeat(Math.max(0, (size - size / 2))));

        for(Color c : students)
            out.append(colorString(c)).append("█ ");

        out.append("\u001b[0m");

        out.append(" ".repeat(Math.max(0, size / 2)));

        return String.format("%1$"+num+ "s", out);
    }

    /**
     *manages the interaction for the choice and use of a personality card.
     */
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
                out.format(leftAlignFormat,container.getName(), container.getCost(),getStudentString(container.getStudents(),16));
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

    /**
     *returns the personality corresponding to the input string
     * @param name
     */
    private Personality getPersonalityWithName(String name) {
        return personalities.stream() //finds player who is owner of card
                .filter(pers -> name.equals(pers.getName()))
                .findAny()
                .orElse(null);
    }

    /**
     *manages the powers of the personality cards.
     */
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

    /**
     *allows player to choose a color.
     * @param available
     */
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

    /**
     * allows player to choose a color.
     */
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

    /**
     * allows player to choose an island.
     */
    private int islandSelection() {
        int islandId;
        boolean check;
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

    /**
     * allows player to choose a student.
     */
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


    /**
     * @return true if the island exists, false otherwise.
     * @param id
     */
    private boolean existingIslandId(int id) {
        for(Island i : islands)
            if(i.getID() == id)
                return true;

        return false;
    }

    /**
     *check if the player has already moved mother nature and students, if that is true then add select_cloud command to availableCommands.
     */
    private void possibleTurnEnd(){
        if(!availableCommands.contains("select_cloud") && (!availableCommands.contains("move_mn") && !availableCommands.contains("move_students")))
            availableCommands.add("select_cloud");
    }

    /**
     * remove a student from the gameboard
     * @param jobject
     */
    public void removeStudents(JsonObject jobject) {
        if(!jobject.has("students"))
        {
            return;
        }

        String json = jobject.get("students").toString();
        ArrayList<Color> students = gson.fromJson(json, new TypeToken<List<Color>>(){}.getType());
        myGameBoard.removeStudentsEnter(students);
    }

    /**
     * @return server info
     */
    public Map<String, String> getServerInfo() {
        Map<String, String> out = new HashMap<>();
        displayString("Server's address: ");
        out.put("address", getLine());
        displayString("Server's port: ");
        out.put("port", String.valueOf(getInt()));
        return out;
    }

    /**
     * it is true if in the gameboard of the player there is the teacher of the color in input.
     * @param c
     * @param gb
     */
    public boolean containsProf(GameBoard gb, Color c){
        Professor p = gb.getProf().stream() //finds player who is owner of card
                .filter(pro -> c.equals(pro.getColor()))
                .findAny()
                .orElse(null);
        return p != null;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * just clear the screen.
     */
    public void clearScreen(){
        displayMessage("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * shows the player's gameboard on the display.
     */
    public void showMyBoard() {
        out.format("+---------------------------------My GameBoard----------------------------------+%n");
        showGameBoard(myGameBoard);
    }

    /**
     *shows other players' gameboard on the display.
     */
    public void otherBoards() {
        for(GameBoard gb : othersGameBoards){
            out.format("+------------------------------"+gb.getUsername()+"'s GameBoard-------------------------------+%n");
            showGameBoard(gb);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Knight card's power.
     * @param i
     */
    public void setAdditionalMN(int i) {
        additionalMN = i;
    }
}