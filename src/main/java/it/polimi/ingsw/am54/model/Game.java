package it.polimi.ingsw.am54.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static it.polimi.ingsw.am54.model.Constants.CONTAINERS_PERSONALITIES;
import static it.polimi.ingsw.am54.model.Constants.ISLANDS_AT_START_OF_GAME;

/**
 * Main class that controls game flow and its principal logic.
 */
public class Game implements Serializable {
    public final int gameID;
    /**
     * List of all players.
     */
    public List<Player> listPlayers;
    /**
     * List of available islands.
     */
    public List<Island> islands;
    /**
     * List of available professors.
     */
    public List<Professor> listProfessors;
    public Map<Integer, List<Color>> clouds;
    /**
     * List containing personalities in current game (three).
     */
    public List<Personality> listPersonality;
    public Bag bag;
    /**
     * Winner will be id of player who won the game. <br>
     * While there is no winner, the value is 0.
     */
    public int winner = 0;
    /**
     * Number of turns played.
     */
    public int numTurns = 0;
    public int MotherNature = 0;

    /**
     * Constructs game and initializes attributes.
     *
     * @param gameID     unique game identifier
     * @param numPlayers number of players for current game
     */
    public Game(int gameID, int numPlayers) {
        Map< Integer,TColor> towcol = new HashMap<>() {{
            put(1, TColor.valueOf("BLACK"));
            put(2, TColor.valueOf("WHITE"));
            put(3, TColor.valueOf("GREY"));
            put(4, null);
        }};
        this.gameID = gameID;
        listPlayers = new ArrayList<>();
        islands = new ArrayList<>();
        listProfessors = new ArrayList<>();
        listPersonality = new ArrayList<>();
        startGame(numPlayers, towcol);
    }

    /**
     * Constructs game and initializes attributes.
     *
     * @param gameID     unique game identifier
     * @param numPlayers number of players for current game
     * @param towcol     color of the chosen tower
     */
    public Game(int gameID, int numPlayers, Map< Integer,TColor> towcol) {
        this.gameID = gameID;
        listPlayers = new ArrayList<>();
        islands = new ArrayList<>();
        listProfessors = new ArrayList<>();
        listPersonality = new ArrayList<>();
        startGame(numPlayers, towcol);
    }

    /**
     * Creates instances of objects necessary for game and fills attributes with initial values.
     *
     * @param numPlayers number of players
     */
    private void startGame(int numPlayers,Map<Integer, TColor> towcol) {
        /* it creates a GameBoard for each player (depends on numPlayers). */
        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player(i);
            listPlayers.add(player);
        }
        /*for each player adds Towers*/
        int numTower = (numPlayers == 3) ? 6 : 8;

        if(numPlayers != 4){
            for(int i = 1; i <= numPlayers; i++)
            {
                ArrayList<Tower> towers = new ArrayList<>();
                Player p = getPlayerById(i);
                for(int j = 0; j < numTower; j++) {
                    towers.add(new Tower(towcol.get(i), p.getPlayerId()));
                }
                p.getGameBoard().setTowColor(towcol.get(i));
                p.getGameBoard().addTower(towers);
            }
        } else {
            for(int i = 1; i <= numPlayers; i++)
            {
                ArrayList<Tower> towers = new ArrayList<>();
                Player p = getPlayerById(i);
                p.getGameBoard().setTowColor(towcol.get(i));
                if(i % 2 == 0)
                {
                    for(int j = 0; j < numTower; j++) {
                        towers.add(new Tower(towcol.get(2), p.getPlayerId()));
                    }
                } else {

                    for(int j = 0; j < numTower; j++) {
                        towers.add(new Tower(towcol.get(1), p.getPlayerId()));
                    }
                }
                p.getGameBoard().addTower(towers);

            }
        }
        /*places mother nature to random island and finds it's opposite*/
        MotherNature = ThreadLocalRandom.current().nextInt(0, 11 + 1);
        int opposite = (MotherNature > 5) ? MotherNature - 6 : MotherNature + 6;

        /* creates list of available students to be placed on islands*/
        List<Color> studentsForIslands = new ArrayList<>();
        for(Color c : Color.values()){
            studentsForIslands.addAll(List.of(c,c));
        }

        /* it creates the empty islands */
        for (int i = 1; i <= ISLANDS_AT_START_OF_GAME; i++) {
            Island island = new Island(i, 0);
            islands.add(island);
        }
        /* fills islands with students */
        for (Island is: islands) {
            if(is.getID() != islands.get(opposite).getID() && is.getID() != islands.get(MotherNature).getID()) {
                Collections.shuffle(studentsForIslands);
                is.addStudents(List.of(studentsForIslands.remove(0)));
            }
        }

        /* it creates the list of prof*/
        for (Color color : Color.values()) {
            Professor prof = new Professor(color, 0);
            listProfessors.add(prof);
        }

        /*selects three random personalities */
        List<String> tmp = new ArrayList(Constants.PERSONALITIES_STARTING_PRICE.keySet());
        Collections.shuffle(tmp);
        /*
        for(int i = 0; i < 3; i++)
            listPersonality.add(PersonalityFactory.generate(tmp.get(i)));*/
        listPersonality.addAll(PersonalityFactory.generateAll(List.of("archer", "botanist", "baker")));

        bag = new Bag(); //creates instance of Bag
        addStudentsToPersonalities();

        /*sets initial students to the Entrance of every player*/
        int initialStudents = (numPlayers == 3) ? 9 : 7;
        for(Player p : listPlayers) {
            List<Color> studentsEntering = new ArrayList<>();
            for (int i = 0; i < initialStudents; i++) {
                studentsEntering.add(bag.getNextStudent());
            }

            p.getGameBoard().addStudentsEnter(studentsEntering);
        }

    }

    /**
     * allows to add students on the personality cards that require them.
     */
    private void addStudentsToPersonalities() {
        for(Personality p : listPersonality)
            if(p.getClass() == Containers.class && !p.name.equals("botanist")){
                List<Color> students = new ArrayList<>();

                for (int i = 0; i < ((Containers) p).getMaxStudents(); i++)
                    students.add(bag.getNextStudent());

                ((Containers) p).addNewStudents(students);
            }
    }

    /**
     * Calculates influence of every player on selected island and determines its owner.
     *
     * @param location selected island
     */
    public void islandDomination(Island location) {
        List<Player> tmpList = new ArrayList<>(List.copyOf(listPlayers));
        Map<Player, Integer> playerPoints = new HashMap<>();
        //sort the list in decreasing order and then get the first element
        for(Player p: tmpList) {
            //Domination points for each player who has the most points wins the domination of the island
            int Points = 0;

            //Tower points
            Personality Faun = getPersonalityWithName("faun");
            if (Faun == null || !Faun.isActive()) { //NOTE: if faun is activated towers shouldn't count
                if (location.getOwner() == p.getPlayerId())
                    Points += location.getTowers().size();
            }

            //student points
            for (Color c : location.getStudents()) {
                //if Glutton is active and the color is NoColor goes to the next iteration
                Modifier Glutton = (Modifier) getPersonalityWithName("glutton");
                if (Glutton != null && Glutton.isActive() && Glutton.getNoColor().equals(c) ){
                    continue;
                }


                Professor currentProfofColor = null;
                for (Professor prof : listProfessors) { // selects professor of current color
                    if (prof.getColor().equals(c)) {
                        currentProfofColor = prof;
                        break;
                    }
                }
                //there should be a professor for every color so here currentProfofColor shouldn't be null
                //adds points to the player who has the Professor of that color
                if (p.getGameBoard().getProf().contains(currentProfofColor))
                    Points++;
            }

            //if Knight is active 2 points are added to the corresponding player
            Modifier Knight = (Modifier) getPersonalityWithName("knight");
            if(Knight != null && Knight.isActive()) {
                if (Knight.getOwner() == p.getPlayerId())
                    Points += 2;
            }
            playerPoints.put(p, Points);
        }
        Personality tmp = getActivePersonality();
        if(tmp != null && (tmp.getName().equals("faun") || tmp.getName().equals("knight") || tmp.getName().equals("glutton")))
            tmp.setActive(false);

        //we sort the map playerPoints
        Map<Player, Integer> result = playerPoints.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        // get the first (the most dominant)
        Iterator<Map.Entry<Player, Integer>> iterator = result.entrySet().iterator();
        Player dominant = iterator.next().getKey();

        // if the first and the second are equal do nothing
        if(iterator.next().getValue().equals(result.get(dominant)))
            return;

        Player current = getPlayerById(location.getOwner());
        if (current == null) { //if no One was the owner we set a new owner
            List<Tower> towerToMove = List.of(dominant.getGameBoard().getTowers().get(0));
            location.addTowers(towerToMove);
            dominant.getGameBoard().removeTower(towerToMove);
            location.setOwner(dominant.getPlayerId());
            return;
        }

        if (current.equals(dominant)) //if dominant hasn't changed do nothing
            return;

        //if current!=dominant move the towers
        List<Tower> dominantTowers = dominant.getGameBoard().getTowers().subList(0, location.getTowers().size());
        List<Tower> locationTowers = location.getTowers();

        current.getGameBoard().addTower(locationTowers);
        location.removeTowers(locationTowers);
        location.addTowers(dominantTowers);
        dominant.getGameBoard().removeTower(dominantTowers);

        //the new owner of this island is the dominant one
        location.setOwner(dominant.getPlayerId());
    }

    /**
     * Checks which player controls which professor and assigns the correct professor
     * to the player.
     */
    public void controlsProf() {
        for (Color c : Color.values()) // loops through all possible colors
        {
            Professor prof = null;
            for (Professor p : listProfessors) // selects professor of current color
                if (p.getColor().equals(c)) {
                    prof = p;
                    break;
                }

            List<Player> tmpList = new ArrayList<>(List.copyOf(listPlayers)); // copy of listPlayer is made so that the order of  initial list is unchanged

            tmpList.sort((p1, p2) -> {//compare is written in way that first element of sorted List is max
                if (p1.getGameBoard().getStudentsHall(c) > p2.getGameBoard().getStudentsHall(c))
                    return -1;
                if (p1.getGameBoard().getStudentsHall(c) < p2.getGameBoard().getStudentsHall(c))
                    return 1;
                if (p1.getGameBoard().getStudentsHall(c) == p2.getGameBoard().getStudentsHall(c)) {
                    Personality baker = getPersonalityWithName("baker");//Checks if there is card baker which has influence on deciding who controls prof
                    if (baker != null && baker.isActive() && baker.getOwner() == p1.getPlayerId())
                        return -1;
                    if (baker != null && baker.isActive() && baker.getOwner() == p2.getPlayerId())
                        return 1;
                    if (!p1.getGameBoard().getProf().stream().filter(x->x.getColor().equals(c)).toList().isEmpty())
                        return -1;
                    if (!p2.getGameBoard().getProf().stream().filter(x->x.getColor().equals(c)).toList().isEmpty())
                        return 1;
                }
                return 0;
            });

            Player p = null;

            for (Player listPlayer : listPlayers) {
                if (listPlayer.getPlayerId() == tmpList.get(0).getPlayerId()) {
                    p = listPlayer;
                }
            }

            if (prof.getOwner() != p.getPlayerId() && p.getGameBoard().getStudentsHall(c) > 0) {// ensures that professor are not added more the once to player

                Player oldOwner = getPlayerById(prof.getOwner());

                if(oldOwner != null)
                    oldOwner.getGameBoard().removeProf(prof);

                if (p.getGameBoard().getStudentsHall(prof.getColor()) == tmpList.get(1).getGameBoard().getStudentsHall(prof.getColor())) {
                    // these conditions ensure that if two players have same number of students of selected color, professor is assigned to one of them only if there is active personality card Baker
                    Personality baker = getPersonalityWithName("baker");
                    if (baker != null && baker.isActive() && baker.getOwner() == p.getPlayerId())
                        p.getGameBoard().addProf(prof);
                }
                else {
                    p.getGameBoard().addProf(prof);
                }
            }



        }
        Personality tmp = getActivePersonality();
        if( tmp != null && tmp.getName().equals("baker"))
            tmp.setActive(false);
    }

    /**
     * Determines order in which players will play next round.
     */
    public void nextRound() {
        /*the following instructions are followed if the game is not finished
        (the game cannot be finished already the first round, for this reason I did not put it before):*/
        if (winner == 0) {
            //I sort the list of players in the order in which the players must play:
            /* I override the 'compare' method to be able to sort the elements of listPlayers
            in ascending order according to the numerical value of the card played by each player: */
            listPlayers.sort(Comparator.comparingInt(player -> player.getHand().getCardPlayed().getValue())); //at this point listBoard has been sorted.
        }
        numTurns++; //whatever has happened, the turn number will need to be increased.
    }


    /**
     * Handles movement of Mother Nature.
     * @param playerID player whose turn is to play
     */
    public void moveMN(int playerID, int playerSelection) {
        int maxMoves = getPlayerById(playerID).getHand().getCardPlayed().getMaxMoves();

        if (getActivePersonality() != null && getActivePersonality().getName().equals("archer") && getPersonalityWithName("archer").getOwner() == playerID) {
            maxMoves += 2;
            getPersonalityWithName("archer").setActive(false);
        }


        if (maxMoves < playerSelection || playerSelection <= 0) {
            throw new RuntimeException("Invalid number of moves");
        }
        else {
            MotherNature = (MotherNature + playerSelection) % islands.size(); //Mother Nature's position is calculated in this way so that when MN goes over last island it lands at right position
            Island currentIsland = islands.get(MotherNature);

            if (currentIsland.getNoEntry()) {
                currentIsland.setNoEntry(false);
                Containers botanist = (Containers) getPersonalityWithName("botanist");
                botanist.bringBackTile();
            }
            else {
                islandDomination(currentIsland);
            }

        }
        uniteIslands(MotherNature);
    }

    /**
     * Allows moving students from Entrance to Hall or Island.
     *
     * @param playerId player who moves students
     * @param where    if 0 moves to hall, else moves to island with that ID
     * @param student  Color of student to be moved
     * @throws RuntimeException if selected student is not in player's entrance
     */
    public void moveStudents(int playerId, int where, Color student) {
        Player p = getPlayerById(playerId);

        if (!p.getGameBoard().getStudentsEnter().contains(student)) {
            throw new RuntimeException("Selected student is not in player's entrance"); // this may be changed to other exception later
        }

        if (where == 0) {
            p.getGameBoard().removeStudentsEnter(List.of(student));
            p.getGameBoard().addStudentHall(student);
        }
        else {
            p.getGameBoard().removeStudentsEnter(List.of(student));
            Island isl = islands.get(getIslandPosition(where));
            isl.addStudents(List.of(student));
        }

    }

    /**
     * Checks if any of win conditions are satisfied. <br>
     * If there is winner changes attribute winner to playerID (of winner).
     */
    public void checkWinner() { /* the checkWinner method must be called at the end of each player's moves */

        /* if a player has run out of towers in his gameBoard, I name him the winner: */
        for (Player player : listPlayers) {
            if (player.getGameBoard().getTowers().isEmpty()) {
                winner = player.getPlayerId();
                return;
            }
        }

        int cardFinished = 0;
        for (Player player : listPlayers) {
            List<Card> allCards = player.getHand().getAllCards();
            if (allCards.isEmpty())
                cardFinished++;
        }
        //if the hypotheses of the if are verified, the game is over and the winner will have to be calculated:
        if (this.islands.size() == 3 || bag.isEmpty() || cardFinished > 0) {
            //I sort the players in ascending order of "number of towers":
            /* I override the 'compare' method to be able to sort the elements of listPlayers
            in ascending order according to the numerical value of the card played by each player: */
            listPlayers.sort(Comparator.comparingInt(player -> player.getGameBoard().getTowers().size())); //at this point listBoard has been sorted.

            int varTemp = 0;
            List<Player> almostWinner = new ArrayList<>(); //is the list that will contain the players with the highest number of towers placed on the islands.

            /* I put in the list of winners all the players with the highest number of towers positioned on islands
            (which will be the first on the list we ordered before): */
            almostWinner.add(listPlayers.get(varTemp)); //the first player in the list will certainly have the highest card number
            for(int i = 1; i < listPlayers.size();i++) {
                if (almostWinner.get(0).getGameBoard().getTowers().size() == listPlayers.get(i).getGameBoard().getTowers().size())
                    almostWinner.add(listPlayers.get(i));
            }


            //at this point two cases can occur:
            if (almostWinner.size() == 1) { //there is a player who has placed the most towers of all: NET WIN.

                winner = almostWinner.get(0).getPlayerId();
            }
            else { //DRAW. among the players who tied, I find the one with the highest number of profs controlled:
                Player playerWinner = almostWinner.get(0);

                for (int i = 1;  i < almostWinner.size(); i++) {
                    if (almostWinner.get(i).getGameBoard().getProf().size() > playerWinner.getGameBoard().getProf().size()){

                        playerWinner = almostWinner.get(i);
                    }

                }
                winner = playerWinner.getPlayerId(); //I declare the player with the highest number of controlled profs the winner.
            }
        }
    }



    /**
     * Generates clouds accordingly with number of players.<br>
     * For each player cloud is created and filed with 3 or 4 students extracted from the Bag.
     *
     * @return Map that represents all clouds and students on them
     */
    public Map<Integer, List<Color>> getClouds() {
        Map<Integer, List<Color>> clouds = new HashMap<>();
        int numStudentsOnCloud, numPlayers = listPlayers.size();

        if (numPlayers == 3)
            numStudentsOnCloud = 4;
        else
            numStudentsOnCloud = 3;

        for (int i = 0; i < numPlayers; i++) //for each player creates a cloud
        {
            List<Color> studentsOnCloud = new ArrayList<>();
            for (int j = 0; j < numStudentsOnCloud; j++) //adds appropriate number of students from bag to cloud
                studentsOnCloud.add(bag.getNextStudent());
            clouds.put(i, studentsOnCloud);
        }

        return clouds;
    }

    /**
     * Checks if selected island and one or both adjacent island have same owner, if true they are unified.<br>
     * All students and towers are added to selected island, while others are deleted from list of islands.
     *
     * @param islandId selected island
     * @throws RuntimeException if island is not on the list of available islands
     */
    protected void uniteIslands(int islandId) {
        int current = islandId;

        if (current == -1)
            throw new RuntimeException("Island with id = " + islandId + " is not in the list");

        if(islands.get(current).getOwner() == 0)
            return;
        if (current == 0) { //special case of first island in the list
            if (islands.get(current).getOwner() == islands.get(islands.size() - 1).getOwner()) {
                unifier(current, islands.size() - 1);
            }
            if (islands.get(current).getOwner() == islands.get(current + 1).getOwner()) {
                unifier(current, current + 1);
            }
        }
        else if (current == islands.size() - 1) { //special case of last island in the list
            if (islands.get(current).getOwner() == islands.get(0).getOwner()) {
                unifier(current, 0);
                current--;
            }
            if (islands.get(current).getOwner() == islands.get(current - 1).getOwner()) {
                unifier(current, current - 1);
                MotherNature--;
            }
        }
        else { //general case
            if (islands.get(current).getOwner() == islands.get(current + 1).getOwner()) {
                unifier(current, current + 1);
            }
            if (islands.get(current).getOwner() == islands.get(current - 1).getOwner()) {
                unifier(current, current - 1);
                MotherNature--;
            }
        }
    }


    /**
     * unifies the two islands given as input.
     * @param isl1
     * @param isl2
     */
    private void unifier(int isl1, int isl2)//helper method to uniteIslands
    {
        islands.get(isl1).addStudents(islands.get(isl2).getStudents());
        islands.get(isl1).addTowers(islands.get(isl2).getTowers());
        islands.remove(isl2);
        if(MotherNature == isl2)
            MotherNature = isl1;
    }

    /**
     * Returns identifier of current game.
     *
     * @return gameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Returns all available personalities in current game (three personalities).
     *
     * @return copy of personality list
     */
    public List<Personality> getPersonality() {
        return List.copyOf(listPersonality);
    }

    /**
     * Return all available professors.
     *
     * @return copy of list of professors
     */
    public List<Professor> getProfessors() {
        return List.copyOf(listProfessors);
    }

    /**
     * Handles effects of activated card. <br>
     * Effects are determined based on card name
     *
     * @param card selected personality card
     * @param object
     * @param playerID
     */
    public String usePersonalityPower(Personality card, int playerID, JsonObject object) {
        Player player = getPlayerById(playerID);

        if (getPersonalityWithName(card.getName()) == null || !listPersonality.contains(card))
            return  "Non existing personality";

        if (player == null)
            return ("Non existing player");

        if (getPlayerById(playerID).getGameBoard().getCoins() < card.getCost())
            return ("Not enough money");

        card.setActive(true);
        card.setOwner(playerID);//changes card owner
        String error;
        if(CONTAINERS_PERSONALITIES.containsKey(card.getName()))
            error = useContainersPower((Containers) card,player, object);
        else
            error = useModifierPower((Modifier) card,player, object);

        if(error != null) {
            card.setActive(false);
            card.setOwner(0);
            return error;
        }

        player.getGameBoard().spendCoins(card.getCost()); //decreases amount of coins that player has
        card.increaseCost();//increases cost for future uses of Personality card


        return "OK"; // id everything went according to plane;
    }

    /**
     * Handles the powers of the container cards.
     * @param card
     * @param player
     * @param object
     */
    private String useContainersPower(Containers card, Player player, JsonObject object)
    {
        GameBoard gb = player.getGameBoard();

        switch (card.getName()){
            case "botanist" -> {
                int is = Integer.parseInt(object.get("island").toString());
                if(getIslandPosition(is) == -1)
                    return ("Island with id " + is + "doesn't exist");
                if (!card.useTile()) {
                    return ("No more tiles available");
                }

                islands.get(getIslandPosition(is)).setNoEntry(true);
            }
            case "winemaker" -> {
                Gson gson = new Gson();
                String color = gson.fromJson(object.get("color"), new TypeToken<String>(){}.getType());
                Color student = Color.valueOf(color);
                int selectedIsland = Integer.parseInt(object.get("island").toString());

                if(getIslandPosition(selectedIsland) == -1)
                    return ("Island with id " + selectedIsland + "doesn't exist");

                card.removeStudents(List.of(student));
                islands.get(getIslandPosition(selectedIsland)).addStudents(List.of(student));
                card.addNewStudents(List.of(bag.getNextStudent()));
            }
            case "jester" -> {
                Gson gson = new GsonBuilder().create();
                String fromCard = object.get("studentsFromCard").toString();
                String fromEntrance = object.get("studentsFromEntrance").toString();
                List<Color> studentsFromCard = gson.fromJson(fromCard, new TypeToken<List<Color>>(){}.getType());
                List<Color> studentsFromEntrance = gson.fromJson(fromEntrance, new TypeToken<List<Color>>(){}.getType());
                if(!new HashSet<>(gb.getStudentsEnter()).containsAll(studentsFromEntrance) || !new HashSet<>(card.getStudents()).containsAll(studentsFromCard))
                    return "Not all selected students present";

                card.removeStudents(studentsFromCard);
                card.addNewStudents(studentsFromEntrance);
                gb.removeStudentsEnter(studentsFromEntrance);
                gb.addStudentsEnter(studentsFromCard);
            }
            case "courtesan" -> {
                Gson gson = new Gson();
                String color = gson.fromJson(object.get("color"), new TypeToken<String>(){}.getType());
                Color selectedStudent = Color.valueOf(color);

                if(!card.getStudents().contains(selectedStudent))
                    return "Select student is not on the card";

                card.removeStudents(List.of(selectedStudent));
                gb.addStudentHall(selectedStudent);
                card.addNewStudents(List.of(bag.getNextStudent()));
            }
        }
        card.setActive(false);
        return null;
    }

    /**
     * Handles the powers of the modifier cards.
     * @param card
     * @param player
     * @param object
     */
    private String useModifierPower(Modifier card, Player player, JsonObject object){
        GameBoard gb = player.getGameBoard();
        switch (card.getName()) {
            case "pirate" -> {
                int selectedIsland = Integer.parseInt(object.get("island").toString());

                if(getIslandPosition(selectedIsland) == -1)
                    return ("Island with id " + selectedIsland + "doesn't exist");

                selectedIsland = getIslandPosition(selectedIsland);
                islandDomination(islands.get(selectedIsland));
            }

            case "glutton" -> {
                Gson gson = new Gson();
                String color = gson.fromJson(object.get("color"), new TypeToken<String>(){}.getType());
                Color selectedColor = Color.valueOf(color);
                card.setNoColor(selectedColor);
                return null;
            }

            case "cantor" -> {
                Gson gson = new GsonBuilder().create();
                String fromHall = object.get("studentsFromHall").toString();
                String fromEntrance = object.get("studentsFromEntrance").toString();
                List<Color> studentsFromHall = gson.fromJson(fromHall, new TypeToken<List<Color>>(){}.getType());
                List<Color> studentsFromEntrance = gson.fromJson(fromEntrance, new TypeToken<List<Color>>(){}.getType());
                if(!new HashSet<>(gb.getStudentsEnter()).containsAll(studentsFromEntrance) || !new HashSet<>(gb.getAllStudentsHall()).containsAll(studentsFromHall))
                    return "Not all selected students present";

                gb.removeStudentsEnter(studentsFromEntrance);
                gb.addStudentsEnter(studentsFromHall);

                for (Color c : studentsFromHall) {
                    gb.removeStudentHall(c, 1);
                }
                for (Color c : studentsFromEntrance) {
                    gb.addStudentHall(c);
                }
            }

            case "witch" -> {
                Gson gson = new Gson();
                String color = gson.fromJson(object.get("color"), new TypeToken<String>(){}.getType());
                Color selectedColor = Color.valueOf(color);
                int totRemoved = 0, studentCount;

                for (Player ply : listPlayers) {
                    studentCount = ply.getGameBoard().getStudentsHall(selectedColor);

                    if (studentCount > 3)
                        studentCount = 3;

                    totRemoved += studentCount;
                    ply.getGameBoard().removeStudentHall(selectedColor, studentCount);
                }
                for (int i = 0; i < totRemoved; i++)
                    bag.addStudents(List.of(selectedColor));
            }
            case "baker" -> {
                controlsProf();
            }
            default -> {  //knight, faun and archer are used in other method (islandDomination *2 , moveMN)
                return null;}
        }
        card.setActive(false);
        return null;

    }

    /**
     * Returns currently active personality
     *
     * @return active Personality (if no active personality returns null)
     */
    private Personality getActivePersonality() {
        for (Personality person : listPersonality) {
            if (person.isActive())
                return person;
        }

        return null;
    }

    /**
     * Returns personality with given name
     *
     * @param name Name of personality
     * @return personality with selected name (null if not present in list)
     */
    public Personality getPersonalityWithName(String name) {
        for (Personality person : listPersonality) {
            if (person.getName().equals(name))
                return person;
        }

        return null;
    }

    /**
     * Returns personality with given name
     *
     * @param id island ID
     * @return index of island
     */
    public int getIslandPosition(int id) {
        Island island = islands.stream()
                .filter(isl -> id == (isl.getID()))
                .findAny()
                .orElse(null);
        if(island == null)
            return -1;
        else
            return islands.indexOf(island);
    }


    /**
     * Returns the player that matches the input ID.
     *
     * @param pid player ID
     */
    public Player getPlayerById(int pid){
        return listPlayers.stream() //finds player who is owner of card
                .filter(ply -> pid == (ply.getPlayerId()))
                .findAny()
                .orElse(null);
    }
}
