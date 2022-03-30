package it.polimi.ingsw.am54.model;

public class Professor {
    private Color color;
    private int owner;

    public Professor(Color color, int owner) {
        this.owner = owner;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
    public int getOwner() {
        return owner;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }
}
