package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
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

    @Test
    public void controlsProfTest2(){
        //all players have no students in hall
        int expected =0; // it is expected that all profs will have no owner
        Game game = new Game(1,4);
        game.controlsProf();

        for(Professor p: game.listProfessors){
            assertEquals(0,p.getOwner());
        }
    }
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


        List<Player> old = game.listPlayers;

        game.controlsProf();

        assertSame(old, game.listPlayers); // test checks if listPlayers has been changed by controlsProf

    }

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

    @Test
    public void buyPersonalityTest(){
        Game game = new Game(1,2);
        Player player = game.listPlayers.get(0);
        Containers botanist = new Containers("botanist");
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
}