package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the class Professor
 */
public class ProfessorTest {
    /**
     * Assures that initial owner of professor has value 0 and that it can be changed
     * @see Professor#Professor(Color, int)
     * @see Professor#setOwner(int)
     * @see Professor#getOwner()
     */
    @Test
    public void ownerTest()
    {
        int initialOwner = 0;
        Professor professor = new Professor(Color.PINK, initialOwner);

        assertEquals(initialOwner, professor.getOwner());

        int newOwner = 2;
        professor.setOwner(newOwner);

        assertEquals(newOwner, professor.getOwner());
    }

    /**
     * Assures that correct color is set by constructor
     * @see Professor#Professor(Color, int)
     * @see Professor#getColor()
     */
    @Test
    public void colorTest(){
        Color expected = Color.GREEN;
        Professor professor = new Professor(expected,0);

        assertEquals(expected, professor.getColor());
    }


}