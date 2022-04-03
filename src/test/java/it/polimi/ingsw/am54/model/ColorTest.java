package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {
    /*NOTE: Enumerators are usually not tested but due to override of method toString it is best
    to ensure that overridden method behaves in expected way */
    @Test
    public void stringTest()
    {
        assertEquals("YELLOW", Color.YELLOW.toString());
        assertEquals("BLUE", Color.BLUE.toString());
        assertEquals("GREEN", Color.GREEN.toString());
        assertEquals("RED", Color.RED.toString());
        assertEquals("PINK", Color.PINK.toString());
    }

}