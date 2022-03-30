package it.polimi.ingsw.am54.model;

import java.util.*;

import static it.polimi.ingsw.am54.model.Constants.ISLANDS_AT_START_OF_GAME;

public class Game {
    private final int gameID;
    protected List<Player> listPlayers;
    private List<Island> islands;
    protected List<Professor> listProfessors;
    private List<Personality> listPersonality;
    private Bag bag;
    public  int winner = 0;
    private int numTurns = 0;
    private int MotherNature = 0;
    public boolean noTowers = false;

    public Game(int gameID,int numPlayers)
    {
        this.gameID = gameID;
        listPlayers = new ArrayList<>();
        islands = new ArrayList<>();
        listProfessors = new ArrayList<>();
        listPersonality = new ArrayList<>();
        startGame(numPlayers);
    }

    private void startGame(int numPlayers) {
        /* it creates a GameBoard for each player (depends on numPlayers). */
        for(int i = 1; i <= numPlayers; i++) {
            Player player = new Player(i);
            listPlayers.add(player);
        }

        /* it creates the empty islands */
        for (int i = 1; i <= ISLANDS_AT_START_OF_GAME; i++)
        {
            Island island = new Island(i,0);
            islands.add(island);
        }

        /* it creates the list of prof*/
        for (Color color:Color.values()) {
            Professor prof = new Professor(color, 0);
            listProfessors.add(prof);
        }
    }

    public void islandDomination(Island location){
        //TODO
    }
    public void controlsProf(){
        for(Color c: Color.values())
        {
            Professor prof = null;
            for(Professor p : listProfessors)
                if(p.getColor().equals(c))
                    prof = p;
                    //TODO maybe add a break;

            Player p = Collections.max(listPlayers, new Comparator<Player>() {
                @Override
                public int compare(Player p1, Player p2) {
                    if(p1.getGameBoard().getStudentsHall(c) > p2.getGameBoard().getStudentsHall(c))
                        return 1;
                    if(p1.getGameBoard().getStudentsHall(c) < p2.getGameBoard().getStudentsHall(c))
                        return -1;
                    if(p1.getGameBoard().getStudentsHall(c) == p2.getGameBoard().getStudentsHall(c)) {
                        Personality baker = getPersonalityWithName("baker");//Checks if there is card baker which has influence on deciding who controls prof
                        if( baker != null && baker.isActive() && baker.getOwner() == p1.getPlayerId())
                            return 1;
                        if( baker != null && baker.isActive() && baker.getOwner() == p2.getPlayerId())
                            return -1;
                        if(p1.getGameBoard().getProf().contains(c))
                            return 1;
                        if(p2.getGameBoard().getProf().contains(c))
                            return -1;
                    }
                    return 0;
                }
            });
            if(prof.getOwner() != p.getPlayerId() && p.getGameBoard().getStudentsHall(c) != 0)
                p.getGameBoard().setProf(prof);
        }
    }

    private void nextRound(){
        if (numTurns==0) {
            Collections.shuffle(listPlayers); /* in the first round the players' order is chosen randomly */
        }
        else
            /*the following instructions are followed if the game is not finished
            (the game cannot be finished already the first round, for this reason I did not put it before):*/
            if(winner==0)
            {
                //I sort the list of players in the order in which the players must play:
                Collections.sort(listPlayers, new Comparator <Player>() {
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

    private void plays() {
        //TODO
    }

    private void moveMN(int playerID){
        int maxMoves = listPlayers.get(playerID).getHand().getCardPlayed().getMaxMoves();

        //TODO check if getActivePersonality is null
        if(getActivePersonality().getName().equals("archer"))
        {
            maxMoves += 2;
        }

        int playerSelection = 0; // this value will arrive from player and should be between 1 and maxMoves
        //TODO playerSelection

        if(maxMoves < playerSelection) {
            //TODO communicate ERROR to player and ask for new input
        }
        else {

            MotherNature = (MotherNature + playerSelection) % ISLANDS_AT_START_OF_GAME; //Mother Nature's position is calculated in this way so that when MN goes over 12th island it lands at right position
            Island currentIsland = islands.get(MotherNature);

            if(currentIsland.getNoEntry())
            {
                currentIsland.setNoEntry(false);
                Containers botanist = (Containers) getPersonalityWithName("botanist");
                botanist.bringBackTile();
            } else{
                islandDomination(currentIsland);
            }

        }

    }

    private void moveStudents(int playerId, int where, Color student){
        //NOTE: Controller should get where and student from Player, and it should check validity of data
        Player p = listPlayers.get(playerId);
        if(where == 0)
        {
            p.getGameBoard().removeStudentsEnter(List.of(student));
            p.getGameBoard().addStudentHall(student);
        }else{
            p.getGameBoard().removeStudentsEnter(List.of(student));
            islands.get(where).addStudents(List.of(student));
        }

    }

    private void checkWinner(){ /* the ceckWinner method must be called at the end of each player's moves */
        //TODO

        /* if a player has run out of towers in his gameBoard, I name him the winner: */
        for (Player player: listPlayers)
        {
            if (player.getGameBoard().getTowers().isEmpty())
            {
                winner=player.getPlayerId();
                return;
            }
        }

        int cardFinished=0;
        for(Player player: listPlayers){
            List<Card> allCards = player.getHand().getAllCards();
            if (allCards.isEmpty())
                cardFinished++;
        }
        //if the hypotheses of the if are verified, the game is over and the winner will have to be calculated:
        if (this.islands.size()==3 || bag.isEmpty()==true || cardFinished>0)
        {
            //I sort the players in ascending order of "number of towers":
            Collections.sort(listPlayers, new Comparator <Player>() {
                /* I override the 'compare' method to be able to sort the elements of listPlayers
                in ascending order according to the numerical value of the card played by each player: */
                @Override
                public int compare(Player player1, Player player2) {
                   /*the numerical values of the cards played by the players are compared
                   and the result of the comparison is returned */
                   return player1.getGameBoard().getTowers().size() - player2.getGameBoard().getTowers().size();
                }
            }); //at this point listBoard has been sorted.
            
            int varTemp=0;
            List<Player> almostWinner = null; //is the list that will contain the players with the highest number of towers placed on the islands.
            almostWinner.add(listPlayers.get(varTemp));

            /* I put in the list of winners all the players with the highest number of towers positioned on islands
            (which will be the first on the list we ordered before): */
            while (almostWinner.get(varTemp).getGameBoard().getTowers().size()== listPlayers.get(varTemp++).getGameBoard().getTowers().size()){
                almostWinner.add(listPlayers.get(varTemp++));
            }

            //at this point two cases can occur:
            if(almostWinner.size()==1){ //there is a player who has placed the most towers of all: NET WIN.
                winner= almostWinner.get(0).getPlayerId();
            }
            else { //DRAW. among the players who tied, I find the one with the highest number of profs controlled:
                Player playerWinner =almostWinner.get(0);
                for (Player playerTemp: almostWinner) {
                    if(playerTemp.getGameBoard().getProf().size()>playerWinner.getGameBoard().getProf().size())
                        playerWinner=playerTemp;
                }
                winner=playerWinner.getPlayerId(); //I declare the player with the highest number of controlled profs the winner.
            }
        }
    }

    private void buyPersonality(int owner, Personality personality){
        //TODO
    }

    public Map<Integer, List<Color>> getClouds() {
        Map<Integer, List<Color>> clouds = new HashMap<>();
        //TODO
        return clouds;
    }

    private void uniteIslands(){
        //TODO
    }

    public int getGameID() {
        return gameID;
    }

    public List<Personality> getPersonality() {
        return List.copyOf(listPersonality);
    }

    public List<Professor> getProfessors() {
        return List.copyOf(listProfessors);
    }

    private void usePersonalityPower(Personality card){
        //TODO
    }

    private Personality getActivePersonality()
    {
        for (Personality person: listPersonality) {
            if(person.isActive())
                return person;
        }

        return null;
    }

    private  Personality getPersonalityWithName(String name){
        for (Personality person: listPersonality) {
            if(person.getName().equals(name))
                return person;
        }

        return null;
    }
}
