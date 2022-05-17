package it.polimi.ingsw.am54.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.Card;
import it.polimi.ingsw.am54.model.Color;
import it.polimi.ingsw.am54.model.TColor;
import it.polimi.ingsw.am54.view.CLI;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.am54.model.Constants.CARD_FOR_EACH_HAND;
import static java.lang.Thread.interrupted;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private final Gson gson = new GsonBuilder().create();
//TODO tests require modifications due to changing implementation of Client
/*
    @Test
    public void joinGameTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "2\nno\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.joinGame();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();

            assertEquals("join_game",getCommand(response));
            String json = getParameter(response);
            assertEquals("2 false", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            String ack = "ACK 1";
            out.writeObject(ack);
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void joinGameErrorTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "8\n2\ndd\nno\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                assertFalse(Client.joinGame());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();

            assertEquals("join_game",getCommand(response));
            String json = getParameter(response);
            assertEquals("2 false", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            String err = "ERR";
            out.writeObject(err);
            response = (String) in.readObject();
            assertEquals("end",getCommand(response));
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void setUsernameTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "\nuser1\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.setUsername();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();

            assertEquals("set_username",getCommand(response));
            String json = getParameter(response);
            assertEquals("", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            out.writeObject("ERR empty name");

            response = (String) in.readObject();

            assertEquals("set_username",getCommand(response));
             json = getParameter(response);
            assertEquals("user1", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            String ack = "ACK";
            out.writeObject(ack);
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void setMageTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "59\n0\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.selectMage();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            List<Mage> mages = new ArrayList<>(List.of(Mage.values()));
            String response = (String) in.readObject();

            assertEquals("get_available_mages",getCommand(response));
            String res = "available_mages " + gson.toJson(mages);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_mage", getCommand(response));

            assertEquals(mages.get(0), gson.fromJson(getParameter(response), new TypeToken<Mage>(){}.getType()) );
            String ack = "ACK";
            out.writeObject(ack);
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void setTowerTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "59\n0\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.selectTowerColor();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            List<TColor> towers = new ArrayList<>(List.of(TColor.values()));
            String response = (String) in.readObject();

            assertEquals("get_available_towers",getCommand(response));
            String res = "available_towers " + gson.toJson(towers);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_tower", getCommand(response));

            assertEquals(towers.get(0), gson.fromJson(getParameter(response), new TypeToken<TColor>(){}.getType()) );
            String ack = "ACK";
            out.writeObject(ack);
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectAssistantCardTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "59\n0\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.waitState();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }




        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            List<Card> cards = new ArrayList<>();
            for(int i = 1; i <= CARD_FOR_EACH_HAND; i++) {
                Card newCard = new Card(i);
                cards.add(newCard);
            }
            String assistant = "planning_turn";
            out.writeObject(assistant);
            String response = (String) in.readObject();


            assertEquals("get_cards",getCommand(response));
            String res = "cards_hand " + gson.toJson(cards);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_assistant_card", getCommand(response));
            Card selected = gson.fromJson(getParameter(response), new TypeToken<Card>(){}.getType());
            assertEquals(cards.get(0).value, selected.value );
            String ack = "ACK";
            out.writeObject(ack);
            String end = "end";
            out.writeObject(end);
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void moveMNTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "0\n5\n1\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.selectAssistantCard();
                Client.moveMN();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }




        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            List<Card> cards = new ArrayList<>();
            for(int i = 1; i <= CARD_FOR_EACH_HAND; i++) {
                Card newCard = new Card(i);
                cards.add(newCard);
            }

            String response = (String) in.readObject();
            assertEquals("get_cards",getCommand(response));
            String res = "cards_hand " + gson.toJson(cards);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_assistant_card", getCommand(response));
            Card selected = gson.fromJson(getParameter(response), new TypeToken<Card>(){}.getType());
            assertEquals(cards.get(0).value, selected.value );
            String ack = "ACK";
            out.writeObject(ack);

            response = (String) in.readObject();
            assertEquals("move_mn",getCommand(response));
            assertEquals(1, (Integer) gson.fromJson(getParameter(response), new TypeToken<Integer>(){}.getType()));
            out.writeObject(ack);

            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void moveStudentsTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "2\n3\nred\ngreen\nblue\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();
                Client.moveStudents();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();

            assertEquals("get_students",getCommand(response));
            String resp = gson.toJson(List.of(Color.values()));
            out.writeObject("students_enter " + resp);
            response = (String) in.readObject();
            assertEquals("move_students",getCommand(response));
            JsonObject jobject = gson.fromJson(getParameter(response), new TypeToken<JsonObject>(){}.getType());
            int where = Integer.parseInt(jobject.get("location").toString());
            String json = jobject.get("students").toString();
            ArrayList<Color> students = gson.fromJson(json, new TypeToken<List<Color>>(){}.getType());

            assertEquals(2,where);
            assertEquals(Color.RED, students.get(0));
            assertEquals(Color.GREEN, students.get(1));
            assertEquals(Color.BLUE, students.get(2));
            out.writeObject("ACK");
            Thread.sleep(30);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


        //NOTE: Cannot test main in Client due to Scanner resets
    @Test
    public void completeTest(){
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try (Socket socket = new Socket("localhost", 1800)){
                Client.objectToServer = new ObjectOutputStream(socket.getOutputStream());
                Client.objectFromServer = new ObjectInputStream(socket.getInputStream());
                Client.cardsInHand = new ArrayList<>();

                System.out.println("Client");

                String players = "2\nno\nuser1\n0\n0\n0\n";
                System.setIn(new ByteArrayInputStream(players.getBytes()));
                Client.view = new CLI();

                Client.main(new String[]{""});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }





        });

        try(ServerSocket serverSocket = new ServerSocket(1800)) {
            t.start();
            Socket socket = serverSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            String response = (String) in.readObject();
            assertEquals("join_game",getCommand(response));
            String json = getParameter(response);
            assertEquals("2 false", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            String ack = "ACK 1";
            out.writeObject(ack);

            response = (String) in.readObject();
            assertEquals("set_username",getCommand(response));
            json = getParameter(response);
            assertEquals("user1", gson.fromJson(json, new TypeToken<String>(){}.getType()));
            ack = "ACK";
            out.writeObject(ack);

            List<Mage> mages = new ArrayList<>(List.of(Mage.values()));
             response = (String) in.readObject();
            assertEquals("get_available_mages",getCommand(response));
            String res = "available_mages " + gson.toJson(mages);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_mage", getCommand(response));
            assertEquals(mages.get(0), gson.fromJson(getParameter(response), new TypeToken<Mage>(){}.getType()) );
            out.writeObject(ack);

            List<TColor> towers = new ArrayList<>(List.of(TColor.values()));
             response = (String) in.readObject();
            assertEquals("get_available_towers",getCommand(response));
            res = "available_towers " + gson.toJson(towers);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_tower", getCommand(response));
            assertEquals(towers.get(0), gson.fromJson(getParameter(response), new TypeToken<TColor>(){}.getType()) );
            out.writeObject(ack);
            String wait = "wait";
            out.writeObject(wait);

            List<Card> cards = new ArrayList<>();
            for(int i = 1; i <= CARD_FOR_EACH_HAND; i++) {
                Card newCard = new Card(i);
                cards.add(newCard);
            }
            String assistant = "planning_turn";
            out.writeObject(assistant);

            response = (String) in.readObject();
            assertEquals("player_ready",getCommand(response));

             response = (String) in.readObject();

            assertEquals("get_cards",getCommand(response));
             res = "cards_hand " + gson.toJson(cards);
            out.writeObject(res);
            response = (String) in.readObject();
            assertEquals("select_assistant_card", getCommand(response));
            Card selected = gson.fromJson(getParameter(response), new TypeToken<Card>(){}.getType());
            assertEquals(cards.get(0).value, selected.value );
            out.writeObject(ack);
            String end = "end";
            out.writeObject(end);

            t.join();
            assertFalse(t.isAlive());

        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }*/

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

}