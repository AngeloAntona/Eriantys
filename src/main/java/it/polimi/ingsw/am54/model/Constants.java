package it.polimi.ingsw.am54.model;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int TOTAL_STUDENTS = 130;
    public static final int STUDENTS_FOR_EACH_COLOR = TOTAL_STUDENTS/Color.values().length;
    public static final int CARD_FOR_EACH_HAND = 10;
    public static final int ISLANDS_AT_START_OF_GAME = 12;
    public static final int MAX_STUDENTS_PER_HALL = 10;
    public static final Map<String, Integer> CONTAINERS_PERSONALITIES= Map.of("winemaker",4, "jester", 6, "courtesan", 4, "botanist", 0);
    public static final Map<String, Integer> PERSONALITIES_STARTING_PRICE = new HashMap<>() {{
        put("winemaker",1);
        put("jester", 1);
        put("botanist", 2);
        put("courtesan", 2);
        put("archer", 1);
        put("baker", 2);
        put("pirate", 3);
        put("faun", 3);
        put("knight", 2);
        put("glutton", 3);
        put("cantor", 1);
        put("witch", 3);
    }};
}
