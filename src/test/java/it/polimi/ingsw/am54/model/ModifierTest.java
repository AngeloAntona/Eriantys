package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;

class ModifierTest {

    @Test
    void chooseColorTest() {
        Modifier modifier = new Modifier("witch");
        assertNotNull(modifier.chooseColor());
    }

    @Test
    void chooseMovesTest() {
        Modifier modifier = new Modifier("witch");
        int moves = modifier.chooseMoves();
        assertTrue(moves>=0 && moves<=2);
    }

    @Test
    void chooseIslandTest() {
        Modifier modifier = new Modifier("witch");
        int islandID = modifier.chooseIsland();
        assertTrue(islandID>=0 && islandID<=12);
    }
}