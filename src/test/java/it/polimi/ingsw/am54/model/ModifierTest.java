package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModifierTest {

    @Test
    void chooseColorTest() {
        Modifier modifier = new Modifier("");
        assertNotNull(modifier.chooseColor());
    }

    @Test
    void chooseMovesTest() {
        Modifier modifier = new Modifier("");
        int moves = modifier.chooseMoves();
        assertTrue(moves>=0 && moves<=2);
    }

    @Test
    void chooseIslandTest() {
        Modifier modifier = new Modifier("");
        int islandID = modifier.chooseIsland();
        assertTrue(islandID>=0 && islandID<=12);
    }
}