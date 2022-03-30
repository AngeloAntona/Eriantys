package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    public void playerIdTest(){
        int pid = 3;
        Player p = new Player(pid);
        assertEquals(pid, p.getPlayerId());
    }

    @Test
    public void handTest() {
        Player p = new Player(2);
        assertEquals( Hand.class, p.getHand().getClass());
    }

    @Test
    public void gameBoardTest() {
        Player p = new Player(2);
        assertEquals( GameBoard.class, p.getGameBoard().getClass());
    }
}