package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

    @Test
    public void idTest()
    {
        int expectedId = 3;
        Island island = new Island(expectedId, 0);
        assertEquals(expectedId, island.getID());
    }

    @Test
    public void ownerTest()
    {
        int expectedOwner = 1;
        Island island = new Island(2, expectedOwner);
        assertEquals(expectedOwner, island.getOwner());

        int newOwner = 3; //in case that other player has taken control of island
        island.setOwner(newOwner);
        assertEquals(newOwner, island.getOwner());
    }

    @Test
    public void towerTest()
    {
        Island island = new Island(2,3);

        assertEquals(0,island.getTower()); //check if initial number of towers is 0

        int expected = island.getTower() + 1;
        island.addTower();
        assertEquals(expected, island.getTower());
    }

    @Test
    public void studentsTest()
    {
        Island island = new Island(5,2);

        List<Color> expected = new ArrayList<>(List.of(Color.values()));

        island.addStudents(expected);

        int i = 0;

        for (Color student:expected) {
            assertEquals(student, island.getStudents().get(i));
            i++;
        }
    }

    @Test
    public void noEntryTest()
    {
        Island island = new Island(2,5);

        assertFalse(island.getNoEntry()); // check if initial value of NoEntry is false

        island.setNoEntry(true);
        assertTrue(island.getNoEntry());
    }


}