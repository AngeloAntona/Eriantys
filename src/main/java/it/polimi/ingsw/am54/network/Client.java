package it.polimi.ingsw.am54.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.view.CLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Client {
    private static int id;
    private static Mage mage;
    private static TColor towerColor;

    static Socket socket = new Socket();
    static ObjectInputStream objectFromServer;
    protected static ObjectOutputStream objectToServer;
    private static final Gson gson = new GsonBuilder().create();
    protected static CLI view;


    public static void main(String[] args) {
        view = new CLI();
        String add = "localhost";
        int port = 1800;

        try{//tries to connect to given server if it exists and has open port
            Socket socket = view.connectToServer();
            //view.printFile("src/main/resources/CLI_images/banner.txt");
            //creates input and output streams for objects
            //NOTE: Order of streams is important in both server and client, can cause error invalid stream header
            objectToServer = new ObjectOutputStream(socket.getOutputStream());
            objectFromServer = new ObjectInputStream(socket.getInputStream());


           if(!joinGame())
                System.exit(404);
            setUsername();
            selectMage();
            selectTowerColor();
            sendText("player_ready");

            if(getCommand(receiveCommand()).equals("wait"))
                waitState();

            sendText("end");
            socket.close(); //closes connection
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    protected static boolean joinGame() throws IOException {

        String selection = view.joinGame();
        System.out.println(selection);
        boolean advanced = getParameter(selection).equals("true");
        sendObject("join_game", selection);
        String response = receiveCommand();

        if(getCommand(response).equals("ERR")) {
            view.displayMessage("FATAL ERROR CANNOT CONNECT \n CLOSING APP");
            sendText("end");
            return false;
        }
        id = gson.fromJson(getParameter(response), new TypeToken<Integer>() {}.getType());
        view.displayMessage("Welcome to the game, your id is " + id);
        view.setId(id);
        view.setAdvanced(advanced);
        return true;

    }

    protected static void setUsername() throws IOException {

        String response, username;
        do{
            username = view.getUsername();
            sendObject("set_username", username);
            response = receiveCommand();
        }while (!getCommand(response).equals("ACK"));

        view.displayMessage("OK");
    }

    protected static void selectMage() throws IOException, ClassNotFoundException {
        List<Mage> available;
        String response, response2 = null;
        int selection;

        do {
            if(response2 != null && getCommand(response2).equals("ERR")){
                view.displayMessage(getParameter(response2));
                view.displayMessage("Please select valid mage!");
            }

            sendText("get_available_mages");
            response = receiveCommand();
            if(getCommand(response).equals("ERR")){
                view.displayMessage("Unexpected server error: " + getParameter(response)); // veramente non aspettiamo che ci da errore qua
                System.exit(201);
            }
            available = gson.fromJson(getParameter(response), new TypeToken<List<Mage>>(){}.getType());
            selection = view.selectMage(available);

            sendObject("select_mage", available.get(selection));

            response2 = receiveCommand();
        }while(!getCommand(response2).equals("ACK"));
        view.displayMessage("OK");
        mage = available.get(selection);
    }

    protected static void selectTowerColor() throws IOException, ClassNotFoundException {

        List<TColor> available;
        String response, response2 = null;
        int selection;

        do {
            if(response2 != null && getCommand(response2).equals("ERR")){
                view.displayMessage(getParameter(response2));
                view.displayMessage("Please select valid tower color!");
            }

            sendText("get_available_towers");
            response = receiveCommand();
            if(getCommand(response).equals("ERR")){
                view.displayMessage("Unexpected server error: " + getParameter(response)); // non aspettiamo neanche qua un errore
                System.exit(201);
            }
            available = gson.fromJson(getParameter(response), new TypeToken<List<TColor>>(){}.getType());
            selection = view.selectTower(available);

            sendObject("select_tower", available.get(selection));

            response2 = receiveCommand();
        }while(!getCommand(response2).equals("ACK"));
        view.displayMessage("OK");
        towerColor = available.get(selection);
    }

    protected static void waitState() {
        view.displayWait();
        //view.displayString("\rPlease wait for your turn");

        String response = receiveCommand();
        switch (getCommand(response)) {
            case "planning_turn" -> selectAssistantCard();
            case "next_turn" -> gameplay();
            case "ping" -> pong();
            case "player_disconnected" -> playerDisconnected(getParameter(response));
            case "end" -> end();
            case "wait" -> waitState();
            case "update" -> update(getParameter(response));
            default -> view.displayMessage(response);
        }
    }




    protected static void selectAssistantCard() {

        String response = null;
        Card selectedCard;
        do{
            if(response != null && getCommand(response).equals("ERR")) {
                view.displayMessage("Error "+ getParameter(response));
                view.displayMessage("Please select another card");
            }
             selectedCard = view.selectAssistantCard();
            sendObject("select_assistant_card", selectedCard);
            response = receiveCommand();
        }while(!getCommand(response).equals("ACK"));

        view.setCardPlayed(selectedCard);
        view.removeFromHand(selectedCard);
        view.displayMessage("OK");
        waitState();
    }

    protected static void moveStudents() {
        int movedInTurn = view.getMovedInTurn();

        if(movedInTurn == 3){
            view.displayMessage("Max number of students already moved");
            return;
        }

        JsonObject out;
        String response = null;
        int moved;
        do {

            if (response != null && getCommand(response).equals("ERR")) {
                view.displayMessage("Error: " + getParameter(response));
                view.displayMessage("Please try again");
            }

            out = view.moveStudents();
            moved = Integer.parseInt(out.get("num_stud").toString());
            sendObject("move_students", out);
            response = receiveCommand();
        }while (!getCommand(response).equals("ACK"));
        view.removeStudents(out);
        view.setMovedInTurn(movedInTurn + moved);
        if(view.getMovedInTurn() == 3)
            view.removeCommand("move_students");
        view.displayMessage("OK");
    }


    protected static void moveMN() {

        if(view.getCardPlayed() == null){
            view.displayMessage("You need to select assistant card before moving Mother Nature");
            return;
        }

        String response = null;
        int selectedMoves;

        do {
            if (response != null && getCommand(response).equals("ERR")) {
                view.displayMessage("Error: " + getParameter(response));
                view.displayMessage("Please try again");
            }
            selectedMoves = view.moveMN();
            sendObject("move_mn", selectedMoves);
            response = receiveCommand();
        } while(!getCommand(response).equals("ACK"));
        view.removeCommand("move_mn");
        view.displayMessage("OK");

    }

    public static void update(String json){
        updateMessage update = gson.fromJson(json, new TypeToken<updateMessage>(){}.getType());
        view.update(update);
        waitState();
    }



    private static void end() {
        //TODO
    }

    private static void playerDisconnected(String nameJson){
        view.displayMessage("player " + nameJson + " has disconnected");
        end();
    }


    private static void pong() {
        sendText("pong");
        waitState();
    }


    private static void gameplay() {
        boolean done = false;
        view.setMovedInTurn(0);
        view.nextRound();
        String command;
        while (!done){
            command = view.commandSelection();
            switch (command) {
                case "move_students" -> moveStudents();
                case "move_mn" -> moveMN();
                case "select_cloud" -> {cloudSelection(); done = true;}
                case "use_personality" -> usePersonality();
            }
        }
        waitState();
    }

    private static void usePersonality() {
        String response = null;
        JsonObject object;
        do{
            if(response != null && getCommand(response).equals("ERR"))
            {
                view.displayMessage("Error: "+ getParameter(response));
            }
            object = view.usePersonality();
            if(object == null)
                return;
            sendObject("use_personality", object);
            response = receiveCommand();
        }while (!getCommand(response).equals("ACK"));
    }

    private static void cloudSelection() {
        String response = null;
        int cloud;

        do{
            if(response != null && getCommand(response).equals("ERR"))
            {
                view.displayMessage("Error: " + getParameter(response));
                view.displayMessage("Please repete selection");
            }
            cloud = view.selectCloud();
            sendObject("select_cloud", cloud);
            response = receiveCommand();
        }while (!getCommand(response).equals("ACK"));


        view.removeCommand("select_cloud");
    }


    public static void sendText(String message)  {
       sendObject(message, null);
    }
    public  static  void sendObject(String command, Object o)  {
        String out;
        if(o != null) {
            String json = gson.toJson(o);
            out = command + " " + json;
        } else
            out = command;
        try {
            objectToServer.writeObject(out);
            objectToServer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * waits a response from the server and returns the received response.
     * @return response from server
     */
    public static String receiveCommand(){
        try {
            return (String) objectFromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static String getCommand(String input){
        if(input == null || input.isEmpty() || input.split(" ", 2)[0].isEmpty())
            return null;
        return input.split(" ", 2)[0];
    }
    public static String getParameter(String input){
        if(input == null || input.isEmpty() || input.split(" ", 2)[1].isEmpty())
            return null;
        return input.split(" ", 2)[1];
    }
}
