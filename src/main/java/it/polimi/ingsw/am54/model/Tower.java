package it.polimi.ingsw.am54.model;

//@pure
public class Tower {
    private TColor color;
    private int[] owners;

    public Tower(TColor color, int owner) {
        this.owners = new int[2];
        this.color = color;
        this.owners[0] = owner;
    }
    public Tower(TColor color, int[] owners) {
        this.color = color;
        this.owners = owners;
    }

    public TColor getColor() {
        return color;
    }
    public int[] getOwners() {
        return owners;
    }
}
