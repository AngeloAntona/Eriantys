package it.polimi.ingsw.am54.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am54.model.GameThread;
import it.polimi.ingsw.am54.model.ControlHandler;
import it.polimi.ingsw.am54.model.TColor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Class does initial interaction with server in which client decides how many players should be in game.
 * If there exists already game with that number of players places and not all are filed the player is added to that game,
 * otherwise new game is created.
 */
public class ConnectionManager extends Thread{
    private int clientID;
    private String username = null;
    private TColor selectedTower = null;
    private ControlHandler controlHandler;
    private boolean turnEnd;

    private final Socket socket;
    private final List<GameThread> games;
    private final ObjectInputStream objectFromClient;
    private final ObjectOutputStream objectToClient;
    private final Gson gson = new GsonBuilder().create();
    private boolean alive; // quando e falso si termina thread
    private boolean playerReadyToStart;
    public ConnectionManager(Socket socket, List<GameThread> games) {
        this.playerReadyToStart = false;
        this.socket = socket;
        this.games = games;

        try {
            this.objectFromClient = new ObjectInputStream(this.socket.getInputStream());
            this.objectToClient = new ObjectOutputStream(this.socket.getOutputStream());
            this.socket.setSoTimeout(10*60000); //10 minutes timeout
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        controlHandler = new ControlHandler();
        while (alive)
        {
            try{
                String input = receiveCommand();
                if(input.contains("join_game"))
                    controlHandler.joinGame(getParameter(input), games, this);// solo se si fa join_game si devono inviare anche le partite e riferimento a connectionManager
                else
                    controlHandler.commandHandler(getCommand(input), getParameter(input)); // gestisce i commandi
            } catch (SocketException e){ //this handles client disconnection
                controlHandler.getGameThread().playerDisconnected(this);
            }
        }
    }
    public void sendText(String message)  {
       sendObject(message, null);
    }
    public void sendObject(String command, Object o)  {
        String out;
        if(o != null) {
            String json = gson.toJson(o);
            out = command + " " +  json;
        } else
            out = command;
        try {
            this.objectToClient.writeObject(out);
            this.objectToClient.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String receiveCommand() throws SocketException{
        try {
            return (String) this.objectFromClient.readObject();
        } catch (SocketException | SocketTimeoutException e){
            this.setAlive(false);
            throw new SocketException();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String getCommand(String input){
        if(input == null || input.isEmpty() || input.split(" ", 2)[0].isEmpty())
            return null;
        return input.split(" ", 2)[0];
    }
    public String getParameter(String input){
        System.out.println(input);
        if(input == null || input.isEmpty() || input.split(" ", 2).length != 2 ||input.split(" ", 2)[1].isEmpty())
            return null;
        return input.split(" ", 2)[1];
    }

    public void setAlive(boolean b) {
        alive = b;
    }
    public void setReadyToStart(boolean b){
        playerReadyToStart = b; }
    public boolean isReadyToStart() {
        return playerReadyToStart;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public TColor getSelectedTower() {
        return selectedTower;
    }
    public void setSelectedTower(TColor selectedTower) {
        this.selectedTower = selectedTower;
    }
    public int getClientID() {
        return clientID;
    }
    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public void update() {
        controlHandler.update();
    }

    public boolean isTurnEnd() {
        return turnEnd;
    }

    public void setTurnEnd(boolean turnEnd) {
        this.turnEnd = turnEnd;
    }
}
