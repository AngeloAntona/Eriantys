package it.polimi.ingsw.am54.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.*;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.network.exceptions.NetworkHandler;
import it.polimi.ingsw.am54.view.CLIController;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.View;
import it.polimi.ingsw.am54.view.ViewController;
import it.polimi.ingsw.am54.view.cli.CLI;
import it.polimi.ingsw.am54.view.gui.GUI;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class represents a client.
 */
public class Client {
    private static int id;
    private static Socket socket;
    protected static ArrayList<Card> cardsInHand;
    protected static Card cardPlayed = null;
    protected static ObjectInputStream objectFromServer;
    protected static ObjectOutputStream objectToServer;
    private static final Gson gson = new GsonBuilder().create();
    public static ViewController viewController;
    private static View view;
    private static int movedInTurn = 0;
    private static GameBoard myGameBoard = null;

    public static void startGame(){
        sendText("player_ready");
    }

    public static void main(String[] args) {
        if (args.length == 1 && args[0].equals("cli")){
            view = new CLI();
        } else{
            view = new GUI();
        }
        view.initialize();
    }

    /**
     * This method tries to establish a connection to the server with the address and the port specified.
     * If successful creates an ObjectOutputStream and an ObjectInputStream from the socket.
     * @param address address of the server to connect to
     * @param port port of the server to connect to
     * @throws UnknownHostException if the address specified isn't a valid address
     * @throws IOException if the connection fails
     */
    public static void connectToServer(String address, int port) throws IOException {
        try{
            //tries to connect to given server if it exists and has open port
            socket = new Socket();
            socket.connect(new InetSocketAddress(address,port), 500);
            //creates input and output streams for objects
            //NOTE: Order of streams is important in both server and client, can cause error invalid stream header
            objectToServer = new ObjectOutputStream(socket.getOutputStream());
            objectFromServer = new ObjectInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            throw new UnknownHostException();
        } catch (IOException e){
            throw new IOException(e);
        }
    }

    /**
     * Sends and "end" message to the server which tells the server to close the current game.
     * This method also closes the socket and the Application.
     */
    public static void end(){
        sendText("end");
        try {
            objectFromServer.close();
            objectToServer.close();
            socket.close(); //closes connection
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a "join_game" message to the server. If the server responds with an ACK
     * this method returns the id of the player in the game otherwise throws an exception.
     * @param options game options in a Json format. These options specify which type of
     *                game the player wants to join.
     * @return the id that identifies the player inside a single game.
     * @throws ServerErrorException if the server doesn't respond with an ack
     */
    public static int joinGame(String options) throws ServerErrorException{
        sendObject("join_game", options);
        String response = receiveCommand();
        if(!getCommand(response).equals("ACK"))
            throw new ServerErrorException(getParameter(response));
        id = gson.fromJson(getParameter(response), new TypeToken<Integer>() {}.getType());
        return id;
    }

    /**
     * this method is used to send command to the server
     * @param command string command that we need
     * @param obj object parameter to send to the server
     * @throws ServerErrorException if the server responds with "ERR"
     */
    public static void command(String command, Object obj) throws ServerErrorException{
        sendObject(command, obj);
        String response = receiveCommand();
        System.out.println("comand: "+command+" response>"+response);
        if(!getCommand(response).equals("ACK"))
            throw new ServerErrorException(getParameter(response));
    }


    /**
     * this method returns the list of the available mages for the initial phase of the game.
     * The list could be not synchronized with the server. To be sure if a mage is available or not
     * you have to use selectMage method
     * @return list of available mages
     */
    public static List<Mage> getAvailableMages(){
        List<Mage> available;
        sendText("get_available_mages");
        String response = receiveCommand();
        if(!getCommand(response).equals("available_mages")){
            NetworkHandler.unexpectedException(new ServerErrorException(getParameter(response)));
            return null;
        }
        available = gson.fromJson(getParameter(response), new TypeToken<List<Mage>>(){}.getType());
        return available;
    }

    /**
     * this method returns the list of the available color for the initial phase of the game.
     * @return list of available TowersColors.
     */
    public static List<TColor> getAvailableTowerColors(){
        List<TColor> available;
        sendText("get_available_towers");
        String response = receiveCommand();
        if(!getCommand(response).equals("available_towers")){
            NetworkHandler.unexpectedException(new ServerErrorException(getParameter(response)));
            return null;
        }
        available = gson.fromJson(getParameter(response), new TypeToken<List<TColor>>(){}.getType());
        return available;
    }

    /**
     * Allows you to send a textMessage.
     * @param message
     */
    public static void sendText(String message)  {
       sendObject(message, null);
    }

    /**
     * Allows you to send an objectMessage.
     * @param command
     * @param o
     */
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
        } catch (SocketException e){
            System.exit(1);
            //throw new RuntimeException(e);
        } catch (IOException e) {
            System.exit(1);
            //throw new RuntimeException(e);
        }

    }

    /**
     * @return viewController
     */
    public static ViewController getViewController() {
        return viewController;
    }

    /**
     * waits for a response from the server and returns the received response.
     * @return response from server
     */
    public static String receiveCommand(){
        try {
            String resp = (String) objectFromServer.readObject();

            if(resp.contains("player_disconnected")) {
                NetworkHandler.handleDisconnect(getParameter(resp));
                return null;
            }
            if(resp.contains("ping")) {
                NetworkHandler.ping();
                return receiveCommand();
            }
            if(resp.contains("update")){
                Gson gson = new Gson();
                String json = getParameter(resp);
                updateMessage update = gson.fromJson(json, new TypeToken<updateMessage>(){}.getType());
                viewController.update(update);
                return receiveCommand();
            }
            return resp;
        } catch (SocketException e){
            end();
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It is used after a move, when it expects an update of moves made by the same client who receives the update.
     */
    public static void receiveSelfUpdate(){
        String resp;
        try {
            resp = (String) objectFromServer.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(resp.contains("update")){
            Gson gson = new Gson();
            String json = getParameter(resp);
            updateMessage update = gson.fromJson(json, new TypeToken<updateMessage>(){}.getType());
            viewController.update(update);
        }else{
            viewController.displayMessage("Error while updating data, some data may not been updated");
        }
    }

    /**
     *it is used to extract the command from the message received from the server.
     * @param input
     * @return
     */
    public static String getCommand(String input){
        if(input == null || input.isEmpty() || input.split(" ", 2)[0].isEmpty())
            return null;
        return input.split(" ", 2)[0];
    }

    /**
     *it is used to extract the parameter from the message received from the server.
     * @param input
     * @return
     */
    public static String getParameter(String input){
        if(input == null || input.isEmpty() || input.split(" ", 2)[1].isEmpty())
            return null;
        return input.split(" ", 2)[1];
    }
}
