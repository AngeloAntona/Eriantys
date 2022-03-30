package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private final int islandID;
    private int owner;
    private List<Color> studentsPresent;
    private int numTower = 0;
    private boolean noEntry = false;

    public Island(int islandID, int owner) {
        studentsPresent = new ArrayList<>();
        this.islandID = islandID;
        this.owner = owner;
    }

    public int getID() {
        return islandID;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    public void addTower()
    {
        numTower++;
    }

    public int getTower()
    {
        return numTower;
    }

    public List<Color> getStudents() {
        return List.copyOf(studentsPresent);
    }

    public void addStudents(List<Color> students)
    {
        studentsPresent.addAll(students);
    }

    public boolean getNoEntry()
    {
        return this.noEntry;
    }

    public void setNoEntry(boolean tile) {
        this.noEntry = tile;
    }
}
