package it.polimi.ingsw.am54.model;

public class Player {

    private final int playerId;
    private final Hand hand;
    private final GameBoard gameBoard;

    public Player(int playerId){
        this.playerId = playerId;
        gameBoard = new GameBoard(playerId);
        hand = new Hand();
    }

    public int getPlayerId() {
        return playerId;
    }

    public Hand getHand() {
        return hand;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }
}
