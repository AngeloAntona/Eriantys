package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Checks functionality of class Player and its methods
 */
public class PlayerTest {

    /**
     * Assures that correct ID is given to player by its constructor
     * @see Player#Player(int)
     * @see Player#getPlayerId()
     */
    @Test
    public void playerIdTest(){
        int pid = 3;
        Player p = new Player(pid);
        assertEquals(pid, p.getPlayerId());
    }

    /**
     * Tests if hand is created for player
     * @see Player#Player(int)
     * @see Player#getHand()
     */
    @Test
    public void handTest() {
        Player p = new Player(2);
        assertEquals( Hand.class, p.getHand().getClass());
        assertFalse(p.getHand().getAllCards().isEmpty());
    }

    /**
     * Checks if correct game board is created by constructor
     * @see Player#Player(int)
     * @see Player#getGameBoard()
     */
    @Test
    public void gameBoardTest() {
        Player p = new Player(2);
        assertEquals( GameBoard.class, p.getGameBoard().getClass());
    }
}