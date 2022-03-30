package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    public void controlsProfTest1()
    {
        Game game = new Game(1,3);
        int[] expected = {1,2,3};
        for(int i = 0;  i < 5; i++) {
            game.listPlayers.get(expected[0]-1).getGameBoard().addStudentHall(Color.BLUE); // we have value - 1 because indexes of list start at 0 while player's id start at 1
            game.listPlayers.get(expected[1]-1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[2]-1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for(int i = 0;  i < 3; i++) {
            game.listPlayers.get(expected[1]-1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[2]-1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[0]-1).getGameBoard().addStudentHall(Color.YELLOW);
        }
        for(int i = 0;  i < 2; i++) {
            game.listPlayers.get(expected[2]-1).getGameBoard().addStudentHall(Color.BLUE);
            game.listPlayers.get(expected[0]-1).getGameBoard().addStudentHall(Color.RED);
            game.listPlayers.get(expected[1]-1).getGameBoard().addStudentHall(Color.YELLOW);
        }

        game.controlsProf();
        for(Professor p : game.listProfessors)
        {
            //expected values are increased
            if(p.getColor().equals(Color.BLUE))
                assertEquals(expected[0],p.getOwner());
            if(p.getColor().equals(Color.RED))
                assertEquals(expected[1],p.getOwner());
            if(p.getColor().equals(Color.YELLOW))
                assertEquals(expected[2],p.getOwner());
        }
    }
}