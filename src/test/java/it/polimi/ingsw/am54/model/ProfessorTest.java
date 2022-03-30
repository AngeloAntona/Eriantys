package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

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

    @Test
    public void colorTest(){
        Color expected = Color.GREEN;
        Professor professor = new Professor(expected,0);

        assertEquals(expected, professor.getColor());
    }


}