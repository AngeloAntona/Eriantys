package it.polimi.ingsw.am54.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * Tower is used to mark islands that player controls<br>
 * They have one or two owners depending on total number of players<br>
 *  2-3 players -> single owner <br>
 *  4 players   -> two owners
 */
public class Tower {
    private final TColor color;
    private final int[] owners;

    /**
     * Constructs tower, setting its color and owner<br>
     * Used if game is played by 2-3 players
     * @param color tower's color
     * @param owner owner
     */
    public Tower(TColor color, int owner) {
        this.owners = new int[2];
        this.color = color;
        this.owners[0] = owner;
    }
    /**
     * Constructs tower, setting its color and owners<br>
     * Used if game is played by 4 players
     * @param color tower's color
     * @param owners owners
     */
    public Tower(TColor color, int[] owners) {
        this.color = color;
        this.owners = owners;
    }

    /**
     * Returns color of tower
     * @return tower's color
     */
    public TColor getColor() {
        return color;
    }

    /**
     * Returns owner(s) of tower
     * @return array containing owners IDs, if there is single owner second array member is 0
     */
    public int[] getOwners() {
        return owners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tower tower = (Tower) o;
        return color == tower.color && Arrays.equals(owners, tower.owners);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(color);
        result = 31 * result + Arrays.hashCode(owners);
        return result;
    }
}
