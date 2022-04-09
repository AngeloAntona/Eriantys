package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assures that class Tower behaves in expected way
 */
public class TowerTest {
    /**
     * Test if constructor creates correct instance of class Tower when there is single owner
     * @see Tower#Tower(TColor, int)
     * @see Tower#getOwners()
     */
    @Test
    public void singleOwnerTest()
    {
        int owner = 1;
        Tower t = new Tower(TColor.WHITE, owner);

        //when we have single owner of tower we expect that second position in array owners is 0
        int[] expected = {owner, 0};

        for(int i = 0; i < expected.length; i++)
            assertEquals(expected[i], t.getOwners()[i]);
    }
    /**
     * Test if constructor creates correct instance of class Tower when there are multiple owners
     * @see Tower#Tower(TColor, int[])
     * @see Tower#getOwners()
     */
    @Test
    public void doubleOwnerTest()
    {
        int[] expected = {2,4};
        Tower t = new Tower(TColor.WHITE, expected);

        for(int i = 0; i < expected.length; i++)
            assertEquals(expected[i], t.getOwners()[i]);
    }

    /**
     * Assures that tower has correct color associated to it
     * @see Tower#getColor()
     * @see Tower#Tower(TColor, int[])
     * @see Tower#Tower(TColor, int)
     */
    @Test
    public void colorTest()
    {
        TColor expected = TColor.BLACK;

        Tower t1 = new Tower(expected, 2);
        Tower t2 = new Tower(expected, new int[]{2, 3});

        assertEquals(expected, t1.getColor());
        assertEquals(expected, t2.getColor()); // to assure that both constructors set right color
    }

}