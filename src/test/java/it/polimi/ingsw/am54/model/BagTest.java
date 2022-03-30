package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.am54.model.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    public void addStudentsTest(){
        Bag bag = new Bag();
        List<Color> expected = new ArrayList<>();

        for (Color c: Color.values()) {
            expected.add(c);
            bag.addStudents(List.of(c));
        }

        while(!bag.isEmpty())
        {
            expected.remove(bag.getNextStudent());
        }

        assertTrue(bag.isEmpty());
        assertTrue(expected.isEmpty());

    }

    @Test
    public void emptyTest(){
        Bag b = new Bag();

        assertTrue(b.isEmpty());

        b.addStudents(List.of(Color.YELLOW));

        assertFalse(b.isEmpty());
    }

    @Test
    public void fillBagTest() {
        Map<Color, Integer> expected = new HashMap<>();
        Map<Color, Integer> bagState = new HashMap<>();
        Bag bag = new Bag();

        for (Color c : Color.values()) {
            expected.put(c, STUDENTS_FOR_EACH_COLOR - 2);
            bagState.put(c, 0); //initial count
        }

        bag.fillBag();

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