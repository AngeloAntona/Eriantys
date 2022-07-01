package it.polimi.ingsw.am54.network.exceptions;
import it.polimi.ingsw.am54.network.Client;

/**
 * This class handles unexpected behaviors on the network.
 */
public class NetworkHandler {
    /**
     * This method is called to handle an unexpected error message coming from the server
     * or an exception that shouldn't be thrown
     * @param e exception thrown
     */
    public static void unexpectedException(Exception e){
        if(e instanceof ServerErrorException){
            Client.getViewController().displayMessage("Error! \nServer says: " + e.getMessage());
        }
    }

    /**
     * This method is called when a player disconnects from the server
     * @param username username of the player that disconnected
     */
    public static void handleDisconnect(String username) {
        Client.getViewController().playerDisconnected(username);
        System.exit(507);
    }

    /**
     * ping function. Sends a ping response to the server.
     */
    public static void ping(){
        Client.sendText("pong");
    }
}
