package it.polimi.ingsw.am54.model;
import java.io.Serializable;
import java.util.*;

import static it.polimi.ingsw.am54.model.Constants.STUDENTS_FOR_EACH_COLOR;

/**
 * The class Bag allows randomized extraction of students
 */
public class Bag implements Serializable  {
    final public List<Color> availableStudents;

    /**
     * Constructs Bag initializing list of students and filling it with appropriate number of students
     */
    public Bag(){
        availableStudents = new ArrayList<>();
        fillBag();
    }

    /**
     * Allows adding(returning) students to bag
     * @param students list of students that will be added to bag
     */
    public void addStudents(List<Color> students){
        availableStudents.addAll(students);
    }


    private void fillBag(){
        for(int i=0; i<STUDENTS_FOR_EACH_COLOR-2; i++){
            //add, for each of the 5 colors in the Color enum, 24 students.
            availableStudents.addAll(List.of(Color.values()));
        }
    }

    /**
     * Returns random student
     * @return random student (color)
     */
    public Color getNextStudent(){
        Collections.shuffle(availableStudents); // a shuffle is performed before each student draw.
        return availableStudents.remove(0);
    }

    /**
     * Checks if bag is empty
     * @return boolean (true if bag is empty)
     */
    public Boolean isEmpty(){
        return availableStudents.isEmpty();
    }
}
