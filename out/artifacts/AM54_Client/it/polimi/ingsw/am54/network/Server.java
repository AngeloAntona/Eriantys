package it.polimi.ingsw.am54.network;

import it.polimi.ingsw.am54.model.Game;
import it.polimi.ingsw.am54.model.GameThread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as main server which will host multiple games and manage client connections
 */
public class Server{
    protected static final List<GameThread> games = new ArrayList<>();
    public Server(){}
    public static void main(String[] args) {
        int port = 1802;
        boolean status = true; //setting this value to false will stop server

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            //serverSocket.bind(new InetSocketAddress("192.168.187.209",port));
            System.out.println("Address: " + serverSocket.getInetAddress());
            System.out.println("Server is listening on port "+ port);
            int id = 0;
            while (status){
                //accepts new connections and creates threads that communicate with every client
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: "+ socket.getLocalSocketAddress());
                ConnectionManager cm = new ConnectionManager(socket, games);
                cm.start();
                cm.setAlive(true);
            }

            System.out.println("Server shutting down");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
