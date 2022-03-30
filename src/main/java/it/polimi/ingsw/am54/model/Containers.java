package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.*;


public class Containers extends Personality {

    private List<Color> students;
    private int noEntry = 0;
    private int maxStudents;

    public Containers(String name) {

        super(name, 0 ,PERSONALITIES_STARTING_PRICE.get(name) );

        if (name.equals("botanist")) {
            noEntry = 4;
        } else {
            students = new ArrayList<>();
            maxStudents = CONTAINERS_PERSONALITIES.get(name);
        }
    }

    public void addNewStudents(List<Color> newStudents) {
        if (students.size() + newStudents.size() <= maxStudents) {
            students.addAll(newStudents);
        } else {
            throw new RuntimeException("Too many students, max number is " + maxStudents);
        }
    }

    public List<Color> getStudents() {
        return List.copyOf(students);
    }


    public void removeStudents(List<Color> remove) {
        if (!students.containsAll(remove)) {
            throw new RuntimeException(super.getName() + " does not contain all selected students");
        }

        for (Color student : remove)
            students.remove(student);
    }

    public int chooseIsland() {
        //TODO user choice of island
        return 0;
    }

    public void bringBackTile() {
        if (noEntry < 4) {
            noEntry++;
        } else {
            throw new RuntimeException("Too many tiles, max number is 4");
        }
    }

    public boolean useTile() { //returns ture if there are available noEntry tiles (and decreases number of tiles) otherwise returns false

        if(noEntry > 0){
            noEntry--;
            return true;
        }

        return false;
    }

}
