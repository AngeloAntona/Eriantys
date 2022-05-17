package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Set of tests that ensures expected behavior of class GameBoard and its methods
 */
public class GameBoardTest {

    /**
     * Checks if owner is set correctly
     * @see  GameBoard#getOwner() 
     * @see  GameBoard#GameBoard(int) 
     */
    @Test
    public void ownerTest(){
        int expected = 2;

        GameBoard gb = new GameBoard(expected);
        assertEquals(expected, gb.getOwner());
    }

    /**
     * Checks if students in the entrance can be manipulated correct way, by adding and removing them
     * @see  GameBoard#getStudentsEnter() 
     * @see  GameBoard#addStudentsEnter(List) 
     */
    @Test
    public void studentEnterTest(){
        GameBoard gb = new GameBoard(2);
        List<Color> expected = new ArrayList<>(List.of(Color.values()));

        gb.addStudentsEnter(expected);

        int i = 0;

        for (Color student:expected) {
            assertEquals(student, gb.getStudentsEnter().get(i));
            i++;

        }

        List<Color> removed = new ArrayList<>();
        removed.add(Color.BLUE);
        removed.add(Color.RED);

        for (Color student:removed) {
            expected.remove(student);
        }
        gb.removeStudentsEnter(removed);

        assertEquals(gb.getStudentsEnter().size(), expected.size());
        assertTrue(gb.getStudentsEnter().containsAll(expected));

    }

    /**
     * Checks if students in the hall can be manipulated correct way, by adding and removing them
     * @see  GameBoard#getStudentsHall(Color)
     * @see  GameBoard#addStudentHall(Color)
     */
    @Test
    public void studentHallTest(){
        GameBoard gb = new GameBoard(2);

        List<Color> students = new ArrayList<>(List.of(Color.RED, Color.RED, Color.RED, Color.RED, Color.BLUE, Color.YELLOW, Color.YELLOW));

        int expectedRed = 4, expectedBlue = 1, expectedYellow = 2;

        for (Color c: students) {
            gb.addStudentHall(c);
        }

        assertEquals(expectedRed, gb.getStudentsHall(Color.RED));
        assertEquals(expectedBlue, gb.getStudentsHall(Color.BLUE));
        assertEquals(expectedYellow, gb.getStudentsHall(Color.YELLOW));
        int rem = 2;
        expectedRed -= rem;
        gb.removeStudentHall(Color.RED,rem);

        //only red should change
        assertEquals(expectedRed, gb.getStudentsHall(Color.RED));
        assertEquals(expectedBlue, gb.getStudentsHall(Color.BLUE));
        assertEquals(expectedYellow, gb.getStudentsHall(Color.YELLOW));

    }

    /**
     * Tests functionality of list that contains currently owned professors by asserting that owner of every added
     * professor is same as owner of game board
     * @see  GameBoard#addProf(Professor)
     * @see  GameBoard#removeProf(Professor)
     */
    @Test
    public void profTest()
    {
        int owner = 2;
        GameBoard gb = new GameBoard(owner);

        Professor prof1 = new Professor(Color.RED, 0);
        Professor prof2 = new Professor(Color.BLUE, 1);

        gb.addProf(prof1);
        gb.addProf(prof2);

        assertEquals(2, gb.getProf().size());
        assertEquals(gb.getProf().get(0),prof1);
        assertEquals(gb.getProf().get(1),prof2);
        assertEquals(prof1.getOwner(), owner);

        gb.removeProf(prof1);
        assertEquals(1, gb.getProf().size());
        assertEquals(gb.getProf().get(0),prof2);
    }

    /**
     * Checks if correct amount of coins is added to game board when filling the hall with students.<br>
     * Also assures that coins are gained if same spot is filled multiple times <br>
     * Spending of coins is also tested
     * @see  GameBoard#getCoins()
     * @see  GameBoard#spendCoins(int)
     * @see  GameBoard#addStudentHall(Color)
     */
    @Test
    public void coinTest()
    {
        GameBoard gb = new GameBoard(2);

        assertEquals(0, gb.getCoins()); // initial value should be  0


        int expectedCoins = 3;
        for (int i =0; i < 10; i++)
            gb.addStudentHall(Color.PINK);

        assertEquals(expectedCoins, gb.getCoins());

        gb.removeStudentHall(Color.PINK,3);

        for (int i =0; i < 3; i++)
            gb.addStudentHall(Color.PINK);

        assertEquals(expectedCoins, gb.getCoins());// number of coins shouldn't change if we pass position 9 two times

        expectedCoins -= 2;
        gb.spendCoins(2);
        assertEquals(expectedCoins, gb.getCoins());
    }

    /**
     * Test assures that adding and removing towers functions correctly
     * @see  GameBoard#addTower(List)
     * @see  GameBoard#removeTower(List)
     */
    @Test
    public void towerGBTest()
    {
        int owner = 2;
        GameBoard gb = new GameBoard(owner);
        List<Tower> expected = new ArrayList<>();
        for(int i = 0; i < 6; i++)
        {
            Tower t = new Tower(TColor.WHITE, owner);
            expected.add(t);
            gb.addTower(List.of(t));

        }
        assertEquals(expected.size(), gb.getTowers().size());
        assertTrue(expected.containsAll(gb.getTowers()));


        gb.removeTower(List.of(expected.get(0), expected.get(3), expected.get(5)));



        assertEquals(expected.size()-3, gb.getTowers().size());
        assertTrue(expected.containsAll(gb.getTowers()));


    }


}