package it.polimi.ingsw.am54.model;
import static it.polimi.ingsw.am54.model.Constants.MAX_STUDENTS_PER_HALL;
import java.util.*;


public class GameBoard {
    private final int owner;
    private List<Color> studentsEnter;
    private Map<Color, Integer> hall;
    private Map<Color, Integer> nextCoin;
    private List<Professor> profControlled;
    private int coins = 0;
    private List<Tower> towers;
    public int extraInfluence = 0;
    public int extraMoves = 0;

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

    public int getOwner() {
        return owner;
    }

    public int getCoins() {
        return coins;
    }

    public List<Color> getStudentsEnter() {
        return List.copyOf(studentsEnter);
    }

    public void addStudentsEnter(List<Color> students)
    {
        studentsEnter.addAll(students);
    }

    public void removeStudentsEnter(List<Color> students)
    {
        for (Color student:students) {
            studentsEnter.remove(student);
        }
    }

    public int getStudentsHall(Color color)
    {
        return hall.get(color);
    }

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

    public void removeStudentHall(Color color, int num)
    {
        int tmp = hall.get(color);
        if(tmp - num > 0)
            tmp -= num;
        else
            tmp = 0;
        this.hall.put(color,tmp);
    }

    public List<Professor> getProf() {
        return List.copyOf(profControlled);
    }

    //NOTE: maybe the name addProf() would be more precise
    public void setProf(Professor prof) {
        if(prof == null)
            return;
        prof.setOwner(owner);
        profControlled.add(prof);
    }

    public void  removeProf(Professor prof) {
        if(prof == null)
            return;
        profControlled.remove(prof);
    }

    public List<Tower> getTowers() {
        /* it copies the list to avoid returning a reference to the internal structure */
        return List.copyOf(towers);
    }

    public void addTower(List<Tower> newTowers)
    {
        towers.addAll(newTowers);
    }

    public void removeTower(List<Tower> twl)
    {
        for (Tower tower:twl) {
            towers.remove(tower);
        }
    }


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
}
