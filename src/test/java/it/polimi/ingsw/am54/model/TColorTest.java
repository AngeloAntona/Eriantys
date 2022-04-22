package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enumerators are usually not tested but due to override of method toString it is best
 * to ensure that overridden methods behave in expected way
 */
public class TColorTest {

    /**
     *  Test check if toString returns expected values
     * @see Color
     *  */
    @Test
    public void stringTest()
    {
        assertEquals("BLACK", TColor.BLACK.toString());
        assertEquals("GRAY", TColor.GRAY.toString());
        assertEquals("WHITE", TColor.WHITE.toString());
    }
}