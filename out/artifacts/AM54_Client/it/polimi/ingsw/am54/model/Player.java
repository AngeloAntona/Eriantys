package it.polimi.ingsw.am54.model;

import java.io.Serializable;

/**
 * Class serves to connect game board and hand which are referred to same player.
 */
public class Player implements Serializable {

    public final int playerId;
    public final Hand hand;
    public final GameBoard gameBoard;

    /**
     * Constructs player, creating game board and hand for that player.
     * @param playerId unique player identifier
     */
    public Player(int playerId){
        this.playerId = playerId;
        gameBoard = new GameBoard(playerId);
        hand = new Hand();
    }

    /**
     * Returns id of player.
     * @return playerID
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Returns Hand connected to player (containing all cards).
     * @return Hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns reference to player's game board.
     * @return gameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
