package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.sql.Array;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {


    @Test
    public void valueTest()
    {
        int value = 5;
        Card c = new Card( value);
        assertEquals(value, c.getValue());
    }

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