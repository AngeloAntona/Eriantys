package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.am54.model.Constants.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Set of tests to ensure functionality of class Bag and its methods
 */

public class BagTest {

    /**
     * This test will check functionality of adding students to the bag, by filling list by initials students (given by fillBag()) and then adding same students
     * to both list and bag. To check if everything is in order students will be extracted from bag and in same time removed from list. If at the end both list and bag
     * are empty, the test is successful
     * @see Bag#addStudents(List)
     */
    @Test
    public void addStudentsTest(){
        Bag bag = new Bag();
        List<Color> expected = new ArrayList<>();

        for(int i=0; i<STUDENTS_FOR_EACH_COLOR-2; i++){
            /*
             *  this assures that list expected contains same starting students as bag .*/
            expected.addAll(List.of(Color.values()));
        }

        for (Color c: Color.values()) {
            expected.add(c);
            bag.addStudents(List.of(c));
        }

        while(!bag.isEmpty() && !expected.isEmpty())
        {
            expected.remove(bag.getNextStudent());
        }

        assertTrue(bag.isEmpty());
        assertTrue(expected.isEmpty());

    }

    /**
     * Test extracts all initial students from the bag and checks if it is empty after that
     * @see Bag#isEmpty()
     */
    @Test
    public void emptyTest(){
        Bag bag = new Bag();

        assertFalse(bag.isEmpty());

        for(int i=0; i<(STUDENTS_FOR_EACH_COLOR-2) * 5; i++){
            bag.getNextStudent();
        }

        assertTrue(bag.isEmpty());
    }

    /**
     * Test checks functionality of method that initializes students in the bag
     * by counting removed students of each color until bag is empty, and then
     * it confronts counted with expected number
     * @see Bag#Bag()
     */
    @Test
    public void fillBagTest() {
        Map<Color, Integer> expected = new HashMap<>();
        Map<Color, Integer> bagState = new HashMap<>();
        Bag bag = new Bag();

        for (Color c : Color.values()) {
            expected.put(c, STUDENTS_FOR_EACH_COLOR - 2);
            bagState.put(c, 0); //initial count
        }


        while (!bag.isEmpty()) { // counting number of students of each color
            Color color = bag.getNextStudent();
            int tmp = bagState.get(color);
            tmp++;
            bagState.put(color, tmp);
        }

        assertTrue(bag.isEmpty());

        for (Color c : Color.values()) {
            assertEquals(expected.get(c), bagState.get(c));
        }
    }
}