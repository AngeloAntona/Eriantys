package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
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
        for (int i = 0; i < 5; i++) {
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.BLUE); // we have value - 1 because indexes of list start at 0 while player's id start at 1
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 3; i++) {
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 2; i++) {
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.YELLOW);
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
        game.listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        game.controlsProf();
        int expected = 1; //it is expected that prof will not change owner

        for (Professor p : game.listProfessors) {
            //expected values are increased

            if (p.getColor().equals(Color.RED))
                assertEquals(expected, p.getOwner());

        }

        game.listPlayers.get(0).getGameBoard().addStudentHall(Color.RED);
        game.listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);
        game.listPlayers.get(1).getGameBoard().addStudentHall(Color.RED);

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
        game.listPersonality.add(botanist);
        game.listPersonality.get(0).setActive(true);
        game.listPersonality.get(0).setOwner(1);

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
        for (int i = 0; i < 5; i++) {
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.BLUE); // we have value - 1 because indexes of list start at 0 while player's id start at 1
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 3; i++) {
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for (int i = 0; i < 2; i++) {
            game.listPlayers.get(expected[2] - 1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[0] - 1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[1] - 1).getGameBoard().addStudentHall(Color.YELLOW);
        }


        List<Player> old = new ArrayList<>(game.listPlayers);

        game.controlsProf();

        assertEquals(old, game.listPlayers); // test checks if listPlayers has been changed by controlsProf

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

        for(int i = 0; i < 4; i++) {
            p.getGameBoard().addStudentsEnter(List.of(Color.YELLOW));
            game.islands.get(islandInd).addStudents(List.of(Color.BLUE));
        }

        List<Color> studentsEnter = p.getGameBoard().getStudentsEnter();
        List<Color> studentsIsland = game.islands.get(islandInd).getStudents();

        game.moveStudents(p.getPlayerId(), islandInd, Color.YELLOW);

        assertEquals(studentsEnter.size()-1, p.getGameBoard().getStudentsEnter().size());
        assertTrue(studentsEnter.containsAll(p.getGameBoard().getStudentsEnter()));

        assertEquals(studentsIsland.size()+1, game.islands.get(islandInd).getStudents().size());
        assertTrue(game.islands.get(islandInd).getStudents().containsAll(studentsIsland));
        assertTrue(game.islands.get(islandInd).getStudents().contains(Color.YELLOW));

    }

    /**
     * checks if movesStudents throws and exception if the student in the parameter student
     * isn't contained in the corresponding player GameBoard entrance
     * @see Game#moveStudents(int, int, Color)
     */
    @Test
    public void moveStudentExceptionTest(){
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
            Player p = game.listPlayers.get(0);


            for(int i = 0; i < 3; i ++)
                p.getGameBoard().addStudentsEnter(List.of(Color.BLUE));

            game.moveStudents(p.getPlayerId(), 0, Color.RED);//Player's entrance doesn't contain any red students

        });
        String actualMessage = exception.getMessage();
        assertEquals("Selected student is not in player's entrance", actualMessage);
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
        assertEquals(12,game.islands.size());//initial number of islands
        List<Color> students = new ArrayList<>(List.of(Color.RED,Color.RED,Color.RED));
        game.islands.get(11).addStudents(students);
        game.islands.get(1).addStudents(students);
        game.islands.get(0).addStudents(students);
        game.islands.get(11).setOwner(1);
        game.islands.get(1).setOwner(1);
        game.islands.get(0).setOwner(1);
        game.uniteIslands(1);
        assertEquals(10,game.islands.size());
        assertEquals(9,game.islands.get(0).getStudents().size());

        for(Color stud : game.islands.get(0).getStudents())
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
        assertEquals(12,game.islands.size());//initial number of islands

        game.islands.get(10).setOwner(1);
        game.islands.get(0).setOwner(1);
        game.islands.get(11).setOwner(1);

        game.uniteIslands(game.islands.get(11).getID());

        assertEquals(10,game.islands.size());
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
        assertEquals(12,game.islands.size());//initial number of islands


        game.islands.get(5).setOwner(1);
        game.islands.get(4).setOwner(1);
        game.islands.get(6).setOwner(1);

        game.uniteIslands(game.islands.get(5).getID());

        assertEquals(10,game.islands.size());

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
        assertEquals(12,game.islands.size());//initial number of islands


        game.islands.get(5).setOwner(1);
        game.islands.get(4).setOwner(1);
        game.islands.get(6).setOwner(1);
        game.uniteIslands(game.islands.get(5).getID());

        game.islands.get(7).setOwner(2);
        game.islands.get(8).setOwner(2);
        game.islands.get(6).setOwner(2);
        game.uniteIslands(game.islands.get(7).getID());

        assertEquals(8,game.islands.size());

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
            game.islands.get(5).setOwner(1);
            game.islands.get(4).setOwner(1);
            game.islands.get(6).setOwner(1);
            int removedId = game.islands.get(6).getID();
            game.uniteIslands(game.islands.get(5).getID());
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
     * @see Game#buyPersonality(int, Personality)
     * @see Constants#PERSONALITIES_STARTING_PRICE
     */
    @Test
    public void buyPersonalityExceptionTest1(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Game game = new Game(1,2);
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
        Containers bot = new Containers("botanist");
        bot.setOwner(game.listPlayers.get(0).getPlayerId());
        game.listPersonality.add(bot);
        game.listPersonality.add(new Modifier("witch"));
        game.listPersonality.add(new Containers("jester"));
        bot.setActive(true);

        int island = 5;
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
            Containers bot = new Containers("botanist");
            game.listPersonality.add(bot);
            bot.setOwner(game.listPlayers.get(0).getPlayerId());
            for (int i = 0; i < 4; i++)
                bot.useTile();
            game.listPersonality.add(new Modifier("witch"));
            game.listPersonality.add(new Containers("jester"));
            bot.setActive(true);

            int  island = 3;
            System.setIn(new ByteArrayInputStream((island+"\n").getBytes()));
            game.usePersonalityPower(bot);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("No more tiles available"));
    }
}