package it.polimi.ingsw.am54.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents islands, containing information about them
 * (number of present towers and students, presence of noEntry tile)
 */
public class Island {
    private final int islandID;
    private int owner;
    private List<Color> studentsPresent;
    private List<Tower> listTowers;
    private boolean noEntry = false;

    /**
     * Constructs Island, initializing list of students and setting owner and island id
     * @param islandID unique island identifier
     * @param owner current owner of island (default is 0)
     */
    public Island(int islandID, int owner) {
        studentsPresent = new ArrayList<>();
        listTowers = new ArrayList<>();
        this.islandID = islandID;
        this.owner = owner;
    }

    /**
     * Returns island identifier
     * @return islandID
     */
    public int getID() {
        return islandID;
    }

    /**
     * Returns owner of island
     * @return playerId (if no one owns island, returns 0)
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Allows setting (changing) owner
     * @param owner new owner (playerID)
     */
    public void setOwner(int owner)
    {
        this.owner = owner;
    }

    /**
     * Allows adding new towers to island
     * @param towers list of towers
     */
    public void addTowers(List<Tower> towers) {listTowers.addAll(towers);}

    /**
     * Returns the list of towers present on island
     * @return list of towers
     */
    public List<Tower> getTowers() {return List.copyOf(listTowers);}

    /**
     * //TODO could be more sensible to use a method moveTowers or setTowers
     * Removes a list of towers if present on the island
     * @param towers to remove
     */
    public void removeTowers(List<Tower> towers){ listTowers.removeAll(towers);}

    /**
     * Returns list of students currently present on island
     * @return list od students (colors)
     */
    public List<Color> getStudents() {
        return List.copyOf(studentsPresent);
    }

    /**
     * Allows adding of new students to island
     * @param students list of students (colors)
     */
    public void addStudents(List<Color> students)
    {
        studentsPresent.addAll(students);
    }

    /**
     * Checks if there is noEntry tile on Island
     * @return boolean (true if noEntry is present)
     */
    public boolean getNoEntry()
    {
        return this.noEntry;
    }

    /**
     * Allows setting noEntry tile to island
     * @param tile boolean state of noEntry
     */
    public void setNoEntry(boolean tile) {
        this.noEntry = tile;
    }
}
