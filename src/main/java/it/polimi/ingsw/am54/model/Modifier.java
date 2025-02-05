package it.polimi.ingsw.am54.model;

import java.io.Serializable;

/**
 * Class contains all personalities which modify general game logic (e.g. island domination, moves of Mature Nature).
 */
public class Modifier extends Personality implements Serializable {
    public Color noColor;


    /**
     * Constructs Modifier and sets attributes to defaultvalues.
     * @param name name of Personality
     */
    public Modifier(String name) {
        super(name, 0, Constants.PERSONALITIES_STARTING_PRICE.get(name));
        this.noColor = null;
    }

    /**
     * Allows setting of argument noColor.
     * @param color selected color to be set as noColor
     */
    public void setNoColor(Color color){
        noColor = color;
    }

    /**
     * Allows user to select number of additional moves.
     * @return selected number of moves
     */
    public int chooseMoves(){
        //TODO ask the player for a number of moves between 0 and 2
        return 0;
    }


    /**
     * Returns value of noColor.
     * @return Color
     */
    public Color getNoColor(){
        return noColor;
    }
}
