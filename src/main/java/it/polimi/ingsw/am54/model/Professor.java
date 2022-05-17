package it.polimi.ingsw.am54.model;

import java.io.Serializable;

/**
 * Class represents professors that can be controlled by player.<br>
 * There is exactly one professor of each color.
 */
public class Professor implements Serializable {
    public final Color color;
    public int owner;

    /**
     * Constructs instance of professor based on color
     * @param color Color of professor
     * @param owner professor current owner (default is 0)
     */
    public Professor(Color color, int owner) {
        this.owner = owner;
        this.color = color;
    }

    /**
     * Returns professor's color
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns current owner of professor
     * @return playerID
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Allows changing owner of professor
     * @param owner new owner
     */
    public void setOwner(int owner) {
        this.owner = owner;
    }
}
