package it.polimi.ingsw.am54.model;
import it.polimi.ingsw.am54.model.Game;

import java.util.Objects;

public abstract class Personality {
    private final String name;
    private int cost;
    private int owner;
    private boolean active = false;

    public Personality(String name, int owner, int cost){
        this.name = name;
        this.owner = owner;
        this.cost = cost;
    }
    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public void increaseCost(){
        cost++;
    }
    public int getOwner() {
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    //equals and hasCode are very useful for lists of Personalities
    //NOTE: equals checks just the name (no two cards should have the same name)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personality that = (Personality) o;
        return name.equals(that.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
