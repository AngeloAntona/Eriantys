package it.polimi.ingsw.am54.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents Assistant card and stores value
 * and maximal number of moves that Mother Nature can make.
 */
public class Card implements Serializable {
    public final int value;
    public final int MNMaxMoves;

    /**
     * Constructs card with value and maximal number of moves of Mother Nature.
     * @param value the value of card
     */
    public Card(int value) {

        this.value = value;
        this.MNMaxMoves = (value+1)/2;
    }

    /**
     * Returns value of card.
     * @return card value
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns maximal number of moves that Mother Nature can make.
     * @return max move of Mother Nature
     */
    public int getMaxMoves() {
        return MNMaxMoves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return value == card.value && MNMaxMoves == card.MNMaxMoves;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, MNMaxMoves);
    }
}
