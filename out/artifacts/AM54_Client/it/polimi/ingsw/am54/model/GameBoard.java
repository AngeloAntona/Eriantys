package it.polimi.ingsw.am54.model;
import static it.polimi.ingsw.am54.model.Constants.MAX_STUDENTS_PER_HALL;

import java.io.Serializable;
import java.util.*;


/**
 *  Class GameBoard is used as game board of each player.<br>
 *  It keeps track of students present in entrance and hall, number of tower and available coins.
 */
public class GameBoard implements Serializable {
    public final int owner;
    public List<Color> studentsEnter;
    public Map<Color, Integer> hall;
    public Map<Color, Integer> nextCoin;
    public List<Professor> profControlled;
    public int coins = 0;
    public List<Tower> towers;
    /**
     * Additional influence points given by Personality's power.
     */
    public int extraInfluence = 0;
    /**
     * Additional Mother Nature moves given by Personality's power.
     */
    public int extraMoves = 0;
    public String username;

    /**
     * Constructs GameBoard, setting owner and initializing all other parameters (hall, entrance, towers, coins).
     * @param owner - playerID of player who is owner of GameBoard
     */
    public GameBoard(int owner) {
        this.owner = owner;
        studentsEnter = new ArrayList<>();
        profControlled = new ArrayList<>();
        towers = new ArrayList<>();
        hall = new HashMap<>();
        nextCoin = new HashMap<>(); /* for each color, specify how far away the coin gain box is */

        for (Color color: Color.values()) {
            hall.put(color, 0);
            nextCoin.put(color,3); /* the first coin of each color is added when the player places the third student (of that color) in the room */
        }
    }

    /**
     * Returns owner of GameBoard.
     * @return playerID of player who is owner of GameBoard
     */
    public int getOwner() {
        return owner;
    }

    /**
     * Returns number of available coins.
     * @return number of coins
     */
    public int getCoins() {
        return coins;
    }
    /**
     * Decreases number of available coins.
     * @param spent number of coins
     */
    public void spendCoins(int spent) {coins -= spent;}

    /**
     * Returns list of students currently present at entrance.
     * @return list of students (colors)
     */
    public List<Color> getStudentsEnter() {
        return List.copyOf(studentsEnter);
    }

    /**
     * Allows adding new students to entrance.
     * @param students list of students (colors)
     */
    public void addStudentsEnter(List<Color> students)
    {
        studentsEnter.addAll(students);
    }

    /**
     * Removes students from entrance.
     * @param students list of students (colors)
     */
    public void removeStudentsEnter(List<Color> students)
    {
        for (Color student:students) {
            studentsEnter.remove(student);
        }
    }

    /**
     * Returns number of students of certain color currently present in the hall.
     * @param color color of students
     * @return number of students
     */
    public int getStudentsHall(Color color)
    {
        return hall.get(color);
    }

    /**
     * Returns list of all students present in the hall.
     * @return list of students
     */
    public List<Color> getAllStudentsHall(){
        List<Color> out = new ArrayList<>();
        for(Color c : Color.values())
        {
            int amount = hall.get(c);

            for (int i = 0; i < amount; i++)
                out.add(c);
        }

        return out;
    }

    /**
     * Allows adding new student to hall.
     * @param student student (color)
     */
    public void addStudentHall(Color student)
    {
        int tmp = hall.get(student);
        if(tmp+1 <= MAX_STUDENTS_PER_HALL){
            hall.put(student,tmp+1);
        }
        else {
            /*could be useful to throw an exception*/
        }

        checkCoinIncrease(student);
    }

    /**
     * Remove students of certain color from the hall.
     * @param color color of students
     * @param num number of students to be removed
     */
    public void removeStudentHall(Color color, int num)
    {
        int tmp = hall.get(color);
        if(tmp - num > 0)
            tmp -= num;
        else
            tmp = 0;
        this.hall.put(color,tmp);
    }

    /**
     * Returns list of professors currently controlled by player.
     * @return list of professors
     */
    public List<Professor> getProf() {
        return List.copyOf(profControlled);
    }

    /**
     * Adds professor to list of controlled professors. <br>
     * Also changes owner of professor.
     * @param prof Professor
     */
    public void addProf(Professor prof) {
        if(prof == null)
            return;
        prof.setOwner(owner);
        profControlled.add(prof);
    }

    /**
     * Removes professor from list of controlled professors.
     * @param prof Professor
     */
    public void  removeProf(Professor prof) {
        if(prof == null)
            return;
        profControlled.remove(prof);
    }

    /**
     * Reruns the list of towers currently available to player.
     * @return list of towers
     */
    public List<Tower> getTowers() {
        /* it copies the list to avoid returning a reference to the internal structure */
        return List.copyOf(towers);
    }

    /**
     * Adds towers to the list of available towers.
     * @param newTowers list of towers
     */
    public void addTower(List<Tower> newTowers)
    {
        towers.addAll(newTowers);
    }

    /**
     * Removes towers from the list of available towers.
     * @param twl list of towers
     */
    public void removeTower(List<Tower> twl)
    {
        for (Tower tower:twl) {
            towers.remove(tower);
        }
    }

    /**
     * Checks if a player has filled the "colorX" section of his hall up to the coin box, and if so he gives him a coin.
     * The threshold for receiving a coin will now be increased to the next coin box.
     * @param color
     */
    private void checkCoinIncrease(Color color)
    {
        if(hall.get(color).equals(nextCoin.get(color)))
        {
            coins++;
            int increase = nextCoin.get(color);
            increase += 3;
            nextCoin.put(color, increase);

        }
    }

    /**
     * returns the username of the gameboard owner.
     */
    public String getUsername() {
        return username;
    }

    /**
     * set the username of the gameboard owner.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
