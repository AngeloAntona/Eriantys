package it.polimi.ingsw.am54.model;
import java.net.CookieHandler;
import java.util.*;

import static it.polimi.ingsw.am54.model.Constants.STUDENTS_FOR_EACH_COLOR;

public class Bag {
    private List<Color> availableStudents;

    public Bag(){
        availableStudents = new ArrayList<Color>();
    }

    public void addStudents(List<Color> students){
        availableStudents.addAll(students);
    }

    public void fillBag(){
        for(int i=0; i<STUDENTS_FOR_EACH_COLOR-2; i++){
            /* add, for each of the 5 colors in the Color enum, 24 students.
            (I implemented it in this way to make it easier to add any new colors) */
            availableStudents.addAll(List.of(Color.values()));
        }
    }

    /* a shuffle is performed before each student draw. */
    public Color getNextStudent(){
        Collections.shuffle(availableStudents);
        return availableStudents.remove(0);
    }

    public Boolean isEmpty(){
        return availableStudents.isEmpty();
    }
}
