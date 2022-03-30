package it.polimi.ingsw.am54.model;

public class Modifier extends Personality{
    private Color noColor;
    private boolean noDraw;
    private boolean noTowers;

    public Modifier(String name) {
        super(name, 0, Constants.PERSONALITIES_STARTING_PRICE.get(name));
        //TODO switch case in which we set noDraw, noTowers to the respective values for the specific cards
        this.noColor = null;
        this.noDraw = false;
        this.noTowers = false;
    }

    public Color chooseColor(){
        //TODO ask the player for a color
        return null;
    }
    public int chooseMoves(){
        //TODO ask the player for a number of moves between 0 and 2
        return 0;
    }
    public int chooseIsland(){
        //TODO ask the player for an island
        return 0;
    }

    public Color getNoColor(){
        return noColor;
    }
    public boolean getNoDraw() {
        return noDraw;
    }
    public boolean getNoTowers() {
        return noTowers;
    }

}
