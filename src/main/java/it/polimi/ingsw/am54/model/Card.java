package it.polimi.ingsw.am54.model;

//@pure
public class Card {
    private final int value;
    private final int MNMaxMoves;


    public Card(int value) {
        this.value = value;
        this.MNMaxMoves = (value+1)/2;
    }

    public int getValue() {
        return value;
    }

    public int getMaxMoves() {
        return MNMaxMoves;
    }
}
