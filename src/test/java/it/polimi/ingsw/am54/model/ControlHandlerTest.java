package it.polimi.ingsw.am54.model;

import it.polimi.ingsw.am54.network.ConnectionManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

class ControlHandlerTest {

    /**
     * this tests if commandHandler works correctly in the initial phase of a
     * two player game
     */
    /*
    @Test
    void twoPlayersInitialPhase() {
        Socket client1 = new Socket(), client2 = new Socket();
        ObjectOutputStream outCli1 = null;
        ObjectOutputStream outCli2;
        new Thread(()->{

            try {
                outCli1 = client1.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        //ConnectionManager ss1 = new ConnectionManager();
        //ConnectionManager ss2 = new ConnectionManager();
        ControlHandler c = new ControlHandler();
        GameThread game = new GameThread(0, 2);
    }*/

    @Test
    void joinGame() {
    }

    @Test
    void isValidPlayerNumber() {
    }
}