package it.polimi.ingsw.am54.model;

import java.util.*;

import static it.polimi.ingsw.am54.model.Constants.ISLANDS_AT_START_OF_GAME;

/**
 * Main class that controls game flow and its principal logic
 */
public class Game {
    private final int gameID;
    /**
     * List of all players
     */
    protected List<Player> listPlayers;
    /**
     * List of available islands
     */
    protected List<Island> islands;
    /**
     * List of available professors
     */
    protected List<Professor> listProfessors;
    /**
     * List containing personalities in current game (three)
     */
    protected List<Personality> listPersonality;
    private Bag bag;
    /**
     * Winner will be id of player who won the game. <br>
     * While there is no winner, the value is 0
     */
    public int winner = 0;
    /**
     * Number of turns played
     */
    protected int numTurns = 0;
    private int MotherNature = 0;
    /**
     * Property used by islandDomination in case that towers' influence doesn't count
     */
    public boolean noTowers = false;

    /**
     * Constructs game and initializes attributes
     * @param gameID unique game identifier
     * @param numPlayers number of players for current game
     */
    public Game(int gameID, int numPlayers) {
        this.gameID = gameID;
        listPlayers = new ArrayList<>();
        islands = new ArrayList<>();
        listProfessors = new ArrayList<>();
        listPersonality = new ArrayList<>();
        startGame(numPlayers);
    }

    /**
     * Creates instances of objects necessary for game and fills attributes with initial values
     * @param numPlayers number of players
     */
    private void startGame(int numPlayers) {
        /* it creates a GameBoard for each player (depends on numPlayers). */
        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player(i);
            listPlayers.add(player);
        }

        /* it creates the empty islands */
        for (int i = 1; i <= ISLANDS_AT_START_OF_GAME; i++) {
            Island island = new Island(i, 0);
            islands.add(island);
        }

        /* it creates the list of prof*/
        for (Color color : Color.values()) {
            Professor prof = new Professor(color, 0);
            listProfessors.add(prof);
        }

        bag = new Bag(); //creates instance of Bag
    }

    /**
     * Calculates influence of every player on selected island and determines its owner
     * @param location selected island
     */
    public void islandDomination(Island location) {
    }

    /**
     * Checks which player controls which professor. <br>
     * Iterates through all professors
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
                    if (p1.getGameBoard().getProf().contains(c))
                        return -1;
                    if (p2.getGameBoard().getProf().contains(c))
                        return 1;
                }
                return 0;
            });

            Player p = null;

            for (int i = 0; i < listPlayers.size(); i++) {
                if (listPlayers.get(i).getPlayerId() == tmpList.get(0).getPlayerId()) {
                    p = listPlayers.get(i);
                }
            }

            if (prof.getOwner() != p.getPlayerId() && p.getGameBoard().getStudentsHall(c) > 0) {// ensures that professor are not added more the once to player
                if (p.getGameBoard().getStudentsHall(prof.getColor()) == tmpList.get(1).getGameBoard().getStudentsHall(prof.getColor())) {
                    // these conditions ensure that if two players have same number of students of selected color, professor is assigned to one of them only if there is active personality card Baker
                    if (getPersonalityWithName("baker") != null && getPersonalityWithName("baker").isActive() && getPersonalityWithName("baker").getOwner() == p.getPlayerId())
                        p.getGameBoard().addProf(prof);
                } else {
                    p.getGameBoard().addProf(prof);
                }
            }

        }
    }

    /**
     * Determines order in which players will play next round.
     */
    protected void nextRound() {
        if (numTurns == 0) {
            Collections.shuffle(listPlayers); /* in the first round the players' order is chosen randomly */
        } else
            /*the following instructions are followed if the game is not finished
            (the game cannot be finished already the first round, for this reason I did not put it before):*/
            if (winner == 0) {
                //I sort the list of players in the order in which the players must play:
                listPlayers.sort(new Comparator<Player>() {
                    /* I override the 'compare' method to be able to sort the elements of listPlayers
                    in ascending order according to the numerical value of the card played by each player: */
                    @Override
                    public int compare(Player player1, Player player2) {

                       /*the numerical values of the cards played by the players are
                       compared and the result of the comparison is returned */
                        return player1.getHand().getCardPlayed().getValue() - player2.getHand().getCardPlayed().getValue();
                    }
                }); //at this point listBoard has been sorted.
            }
        numTurns++; //whatever has happened, the turn number will need to be increased.
    }

    /**
     * Handles allowed actions that player can make during his/hers move
     */
    private void plays() {
        //TODO
    }

    /**
     * Handles movement of Mother Nature
     * @param playerID player whose turn is to play
     */
    private void moveMN(int playerID) {
        int maxMoves = listPlayers.get(playerID).getHand().getCardPlayed().getMaxMoves();


        if (getActivePersonality() != null && getActivePersonality().getName().equals("archer")) {
            maxMoves += 2;
        }

        int playerSelection = 0; // this value will arrive from player and should be between 1 and maxMoves
        //TODO playerSelection

        if (maxMoves < playerSelection) {
            //TODO communicate ERROR to player and ask for new input
        } else {

            MotherNature = (MotherNature + playerSelection) % ISLANDS_AT_START_OF_GAME; //Mother Nature's position is calculated in this way so that when MN goes over 12th island it lands at right position
            Island currentIsland = islands.get(MotherNature);

            if (currentIsland.getNoEntry()) {
                currentIsland.setNoEntry(false);
                Containers botanist = (Containers) getPersonalityWithName("botanist");
                botanist.bringBackTile();
            } else {
                islandDomination(currentIsland);
            }

        }

    }

    /**
     * Allows moving students from Entrance to Hall or Island
     * @param playerId player who moves students
     * @param where if 0 moves to hall, else moves to island with that ID
     * @param student Color of student to be moved
     * @throws RuntimeException if selected student is not in player's entrance
     */
    protected void moveStudents(int playerId, int where, Color student) {
        Player p = listPlayers.get(playerId - 1);

        if (!p.getGameBoard().getStudentsEnter().contains(student)) {
            throw new RuntimeException("Selected student is not in player's entrance"); // this may be changed to other exception later
        }

        if (where == 0) {
            p.getGameBoard().removeStudentsEnter(List.of(student));
            p.getGameBoard().addStudentHall(student);
        }else {
            p.getGameBoard().removeStudentsEnter(List.of(student));
            islands.get(where).addStudents(List.of(student));
        }

    }

    /**
     * Checks if any of win conditions are satisfied. <br>
     * If there is winner changes attribute winner to playerID (of winner)
     */
    private void checkWinner() { /* the ceckWinner method must be called at the end of each player's moves */
        //TODO

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
        if (this.islands.size() == 3 || bag.isEmpty() == true || cardFinished > 0) {
            //I sort the players in ascending order of "number of towers":
            Collections.sort(listPlayers, new Comparator<Player>() {
                /* I override the 'compare' method to be able to sort the elements of listPlayers
                in ascending order according to the numerical value of the card played by each player: */
                @Override
                public int compare(Player player1, Player player2) {
                   /*the numerical values of the cards played by the players are compared
                   and the result of the comparison is returned */
                    return player1.getGameBoard().getTowers().size() - player2.getGameBoard().getTowers().size();
                }
            }); //at this point listBoard has been sorted.

            int varTemp = 0;
            List<Player> almostWinner = null; //is the list that will contain the players with the highest number of towers placed on the islands.
            almostWinner.add(listPlayers.get(varTemp));

            /* I put in the list of winners all the players with the highest number of towers positioned on islands
            (which will be the first on the list we ordered before): */
            while (almostWinner.get(varTemp).getGameBoard().getTowers().size() == listPlayers.get(varTemp++).getGameBoard().getTowers().size()) {
                almostWinner.add(listPlayers.get(varTemp++));
            }

            //at this point two cases can occur:
            if (almostWinner.size() == 1) { //there is a player who has placed the most towers of all: NET WIN.
                winner = almostWinner.get(0).getPlayerId();
            } else { //DRAW. among the players who tied, I find the one with the highest number of profs controlled:
                Player playerWinner = almostWinner.get(0);
                for (Player playerTemp : almostWinner) {
                    if (playerTemp.getGameBoard().getProf().size() > playerWinner.getGameBoard().getProf().size())
                        playerWinner = playerTemp;
                }
                winner = playerWinner.getPlayerId(); //I declare the player with the highest number of controlled profs the winner.
            }
        }
    }

    /**
     *  Checks if player has enough money and if so changes owner of selected personality.
     * @param playerID player who wants to buy personality
     * @param personality selected personality
     * @throws RuntimeException if selected personality is not among available
     * @throws RuntimeException if player doesn't exist
     * @throws RuntimeException if player doesn't have enough money to buy personality
     */
    protected void buyPersonality(int playerID, Personality personality) {
        Player player = listPlayers.stream()
                .filter(ply -> playerID == (ply.getPlayerId()))
                .findAny()
                .orElse(null);

        if(getPersonalityWithName(personality.getName()) == null)
            throw new RuntimeException("Non existing personality");

        if(player == null)
            throw  new RuntimeException("Non existing player");

        if(listPlayers.get(playerID-1).getGameBoard().getCoins() < personality.getCost())
            throw new RuntimeException("Not enough money");

        player.getGameBoard().spendCoins(personality.getCost()); //decreases amount of coins that player has

        personality.setOwner(playerID);//changes card owner
        personality.increaseCost();//increases cost for future uses of Personality card
        //personality.setActive(ture);
        //usePersonalityPower(personality); in case that personality is used immediately after being bought
    }

    /**
     * Generates clouds accordingly with number of players.<br>
     * For each player cloud is created and filed with 3 or 4 students extracted from the Bag
     * @return Map that represents all clouds and students on them
     */
    public Map<Integer, List<Color>> getClouds() {
        Map<Integer, List<Color>> clouds = new HashMap<>();
        int numStudentsOnCloud, numPlayers = listPlayers.size();

        if (numPlayers == 3)
            numStudentsOnCloud = 4;
        else
            numStudentsOnCloud = 3;

        for(int i = 0; i < numPlayers; i++) //for each player creates a cloud
        {
            List<Color> studentsOnCloud = new ArrayList<>();
            for(int j = 0; j < numStudentsOnCloud; j++) //adds appropriate number of students from bag to cloud
                studentsOnCloud.add(bag.getNextStudent());
            clouds.put(i,studentsOnCloud);
        }

        return clouds;
    }

    /**
     * Checks if selected island and one or both adjacent island have same owner, if true they are unified<br>
     * All students and towers are added to selected island, while others are deleted from list of islands
     * @param islandId selected island
     * @throws RuntimeException if island is not on the list of available islands
     */
    protected void uniteIslands(int islandId) {
        int current = -1;
        for(Island is : islands){
            if(is.getID() == islandId)
                current = islands.indexOf(is);
        }

        if(current == -1)
            throw new RuntimeException("Island with id = "+ islandId+" is not in the list");

        if(current == 0){ //special case of first island in the list
            if(islands.get(current).getOwner() == islands.get(islands.size()-1).getOwner()){
                unifier(current,islands.size()-1);
            }
            if(islands.get(current).getOwner() == islands.get(current+1).getOwner()){
                unifier(current,current+1);
            }

        } else if(current == islands.size()-1) { //special case of last island in the list
            if(islands.get(current).getOwner() == islands.get(0).getOwner()){
                unifier(current,0);
                current--;
            }
            if(islands.get(current).getOwner() == islands.get(current-1).getOwner()){
                unifier(current,current-1);
            }

        }else{ //general case
            if(islands.get(current).getOwner() == islands.get(current+1).getOwner()){
                unifier(current,current+1);
            }
            if(islands.get(current).getOwner() == islands.get(current-1).getOwner()){
                unifier(current,current-1);
            }
        }
    }

    private void unifier(int isl1, int isl2)//helper method to uniteIslands
    {
        islands.get(isl1).addStudents(islands.get(isl2).getStudents());
        islands.get(isl1).addTower();
        islands.remove(isl2);
    }

    /**
     * Returns identifier of current game
     * @return gameID
     */
    public int getGameID() {
        return gameID;
    }

    /**
     * Returns all available personalities in current game (three personalities)
     * @return copy of personality list
     */
    public List<Personality> getPersonality() {
        return List.copyOf(listPersonality);
    }

    /**
     * Return all available professors
     * @return copy of lsit of professors
     */
    public List<Professor> getProfessors() {
        return List.copyOf(listProfessors);
    }

    /**
     * Handles effects of activated card. <br>
     * Effects are determined based on card name
     * @param card selected personality card
     */
    private void usePersonalityPower(Personality card) {
        //TODO
    }

    /**
     * Returns currently active personality
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
     * @param name Name of personality
     * @return personality with selected name (null if not present in list)
     */
    private Personality getPersonalityWithName(String name) {
        for (Personality person : listPersonality) {
            if (person.getName().equals(name))
                return person;
        }

        return null;
    }
}
