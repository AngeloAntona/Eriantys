package it.polimi.ingsw.am54.model;

/**
 * Class contains all personalities which modify general game logic (e.g. island domination, moves of Mature Nature)
 */
public class Modifier extends Personality{
    private Color noColor;
    private boolean noDraw;
    private boolean noTowers;

    /**
     * Constructs Modifier and sets attributes to defaultvalues
     * @param name name of Personality
     */
    public Modifier(String name) {
        super(name, 0, Constants.PERSONALITIES_STARTING_PRICE.get(name));
        //TODO switch case in which we set noDraw, noTowers to the respective values for the specific cards
        this.noColor = null;
        this.noDraw = false;
        this.noTowers = false;
    }

    /**
     * Allows player to select Color which will be used for effect of certain card
     * @return selected Color
     */
    public Color chooseColor(){
        //TODO ask the player for a color
        noColor = Color.RED;// temporary solution for test errors
        return noColor;
    }

    /**
     * Allows user to select number of additional moves
     * @return selected number of moves
     */
    public int chooseMoves(){
        //TODO ask the player for a number of moves between 0 and 2
        return 0;
    }


    /**
     * Returns value of noColor
     * @return Color
     */
    public Color getNoColor(){
        return noColor;
    }

    /**
     * Returns value of noDraw
     * @return boolean
     */
    public boolean getNoDraw() {
        return noDraw;
    }

    /**
     * Returns value of noTower
     * @return boolean
     */
    public boolean getNoTowers() {
        return noTowers;
    }

}
