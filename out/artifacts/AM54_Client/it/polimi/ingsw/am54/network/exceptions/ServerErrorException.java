package it.polimi.ingsw.am54.network.exceptions;

/**
 * This exception is thrown when the server responds with an ERR message
 * the message of this exception should be the server message.
 */
public class ServerErrorException extends Exception{
    public ServerErrorException(String message) {
        super(message);
    }
}
