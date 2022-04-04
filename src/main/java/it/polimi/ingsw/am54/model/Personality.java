package it.polimi.ingsw.am54.model;
import it.polimi.ingsw.am54.model.Game;

import java.util.Objects;

/**
 * Abstract class that serves for creating Modifiers and Containers. <br>
 * Contains methods used by both types
 */
public abstract class Personality {
    private final String name;
    private int cost;
    private int owner;
    private boolean active = false;

    /**
     * Constructor that sets main attributes' values
     * @param name name of personality
     * @param owner owner's Id (default 0)
     * @param cost cost of personality
     */
    public Personality(String name, int owner, int cost){
        this.name = name;
        this.owner = owner;
        this.cost = cost;
    }

    /**
     * Returns name of Personality
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns cost of Personality
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Increases cost of Personality by 1
     */
    public void increaseCost(){
        cost++;
    }

    /**
     * Returns owner of Personality
     * @return playerId
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Allows change of attribute owner
     * @param owner new owner's ID
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }

    /**
     * Checks if Personality is currently active
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Allows changing of  active status
     * @param active new state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    //equals and hasCode are very useful for lists of Personalities
    //NOTE: equals checks just the name (no two cards should have the same name)

    /**
     * Compares only names of two personalities<br>
     * No two cards should have same name
     * @param o Personality to compare with
     * @return true if same name, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personality that = (Personality) o;
        return name.equals(that.name);
    }

    /**
     * Makes usage of hashCode easier
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Allows user to select island
     * @return selected island
     */
    public int chooseIsland(){
        //TODO ask the player for an island
        return 0;
    }
}
