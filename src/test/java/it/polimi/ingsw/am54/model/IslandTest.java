package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assures functionality of class Island
 */
public class IslandTest {

    /**
     * Checks if correct ID is set at island by constructor
     * @see  Island#getID()
     * @see Island#Island(int, int)
     */
    @Test
    public void idTest()
    {
        int expectedId = 3;
        Island island = new Island(expectedId, 0);
        assertEquals(expectedId, island.getID());
    }

    /**
     * Tests if correct owner is set by constructor and assures that owner can be changed
     * @see Island#Island(int, int)
     * @see Island#getOwner()
     * @see Island#setOwner(int)
     */
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

    /**
     * Assures that towers can be added to island, removed from it, and that list of all towers can be retrieved
     * @see Island#addTowers(List)
     * @see Island#removeTowers(List)
     * @see Island#getTowers()
     */
    @Test
    public void towerTest()
    {
        Island island = new Island(2,3);

        assertEquals(0,island.getTowers().size()); //check if initial number of towers is 0

        int expected = 7;
        List<Tower> expectedList = new ArrayList<>();
        for(int i = 0; i < expected; i++) {
            Tower t;
            if(i % 2 == 0 ) {
                t = new Tower(TColor.BLACK, 1);
            }
            else
               t = new Tower(TColor.WHITE, 1);
            island.addTowers(List.of(t));
            expectedList.add(t);
        }

        assertEquals(expected, island.getTowers().size());
        assertEquals(expectedList,island.getTowers());

        island.removeTowers(expectedList);
        assertTrue(island.getTowers().isEmpty());
    }

    /**
     * Tests if students can be placed to island and if list of present students contains right values
     * @see Island#addStudents(List)
     * @see Island#getStudents()
     */
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

    /**
     * Assures that initial value of property noEntry is false and that it can be changed to true
     * @see Island#getNoEntry()
     * @see Island#setNoEntry(boolean)
     */
    @Test
    public void noEntryTest()
    {
        Island island = new Island(2,5);

        assertFalse(island.getNoEntry()); // check if initial value of NoEntry is false

        island.setNoEntry(true);
        assertTrue(island.getNoEntry());
    }


}