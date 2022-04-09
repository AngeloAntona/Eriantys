package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Set of tests to ensure functionality of class Card and its methods
 */
public class CardTest {

    /**
     * Test that checks if constructor sets correct value of card and if getValue() returns correct number
     * @see Card#getValue()
     */
    @Test
    public void valueTest()
    {
        int value = 5;
        Card c = new Card( value);
        assertEquals(value, c.getValue());
    }

    /**
     * Test checks if maximal number of moves of Mather Nature are calculated and returned correctly. <br>
     * It contains array of expected values witch are compared with calculated ones
     * @see Card#getMaxMoves()
     * @see Card#Card(int)
     */
    @Test
    public void maxMovesTest()
    {
        int[] values = {1,2,3,4,5,6,7,8,9,10};
        int[] expectedMaxMoves = {1,1,2,2,3,3,4,4,5,5};

        for(int i = 0; i < values.length; i++)
        {
            Card c = new Card(values[i]);
            assertEquals(expectedMaxMoves[i], c.getMaxMoves());
        }
    }

}