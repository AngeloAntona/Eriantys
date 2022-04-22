package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifierTest {
    /**
     * checks if chooseMoves returns an acceptable value
     * (between 0 and 2)
     * @see Modifier#chooseMoves()
     */
    @Test
    void chooseMovesTest() {
        Modifier modifier = new Modifier("witch");
        int moves = modifier.chooseMoves();
        assertTrue(moves>=0 && moves<=2);
    }

    /**
     * checks that setNoColor behaves correctly
     * @see Modifier#setNoColor(Color)
     */
    @Test
    void setNoColorTest(){
        Modifier modifier = new Modifier("witch");
        modifier.setNoColor(Color.GREEN);
        assertEquals(modifier.getNoColor(), Color.GREEN);
    }

    /**
     * checks that getNoColor returns the correct value
     * @see Modifier#getNoColor()
     */
    @Test
    void getNoColor(){
        Modifier modifier = new Modifier("witch");
        modifier.setNoColor(Color.RED);
        assertEquals(modifier.getNoColor(), Color.RED);
    }
}