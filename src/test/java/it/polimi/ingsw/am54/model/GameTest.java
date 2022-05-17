package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    /**
     * checks that islandDomination works properly when faun is applied
     * @see Game#islandDomination(Island)
     */
    @Test
    public void islandDominationPersonalityTest(){
        Game game = new Game(1, 2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Personality faun = PersonalityFactory.generate("faun");
        game.listPersonality.add(faun);
        faun.setActive(true);
        Player p1 = game.getPlayerById(1);


        //adding professors
        //add 2 Blue, 1 Green to player 1

        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.GREEN);

        //add 1 red, 1 blue, 1 green to player 2
        Player p2 = game.getPlayerById(2);
        p2.getGameBoard().addStudentHall(Color.RED);
        p2.getGameBoard().addStudentHall(Color.BLUE);
        p2.getGameBoard().addStudentHall(Color.GREEN);
        //this should set the blue professor to player 1 and the red professor to player 2
        game.controlsProf();
        //adding color to islands
        List<Island> islands = game.islands;
        islands.get(1).addStudents(List.of(Color.BLUE, Color.RED, Color.RED));
        islands.get(2).addStudents(List.of(Color.BLUE, Color.BLUE, Color.GREEN));
        islands.get(3).addStudents(List.of(Color.GREEN, Color.GREEN));

        //!!this is for faun
        //add towers
        islands.get(1).addTowers(List.of(p1.getGameBoard().getTowers().get(0)));

        //calculating domination and setting towers
        game.islandDomination(islands.get(1));
        game.islandDomination(islands.get(2));
        game.islandDomination(islands.get(3));

        //checking owners
        assertEquals(islands.get(1).getOwner(), 2);
        assertEquals(islands.get(2).getOwner(), 1);
        assertEquals(islands.get(3).getOwner(), 0);
        assertFalse(faun.isActive());
    }

    /**
     * checks that islandDomination works properly when knight is applied
     * @see Game#islandDomination(Island)
     */
    @Test
    public void islandDominationPersonalityTest2(){
        Game game = new Game(1, 2);
        Player ply = game.listPlayers.get(0);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Personality knight = PersonalityFactory.generate("knight");

        //setting owner of card knight as player 1 (only player with influence because no students were moved)
        game.listPersonality.add(knight);
        knight.setOwner(ply.getPlayerId());
        knight.setActive(true);




        //calculating domination and setting towers
        game.islandDomination(game.islands.get(1));
        game.islandDomination(game.islands.get(2));


        //checking owners
        assertEquals(ply.getPlayerId(),game.islands.get(1).getOwner());
        assertEquals(0,game.islands.get(2).getOwner());
        assertFalse(knight.isActive());
    }

    /**
     * checks that islandDomination works properly when faun is applied
     * @see Game#islandDomination(Island)
     */
    @Test
    public void islandDominationPersonalityTest3(){
        Game game = new Game(1, 2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Modifier glutton = (Modifier) PersonalityFactory.generate("glutton");
        game.listPersonality.add(glutton);
        glutton.setActive(true);
        glutton.setNoColor(Color.RED);

        //adding professors
        //add 2 Blue, 1 Green to player 1
        Player p1 = game.getPlayerById(1);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.GREEN);
        //add 1 red, 1 blue, 1 green to player 2
        Player p2 = game.getPlayerById(2);
        p2.getGameBoard().addStudentHall(Color.RED);
        p2.getGameBoard().addStudentHall(Color.BLUE);
        p2.getGameBoard().addStudentHall(Color.GREEN);
        //this should set the blue professor to player 1 and the red professor to player 2
        game.controlsProf();
        //adding color to islands
        List<Island> islands = game.islands;
        islands.get(1).addStudents(List.of(Color.BLUE, Color.RED, Color.RED));
        islands.get(2).addStudents(List.of(Color.BLUE, Color.BLUE, Color.GREEN));
        islands.get(3).addStudents(List.of(Color.GREEN, Color.GREEN));
        islands.get(4).addStudents(List.of(Color.RED, Color.RED, Color.RED));


        //calculating domination and setting towers
        game.islandDomination(islands.get(1));
        assertFalse(glutton.isActive());
        game.islandDomination(islands.get(2));
        game.islandDomination(islands.get(3));
        game.islandDomination(islands.get(4));

        //checking owners
        assertEquals(1, islands.get(1).getOwner());
        assertEquals(1, islands.get(2).getOwner());
        assertEquals(0, islands.get(3).getOwner());
        assertEquals(2, islands.get(4).getOwner());
        assertFalse(glutton.isActive());
    }

    /**
     * checks that islandDomination works properly when no Personality is active
     * (contronlsProf is also used)
     * @see Game#islandDomination(Island)
     * @see Game#controlsProf()
     */
    /*@Test
    public void islandDominationNoPersonalityTest(){
        Game game = new Game(1, 2);
        game.listPersonality = new ArrayList<>(); //puts empty list

        //adding professors
        //add 2 Blue, 1 Green to player 1
        Player p1 = game.getPlayerById(1);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        p1.getGameBoard().addStudentHall(Color.GREEN);
        //add 1 red, 1 blue, 1 green to player 2
        Player p2 = game.getPlayerById(2);
        p2.getGameBoard().addStudentHall(Color.RED);
        p2.getGameBoard().addStudentHall(Color.BLUE);
        p2.getGameBoard().addStudentHall(Color.GREEN);
        //this should set the blue professor to player 1 and the red professor to player 2
        game.controlsProf();

        //adding color to islands
        List<Island> islands = game.islands;
        islands.get(1).addStudents(List.of(Color.BLUE, Color.RED, Color.RED));
        islands.get(2).addStudents(List.of(Color.BLUE, Color.BLUE, Color.GREEN));
        islands.get(3).addStudents(List.of(Color.GREEN, Color.GREEN));

        //calculating domination
        game.islandDomination(islands.get(1));
        game.islandDomination(islands.get(2));
        game.islandDomination(islands.get(3));

        //checking owners
        assertEquals(islands.get(1).getOwner(), 2);
        assertEquals(islands.get(2).getOwner(), 1);
        assertEquals(islands.get(3).getOwner(), 0);

        //checking towers
        assertEquals(islands.get(1).getTowers().get(0), p2.getGameBoard().getTowers().get(0));
        assertEquals(islands.get(2).getTowers().get(0), p1.getGameBoard().getTowers().get(1));
        assertEquals(islands.get(3).getTowers().size(), 0);

    }*/

    /**
     * checks that the method controlsProf calculates and sets the owner of
     * the corresponding professors correctly
     * @see Game#controlsProf()
     * @see GameBoard#addStudentHall(Color)
     */
    @Test
    public void controlsProfTest1() {
        //in this test every player has the largest number of students of one color
        Game game = new Game(1, 3);
        int[] expected = {1, 2, 3};
        List<Player> listPlayers = game.listPlayers;
        for (int i = 0; i < 5; i++) {
            listPlayers.get(0).getGameBoard().addStudentHall(Color.BLUE); // we have value - 1 because indexes of list start at 0 while player's id start at 1
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 3; i++) {
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.BLUE);
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(0).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 2; i++) {
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.BLUE);
            listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }

        game.controlsProf();
        for (Professor p : game.listProfessors) {
            //expected values are increased
            if (p.getColor().equals(Color.BLUE))
                assertEquals(expected[0], p.getOwner());
            if (p.getColor().equals(Color.RED))
                assertEquals(expected[1], p.getOwner());
            if (p.getColor().equals(Color.YELLOW))
                assertEquals(expected[2], p.getOwner());
        }
    }

    /**
     * checks that controlsProf doesn't change the owner of the professors when
     * no one has students in the halls
     * @see Game#controlsProf()
     */
    @Test
    public void controlsProfTest2(){
        //all players have no students in hall
        int expected =0; // it is expected that all profs will have no owner
        Game game = new Game(1,4);
        game.controlsProf();

        for(Professor p: game.listProfessors){
            assertEquals(expected,p.getOwner());
        }
    }

    /**
     * checks that controlsProf doesn't change the owner of professors if no personality is
     * active and no one controls any professor and the players have the same number
     * of students in the hall
     * @see Game#controlsProf()
     */
    @Test
    public void controlsProfTest3(){
        //prof has no initial owner and both players have same number of students (no Personality active)
        Game game = new Game(1, 2);
        int expected = 0; //it is expected that prof will not be assigned to anyone
        game.listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        game.listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);

        game.controlsProf();
        for (Professor p : game.listProfessors) {
            //expected values are increased

            if (p.getColor().equals(Color.RED))
                assertEquals(expected, p.getOwner());

        }
    }

    /**
     * checks that controlsProf doesn't change the owner of professors if no personality is
     * active  and the players have the same number of students in the hall
     * @see Game#controlsProf()
     */
    @Test
    public void controlsProfTest4(){
        //prof has  initial owner and both players have same number of students (no Personality active)
        Game game = new Game(1, 2);
        List<Player> listPlayers = game.listPlayers;
        listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        game.controlsProf();
        int expected = 1; //it is expected that prof will not change owner

        for (Professor p : game.listProfessors) {
            //expected values are increased

            if (p.getColor().equals(Color.RED))
                assertEquals(expected, p.getOwner());

        }

        listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);
        listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);

        game.controlsProf();
        for (Professor p : game.listProfessors) {
            //expected values are increased

            if (p.getColor().equals(Color.RED))
                assertEquals(expected, p.getOwner());

        }
    }

    /**
     * checks that controlsProf behaves correctly when Personality botanist is active
     * @see Game#controlsProf()
     * @see Personality#setActive(boolean)
     * @see Personality#setOwner(int)
     */
    @Test
    public void controlsProfTest5(){
        //both players have same number of students but Personality botanist is active
        Game game = new Game(1, 2);
        int expected = 1; // activated Personality baker

        Modifier botanist = new Modifier("baker");
        List<Personality> listPersonality = game.listPersonality;
        listPersonality.add(0,botanist);
        listPersonality.get(0).setActive(true);
        listPersonality.get(0).setOwner(1);

        game.listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        game.listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);

        game.controlsProf();
        for (Professor p : game.listProfessors) {
            //expected values are increased

            if (p.getColor().equals(Color.RED))
                assertEquals(expected, p.getOwner());

        }
    }

    /**
     * check that controlsProf doesn't change the order or the Players inside listPlayers
     * @see Game#controlsProf()
     * @see Game#listPlayers
     */
    @Test
    public void controlsProfTest6(){

        Game game = new Game(1, 3);
        int[] expected = {1, 2, 3};
        List<Player> listPlayers = game.listPlayers;
        for (int i = 0; i < 5; i++) {
            listPlayers.get(0).getGameBoard().addStudentHall(Color.BLUE); // we have value - 1 because indexes of list start at 0 while player's id start at 1
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 3; i++) {
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.BLUE);
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(0).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 2; i++) {
            listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.BLUE);
            listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
            listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }


        List<Player> old = new ArrayList<>(listPlayers);

        game.controlsProf();

        assertEquals(old, listPlayers); // test checks if listPlayers has been changed by controlsProf

    }

    /**
     * Assures that exception is thrown in case that player selects negative number of moves
     * @see Game#moveMN(int, int)
     */
    @Test
    public void MoveMNTestException1(){
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            Player p = game.listPlayers.get(0);
            p.getHand().setCardPlayed(p.getHand().getAllCards().get(0));
            game.moveMN(p.getPlayerId(), -1);

        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid selection", actualMessage);
    }

    /**
     * Assures that exception is thrown in case that player selects more than maximal number of moves
     * @see Game#moveMN(int, int)
     */
    @Test
    public void MoveMNTestException2(){
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            Player p = game.listPlayers.get(0);
            p.getHand().setCardPlayed(p.getHand().getAllCards().get(0));
            game.moveMN(p.getPlayerId(), 3);

        });
        String actualMessage = exception.getMessage();
        assertEquals("Invalid selection", actualMessage);
    }

    /**
     * Assures that MoveMN moves Mother Nature to correct island
     * @see Game#moveMN(int, int)
     */
    @Test
    public void MoveMNTest1(){
        int moves = 3, old= 11;
        Game game = new Game(1,2);
        game.MotherNature = old;
        Player p = game.listPlayers.get(0);
        p.getHand().setCardPlayed(p.getHand().getAllCards().get(5));
        game.moveMN(p.getPlayerId(), moves);
        assertEquals((old+moves) % Constants.ISLANDS_AT_START_OF_GAME, game.MotherNature);
    }

    /**
     * Checks that if mother nature lands on island with a noEntry tile, the tile is removed
     * @see Game#moveMN(int, int)
     * @see Containers#bringBackTile()
     * @see Island#getNoEntry()
     */
    @Test
    public void MoveMNTest2(){
        int expected = 3;
        Game game = new Game(1,2);
        Containers botanist = (Containers) game.getPersonalityWithName("botanist");
        if( botanist == null) {
            botanist = new Containers("botanist");
            game.listPersonality.add(botanist);
        }
        botanist.useTile();
        game.islands.get(3).setNoEntry(true);
        Player p = game.listPlayers.get(0);
        p.getHand().setCardPlayed(p.getHand().getAllCards().get(5));
        game.moveMN(p.getPlayerId(), expected);
        assertEquals(expected, game.MotherNature);
        assertFalse(game.islands.get(3).getNoEntry());
    }

    /**
     * checks that moveStudents correctly moves students from the entrance of a player
     * GameBoard to the hall of the same player GameBoard
     * @see Game#moveStudents(int, int, Color)
     * @see GameBoard#getStudentsHall(Color)
     * @see GameBoard#getStudentsEnter()
     */
    @Test
    public void moveStudentTest1(){
        //Player wants to move student from Entrance to Hall
        Game game = new Game(1,2);
        Player p = game.listPlayers.get(0);


        for(int i = 0; i < 3; i ++)
            p.getGameBoard().addStudentsEnter(List.of(Color.BLUE));

        int hallStudents = p.getGameBoard().getStudentsHall(Color.BLUE);
        List<Color> entranceStudents = p.getGameBoard().getStudentsEnter();

        game.moveStudents(p.getPlayerId(),0,Color.BLUE);

        assertEquals(hallStudents + 1, p.getGameBoard().getStudentsHall(Color.BLUE));
        assertEquals(entranceStudents.size() - 1, p.getGameBoard().getStudentsEnter().size());
        assertTrue(entranceStudents.containsAll(p.getGameBoard().getStudentsEnter()));

        for(Color c: Color.values())
        {
            if(!c.equals(Color.BLUE))
                assertEquals(0,p.getGameBoard().getStudentsHall(c));
        }
    }

    /**
     * checks that movesStudents moves students correctly from the Entrance of a Player
     * GameBoard to a certain Island
     * @see Game#moveStudents(int, int, Color)
     * @see GameBoard#getStudentsEnter()
     * @see GameBoard#addStudentsEnter(List)
     */
    @Test
    public void moveStudentTest2(){
        //Player wants to move student from Entrance to Island
        Game game = new Game(1,2);
        Player p = game.listPlayers.get(0);
        int islandInd = 2;
        int pos = game.getIslandPosition(islandInd);
        for(int i = 0; i < 4; i++) {
            p.getGameBoard().addStudentsEnter(List.of(Color.YELLOW));
            game.islands.get(pos).addStudents(List.of(Color.BLUE));
        }

        List<Color> studentsEnter = p.getGameBoard().getStudentsEnter();
        List<Color> studentsIsland = game.islands.get(pos).getStudents();

        game.moveStudents(p.getPlayerId(), islandInd, Color.YELLOW);

        assertEquals(studentsEnter.size()-1, p.getGameBoard().getStudentsEnter().size());
        assertTrue(studentsEnter.containsAll(p.getGameBoard().getStudentsEnter()));

        assertEquals(studentsIsland.size()+1, game.islands.get(pos).getStudents().size());
        assertTrue(game.islands.get(pos).getStudents().containsAll(studentsIsland));
        assertTrue(game.islands.get(pos).getStudents().contains(Color.YELLOW));

    }

    /**
     * checks if movesStudents throws and exception if the student in the parameter student
     * isn't contained in the corresponding player GameBoard entrance
     * @see Game#moveStudents(int, int, Color)
     */


    /**
     * I check that the checkWinner method calculates the winner in the case of a clear win
     */

    @Test
    public void checkWinnerTest1(){
        //we want to find the winner
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>();
        Player p = game.listPlayers.get(0);
        List<Card> cardList = p.getHand().getAllCards(); //list that contains all the player's cards

        /*I give a red student to Player p to generate an advantage over the other
         player and make him possess the red professor */
        p.getGameBoard().addStudentHall(Color.RED);
        game.controlsProf();

        /*the following actions are used to give Player p the dominance of an island*/
        List<Color> listColor = new ArrayList<>(); //I create a "container list" to pass at the entrance to the gameboard of p
        listColor.add(Color.RED); //I add a red player to that list
        p.getGameBoard().addStudentsEnter(listColor); //I add the red player in the hall of p
        game.moveStudents(p.getPlayerId(),1,Color.RED ); //I move the red player to the island 2
        game.islandDomination(game.islands.get(game.getIslandPosition(1)));//I calculate the dominance on the same island

        //I remove all cards from a player's deck to create the winner calculation conditions
        for (Card card : cardList)
        {
            p.getHand().removeFromCards(card);
        }
        //I calculate the winner, who must therefore be Player p.
        game.checkWinner();
        assertEquals( p.getPlayerId() ,game.winner );
    }

    /**
     * I check that the checkWinner method calculates the winner in the event of a tie win
     */

    @Test
    public void checkWinnerTest2(){
        //we want to find the winner
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>();
        Player p1 = game.listPlayers.get(0);
        Player p2 = game.listPlayers.get(1);
        List<Card> cardList = p1.getHand().getAllCards(); // list that contains all the cards of player p1

        /*I give a red and a blue student like Player p1 to generate an advantage over the other
         player and let him possess the red and blue professor*/
        p1.getGameBoard().addStudentHall(Color.RED);
        p1.getGameBoard().addStudentHall(Color.BLUE);
        game.controlsProf();

        /*I give a yellow student to Player p2 to make him possess the yellow professor*/
        p2.getGameBoard().addStudentHall(Color.YELLOW);
        game.controlsProf();

        /*the following actions are used to make each player have the dominance of an island*/
        //island 1 dominance for p1
        List<Color> listColor1 = new ArrayList<>(); //I create a "container list" to pass to the entry of the gameboard of p1
        listColor1.add(Color.RED); // I add a red player to that list
        p1.getGameBoard().addStudentsEnter(listColor1); //I add the red player in the hall of p1
        game.moveStudents(p1.getPlayerId(),1,Color.RED ); //I move the red player to island 1
        game.islandDomination(game.islands.get(game.getIslandPosition(1)));//I calculate the dominance on the same island

        //dominance island 2 for p2
        List<Color> listColor2 = new ArrayList<>(); //I create a "container list" to pass at the entrance of the p2 gameboard
        listColor2.add(Color.YELLOW); // I add a yellow player to that list
        p2.getGameBoard().addStudentsEnter(listColor2); //I add the yellow player in the hall of p2
        game.moveStudents(p2.getPlayerId(),2,Color.YELLOW ); //I move the yellow player to the island 2
        game.islandDomination(game.islands.get(game.getIslandPosition(2)));//I calculate the dominance on the same island




        //I remove all cards from a player's deck to create the winner calculation conditions
        for (Card card : cardList)
        {
            p1.getHand().removeFromCards(card);
        }

        //I calculate the winner, who must therefore be Player p1 since he is in control of several professors.
        game.checkWinner();
        assertEquals( p1.getPlayerId() ,game.winner );
    }

    /**
     * checks if the method getClouds return clouds with the right number of students if
     * there are 3 players
     * @see Game#getClouds()
     */
    @Test
    public void getCloudTest1(){
        int numPlayers = 3; // case of three players game
        int expectedStudents = 4;
        Game game = new Game(1,numPlayers);

        Map<Integer, List<Color>> clouds = new HashMap<>(game.getClouds());

        assertEquals(numPlayers, clouds.size());// number of cloud and players should be same

        for(int i = 0; i < numPlayers; i++)
        {
            assertEquals(expectedStudents, clouds.get(i).size());
            assertEquals(ArrayList.class, clouds.get(i).getClass());
        }
    }

    /**
     * checks if the method getClouds return clouds with the right number of students if
     * there are 2 players
     * @see Game#getClouds()
     */
    @Test
    public void getCloudTest2(){
        int numPlayers = 2; // case of two(or four) players game
        int expectedStudents = 3;
        Game game = new Game(1,numPlayers);

        Map<Integer, List<Color>> clouds = new HashMap<>(game.getClouds());

        assertEquals(numPlayers, clouds.size());// number of cloud and players should be same

        for(int i = 0; i < numPlayers; i++)
        {
            assertEquals(expectedStudents, clouds.get(i).size());
            assertEquals(ArrayList.class, clouds.get(i).getClass());
        }
    }

    /**
     * checks if the method uniteIslands unites islands correctly if last island and first island are
     * involved. Also checks if the initial number of islands is always 12
     * @see Game#uniteIslands(int)
     */
    @Test
    public void uniteIslandsTest(){
        //test assures that number and color of student is good after unification and enters first if
        Game game = new Game(1,2);
        List<Island> islands = game.islands;
        assertEquals(12, islands.size());//initial number of islands
        List<Color> students = new ArrayList<>(List.of(Color.RED,Color.RED,Color.RED));
        islands.get(11).addStudents(students);
        islands.get(1).addStudents(students);
        islands.get(0).addStudents(students);
        islands.get(11).setOwner(1);
        islands.get(1).setOwner(1);
        islands.get(0).setOwner(1);
        game.uniteIslands(1);
        assertEquals(10, islands.size());
        assertEquals(9, islands.get(0).getStudents().size());

        for(Color stud : islands.get(0).getStudents())
        {
            assertEquals(Color.RED,stud);
        }
    }

    /**
     * checks if the method uniteIslands unites islands correctly if there are 3 islands to unite.
     * @see Game#uniteIslands(int)
     */
    @Test
    public void uniteIslandsTest2(){
        //test enters else if
        Game game = new Game(1,2);
        List<Island> islands = game.islands;
        assertEquals(12, islands.size());//initial number of islands

        islands.get(10).setOwner(1);
        islands.get(0).setOwner(1);
        islands.get(11).setOwner(1);

        game.uniteIslands(islands.get(11).getID());

        assertEquals(10, islands.size());
    }

    /**
     * checks if the method uniteIslands unites islands correctly if there are 3 random island to
     * unite.
     * @see Game#uniteIslands(int)
     */
    @Test
    public void uniteIslandsTest3(){
        //test enters else
        Game game = new Game(1,2);
        List<Island> islands = game.islands;
        assertEquals(12, islands.size());//initial number of islands


        islands.get(5).setOwner(1);
        islands.get(4).setOwner(1);
        islands.get(6).setOwner(1);

        game.uniteIslands(islands.get(5).getID());

        assertEquals(10, islands.size());

    }

    /**
     * checks that the method uniteIslands works as expected when we do two consecutive
     * unifications
     * @see Game#uniteIslands(int)
     */
    @Test
    public void uniteIslandsTest4(){
        //test with multiple united islands
        Game game = new Game(1,2);
        List<Island> islands = game.islands;
        assertEquals(12, islands.size());//initial number of islands


        islands.get(5).setOwner(1);
        islands.get(4).setOwner(1);
        islands.get(6).setOwner(1);
        game.uniteIslands(islands.get(5).getID());

        islands.get(7).setOwner(2);
        islands.get(8).setOwner(2);
        islands.get(6).setOwner(2);
        game.uniteIslands(islands.get(7).getID());

        assertEquals(8, islands.size());

    }

    /**
     * checks that uniteIslands() correctly trows if the island we ask to unite is not on the
     * list of available islands
     * @see Game#uniteIslands(int)
     */
    @Test
    public void uniteIslandsExceptionTest() {

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            List<Island> islands = game.islands;
            islands.get(5).setOwner(1);
            islands.get(4).setOwner(1);
            islands.get(6).setOwner(1);
            int removedId = islands.get(6).getID();
            game.uniteIslands(islands.get(5).getID());
            game.uniteIslands(removedId);

        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Island with id ="));
        assertTrue(actualMessage.contains(" is not in the list"));

    }

    /**
     * checks that the method nextRound() correctly orders listPlayers for the next round to start
     * @see Game#nextRound()
     * @see Game#listPlayers
     */
    @Test
    public void nextRoundTest(){
        Game game = new Game(1,3);
        game.numTurns = 1;
        game.listPlayers.get(0).getHand().setCardPlayed(game.listPlayers.get(0).getHand().getAllCards().get(7));
        game.listPlayers.get(1).getHand().setCardPlayed(game.listPlayers.get(1).getHand().getAllCards().get(3));
        game.listPlayers.get(2).getHand().setCardPlayed(game.listPlayers.get(2).getHand().getAllCards().get(9));

        List<Player> expected = new ArrayList<>(List.of(game.listPlayers.get(1),game.listPlayers.get(0),game.listPlayers.get(2)));

        game.nextRound();
        assertEquals(expected,game.listPlayers);
    }

    /**
     * checks that the method buyPersonality correctly throws an exception if
     * the selected personality is not among the available personalities
     * NOTE: Sometimes test may fail due to random selection of personalities done by startGame(), rerun may resolve error
     * @see Game#buyPersonality(int, Personality)
     * @see Constants#PERSONALITIES_STARTING_PRICE
     */
    @Test
    public void buyPersonalityExceptionTest1(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers botanist = new Containers("botanist");
            game.buyPersonality(1,botanist);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Non existing personality"));
    }

    /**
     * checks that the method buyPersonality() correctly trows an exception when
     * the player doesn't exist
     * @see Game#buyPersonality(int, Personality)
     */
    @Test
    public void buyPersonalityExceptionTest2(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers botanist = new Containers("botanist");
            game.listPersonality.add(botanist);
            game.buyPersonality(5,botanist);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Non existing player"));
    }

    /**
     * checks that the method buyPersonality correctly throws an exception when
     * the player doesn't have enough money to buy personality
     * @see Game#buyPersonality(int, Personality)
     */
    @Test
    public void buyPersonalityExceptionTest3(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers botanist = new Containers("botanist");
            game.listPersonality.add(botanist);
            game.buyPersonality(2,botanist);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Not enough money"));
    }

    /**
     * checks that the method buyPersonality() correctly sets the owner and increases the cost of the card
     * while decreasing the available coins for the player selected
     * @see Game#buyPersonality(int, Personality)
     * @see GameBoard#getCoins()
     * @see Personality#getCost()
     */
    @Test
    public void buyPersonalityTest(){
        Game game = new Game(1,2);
        Player player = game.listPlayers.get(0);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Personality botanist = PersonalityFactory.generate("botanist");
        game.listPersonality.add(botanist);

        int oldCost = botanist.getCost();

        for (int i =0; i < 10; i++)
            player.getGameBoard().addStudentHall(Color.PINK); //this will add 3 coins to player

        int expectedCoin = 3;
        assertEquals(expectedCoin, player.getGameBoard().getCoins());

        expectedCoin -= oldCost;
        game.buyPersonality(player.getPlayerId(),botanist);

        assertEquals(expectedCoin, player.getGameBoard().getCoins());//checks that player has decreased amount of coins
        assertEquals(oldCost+1, botanist.getCost());//checks increase in botanist's price for future usese
        assertEquals(player.getPlayerId(), botanist.getOwner());//checks botanist's owner

    }

    /**
     * Test checks if the method usePersonalityPower throws an exception when no personality is set as active
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerExceptionTest1(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers bot = new Containers("botanist");
            game.listPersonality.add(bot);
            game.listPersonality.add(new Modifier("witch"));
            game.listPersonality.add(new Containers("jester"));
            game.usePersonalityPower(bot);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Selected person is not active"));

    }

    /**
     * Test checks if the method usePersonalityPower throws an exception when one personality is set as active
     * but usePower is called for another
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerExceptionTest2(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers bot = new Containers("botanist");
            game.listPersonality.add(bot);
            game.listPersonality.add(new Modifier("witch"));
            game.listPersonality.add(new Containers("jester"));
            game.listPersonality.get(2).setActive(true);
            game.usePersonalityPower(bot);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Selected person is not active"));

    }

    /**
     * Tests that usePersonalityPower works when used with correct parameters
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerBotanist1(){
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Containers bot = new Containers("botanist");
        bot.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(bot);
        game.listPersonality.add(new Modifier("witch"));
        game.listPersonality.add(new Containers("jester"));
        bot.setActive(true);

        int island = 5;
        //TODO change after player selection is implemented
        System.setIn(new ByteArrayInputStream(((island+1)+"\n").getBytes()));//sends value as console input (+1 is added to get islandId)
        game.usePersonalityPower(bot);
        assertTrue(game.islands.get(island).getNoEntry());
        assertFalse(bot.isActive());
    }

    /**
     * Tests that usePersonalityPower throws an exception in case that botanist is the active personality
     * and there are no more NoEntry tiles available
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerBotanistExceptionTest3(){



        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            game.listPersonality = new ArrayList<>(); //puts empty list
            Containers bot = new Containers("botanist");
            game.listPersonality.add(bot);
            bot.setOwner(game.listPlayers.get(0).getPlayerId());
            for (int i = 0; i < 4; i++)
                bot.useTile();
            game.listPersonality.add(new Modifier("witch"));
            game.listPersonality.add(new Containers("jester"));
            bot.setActive(true);

            int  island = 3;
            //TODO change after player selection is implemented
            System.setIn(new ByteArrayInputStream((island+"\n").getBytes()));
            game.usePersonalityPower(bot);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("No more tiles available"));
    }

    /**
     * Assures that GameID is correctly set
     * @see Game#getGameID()
     */
    @Test
    public void gameIdTest(){
        int expected = 3;
        Game game = new Game(expected, 2);
        assertEquals(expected, game.getGameID());
    }

    /**
     * Assures that correct number of personalities is set at beginning of game
     * @see Game#getProfessors()
     */
    @Test
    public void getPersonalityTest(){
        Game game = new Game(1,2);

        assertEquals(3, game.getPersonality().size());
    }

    /**
     * Assures that correct number of professors is set and that they have correct owner and color
     * @see Game#getProfessors()
     */
    @Test
    public void getProfTest(){
        Game game = new Game(1,2);
        assertEquals(Color.values().length, game.getProfessors().size());
        ArrayList<Color> availableColors = new ArrayList<>(List.of(Color.values()));
        for (Professor p : game.getProfessors())
        {
            assertEquals(0, p.getOwner());
            assertTrue(availableColors.contains(p.getColor()));
            for(Professor p2: game.getProfessors())
                if(!p.equals(p2))
                    assertNotEquals(p.getColor(), p2.getColor());
        }
    }

    /**
     * Tests that usePersonalityPower works when used with winemaker active
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerWinemaker(){
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Containers wine = new Containers("winemaker");
        wine.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(wine);
        game.listPersonality.add(new Modifier("witch"));
        game.listPersonality.add(new Containers("jester"));
        wine.setActive(true);

        int island = 0;
        wine.addNewStudents(List.of(Color.GREEN));
        int oldWineStudentsSize = wine.getStudents().size();
        //TODO change after player selection is implemented
        System.setIn(new ByteArrayInputStream(((island+1)+"\n").getBytes()));
        //this replaces the player selection

        game.usePersonalityPower(wine);

        //TODO change after player selection is implemented (not Color.Green but selection)
        assertTrue(game.islands.get(island).getStudents().contains(Color.GREEN));
        // this actually works only if the bag is not empty
        assertEquals(wine.getStudents().size(), oldWineStudentsSize);
    }

    /**
     * Tests that usePersonalityPower works when used with Jester active
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerJester(){
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Containers jest = new Containers("jester");
        jest.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(jest);
        jest.setActive(true);

        //these two lists are a substitute for the selected color by the player
        List<Color> studentsFromCard = List.of(Color.BLUE, Color.RED);
        List<Color> studentsFromEntrance = List.of(Color.GREEN, Color.YELLOW);
        jest.addNewStudents(studentsFromCard);
        game.listPlayers.get(0).getGameBoard().addStudentsEnter(studentsFromEntrance);
        //TODO change after player selection is implemented
        game.usePersonalityPower(jest);

        assertTrue(game.listPlayers.get(0).getGameBoard().getStudentsEnter().containsAll(studentsFromCard));
        assertTrue(jest.getStudents().containsAll(studentsFromEntrance));
    }

    /**
     * Tests that usePersonalityPower works when used with Courtesan active
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerCourtesan(){
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Containers court = new Containers("courtesan");
        court.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(court);
        court.setActive(true);

        //this list is a substitute for the selected color by the player
        List<Color> studentFromCard = List.of(Color.RED);
        court.addNewStudents(studentFromCard);
        int oldCourtesanStudentsSize = court.getStudents().size();

        //TODO change after player selection is implemented
        game.usePersonalityPower(court);

        assertEquals(court.getStudents().size(), oldCourtesanStudentsSize);
        assertTrue(game.listPlayers.get(0).getGameBoard().getStudentsHall(Color.RED) >= 1);
    }

    /**
     * Tests that usePersonalityPower works when used with Pirate active
     * @see Game#usePersonalityPower(Personality)
     */
    @Test
    public void usePersonalityPowerPirate(){
        Game game = new Game(1,2);
        game.listPersonality = new ArrayList<>(); //puts empty list
        Modifier pir = new Modifier("pirate");
        pir.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(pir);
        pir.setActive(true);

        //TODO change after player selection is implemented
        Island selectedIsland = game.islands.get(game.getIslandPosition(1));
        game.usePersonalityPower(pir);

        //if there is no towers or students nothing has to change
        if(selectedIsland.getStudents().size() == 0 &&
            selectedIsland.getTowers().size() == 0){
            assertEquals(0, selectedIsland.getOwner());
        }
        //TODO see if island domination worked correctly
    }
}