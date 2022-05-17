package it.polimi.ingsw.am54.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.model.Game;
import it.polimi.ingsw.am54.model.Player;
import it.polimi.ingsw.am54.model.TColor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {
    private static final Gson gson = new GsonBuilder().create();


    @Test
    public void receiveTest(){
        int port = 1800;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port "+ port);
            int id = 0;
            Thread client = new Thread(() ->{

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Socket socket = new Socket("localhost", 1800);

                    ObjectOutputStream objectToServer = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectFromServer = new ObjectInputStream(socket.getInputStream());
                    String test = "TEST";
                    objectToServer.writeObject(test);
                    String test2 = "student " + gson.toJson(Color.RED);
                    objectToServer.writeObject(test2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            client.start();
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: "+ socket.getLocalSocketAddress());
                ConnectionManager cm = new ConnectionManager(socket, new ArrayList<>());


          assertEquals("TEST", cm.getCommand(cm.receiveCommand()));
          String nextCommand = cm.receiveCommand();
          assertEquals("student", cm.getCommand(nextCommand));
          assertEquals(Color.RED, gson.fromJson(cm.getParameter(nextCommand), new TypeToken<Color>(){}.getType()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setUsernameTest(){
        int port = 1800;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port "+ port);
            int id = 0;
            Thread client = new Thread(() ->{

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Socket socket = new Socket("localhost", 1800);

                    ObjectOutputStream objectToServer = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectFromServer = new ObjectInputStream(socket.getInputStream());

                    String join = "join_game " + gson.toJson("2 true");
                    objectToServer.writeObject(join);
                    String test = "set_username " + gson.toJson("user1");
                    objectToServer.writeObject(test);
                    objectToServer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            client.start();

            Socket socket = serverSocket.accept();
            System.out.println("New client connected: "+ socket.getLocalSocketAddress());
            ConnectionManager cm = new ConnectionManager(socket, new ArrayList<>());
            cm.setAlive(true);
            cm.start();
            while (cm.getUsername() == null)
                Thread.sleep(500);
            assertEquals("user1", cm.getUsername());
            assertEquals(1, cm.getClientID());
            cm.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void setTColor(){
        int port = 1800;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port "+ port);
            int id = 0;
            Thread client = new Thread(() ->{

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Socket socket = new Socket("localhost", 1800);

                    ObjectOutputStream objectToServer = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectFromServer = new ObjectInputStream(socket.getInputStream());

                    String join = "join_game " + gson.toJson("2 true");
                    objectToServer.writeObject(join);
                    String response = (String) objectFromServer.readObject();
                    String username = "set_username " + gson.toJson("user1");
                    objectToServer.writeObject(username);
                    response = (String) objectFromServer.readObject();
                    ArrayList<TColor> tmp;

                    objectToServer.writeObject("get_available_towers");
                    response = (String) objectFromServer.readObject();
                    String json = response.split(" ", 2)[1];
                    System.out.println(json);
                    tmp  = gson.fromJson(json, new TypeToken<List<TColor>>(){}.getType());
                    String tower = "select_tower " + gson.toJson(tmp.get(0));
                    objectToServer.writeObject(tower);
                    objectToServer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


            });
            client.start();

            Socket socket = serverSocket.accept();
            System.out.println("New client connected: "+ socket.getLocalSocketAddress());
            ConnectionManager cm = new ConnectionManager(socket, new ArrayList<>());
            cm.setAlive(true);
            cm.start();
            while (cm.getSelectedTower() == null)
                Thread.sleep(1000);
            assertEquals(TColor.BLACK, cm.getSelectedTower());
            cm.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void readyToPlayTest(){
        int port = 1800;
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port "+ port);
            int id = 0;
            Thread client = new Thread(() ->{

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Socket socket = new Socket("localhost", 1800);

                    ObjectOutputStream objectToServer = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream objectFromServer = new ObjectInputStream(socket.getInputStream());

                    String join = "join_game " + gson.toJson("2 true");
                    objectToServer.writeObject(join);
                    String test = "player_ready";
                    objectToServer.writeObject(test);
                    objectToServer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            client.start();

            Socket socket = serverSocket.accept();
            System.out.println("New client connected: "+ socket.getLocalSocketAddress());
            ConnectionManager cm = new ConnectionManager(socket, new ArrayList<>());
            cm.setAlive(true);
            cm.start();
            while (!cm.isReadyToStart())
                Thread.sleep(500);

            assertTrue(cm.isReadyToStart());
            cm.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

