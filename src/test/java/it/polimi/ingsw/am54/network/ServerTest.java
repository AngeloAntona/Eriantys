package it.polimi.ingsw.am54.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.am54.model.Game;
import it.polimi.ingsw.am54.model.GameThread;
import it.polimi.ingsw.am54.model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @Test
    public void serverTest() {
        Gson gson = new GsonBuilder().create();
        Server server = new Server();
        int old = server.games.size();
        assertEquals(0, old);
        Thread t = new Thread(() -> {
            server.main(new String[]{"",""});
        });
        t.start();


        try {
            Thread.sleep(500);
            Socket socket = new Socket("localhost", 1802);
            ObjectOutputStream objectToServer = new ObjectOutputStream(socket.getOutputStream());

            String join = "join_game " + gson.toJson("2 true");
            objectToServer.writeObject(join);
            Thread.sleep(500);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        assertEquals(old + 1, server.games.size());
        t.interrupt();
    }


}