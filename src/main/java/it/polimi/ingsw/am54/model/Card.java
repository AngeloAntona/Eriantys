package it.polimi.ingsw.am54.model;

//@pure

import java.io.Serializable;

/**
 * Represents Assistant card and stores value
 * and maximal number of moves that Mother Nature can make.
 */
public class Card implements Serializable {
    public final int value;
    public final int MNMaxMoves;

    /**
     * Constructs card with value and maximal number of moves of Mother Nature
     * @param value the value of card
     */
    public Card(int value) {

        this.value = value;
        this.MNMaxMoves = (value+1)/2;
    }

    /**
     * Returns value of card
     * @return card value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns maximal number of moves that Mother Nature can make
     * @return max move of Mother Nature
     */
    public int getMaxMoves() {
        return MNMaxMoves;
    }
}
