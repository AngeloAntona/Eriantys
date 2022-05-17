package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.am54.model.Constants.*;

/**
 * All personalities that contain some objects (students or noEntry tiles) fall under this class.<br>
 * If card contains students, they are stored in list and maximal number of students that can be on card
 * at once is retrieved from Constants.<br>
 * If card contains noEntry tiles (botanist), only information that needs to be stored is number of available tiles.<br>
 */

public class Containers extends Personality {

    public List<Color> students;
    public int noEntry = 0;
    public int maxStudents;

    /**
     * Constructs new card Container (type of Personality) that contains students or noEntry tiles(botanist)
     * @param name name of personality
     */
    public Containers(String name) {

        super(name, 0 ,PERSONALITIES_STARTING_PRICE.get(name) );

        if (name.equals("botanist")) {
            noEntry = 4;
        } else {
            students = new ArrayList<>();
            maxStudents = CONTAINERS_PERSONALITIES.get(name);
        }
    }

    /**
     * Adds students to card, can be used both for setting initial students and adding new ones
     * after some are removed
     * @param newStudents list of students that should be added to card
     * @throws RuntimeException if there are more students than maximal number allowed
     */
    public void addNewStudents(List<Color> newStudents) {
        if (students.size() + newStudents.size() <= maxStudents) {
            students.addAll(newStudents);
        } else {
            throw new RuntimeException("Too many students, max number is " + maxStudents);
        }
    }

    /**
     * Method that return list of available students
     * @return list of students (colors)
     */
    public List<Color> getStudents() {
        return List.copyOf(students);
    }

    /**
     * Removes students from card
     * @param remove list of students that should be removed from card
     */
    public void removeStudents(List<Color> remove) {
        if (!students.containsAll(remove)) {
            throw new RuntimeException(super.getName() + " does not contain all selected students");
        }

        for (Color student : remove)
            students.remove(student);
    }


    /**
     * Increase number of available tiles
     * @throws RuntimeException if there are already four tiles on card and method is called
     */
    public void bringBackTile() {
        if (noEntry < 4) {
            noEntry++;
        } else {
            throw new RuntimeException("Too many tiles, max number is 4");
        }
    }
    /**
     * Returns ture if there are available noEntry tiles (and decreases number of tiles) otherwise returns false
     * @return boolean
     */
    public boolean useTile() {
        if(noEntry > 0){
            noEntry--;
            return true;
        }

        return false;
    }

}
