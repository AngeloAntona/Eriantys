package it.polimi.ingsw.am54.model;


import org.junit.jupiter.api.Test;
import java.util.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ContainersTest  {


    @Test
    public void studentCorrectNumberTest(){
        Containers container = new Containers("winemaker");
        List<Color> expected  = new ArrayList<>();

        for(int i = 0; i < 4; i++)
        {
            expected.add(Color.values()[i]);
            container.addNewStudents(List.of(Color.values()[i]));
        }

        assertEquals(expected.size(), container.getStudents().size());
        assertTrue(expected.containsAll(container.getStudents()));
    }

    @Test
    public void studentsExceptionTooManyStudentsTest(){


        Exception exception = assertThrows(RuntimeException.class, () -> {
            Containers container = new Containers("winemaker");

            for(int i = 0; i < 5; i++)
            {
                container.addNewStudents(List.of(Color.values()[i]));

            }
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains("Too many students"));
    }

    @Test
    public void addNewStudentsTest(){
        Containers container = new Containers("winemaker");
        ArrayList<Color> oldStudents = new ArrayList<>();
        ArrayList<Color> newStudents = new ArrayList<>();

        oldStudents.add(Color.RED);
        container.addNewStudents(oldStudents);
        int oldSize = container.getStudents().size();

        newStudents.add(Color.BLUE);
        newStudents.add(Color.PINK);
        container.addNewStudents(newStudents);

        assertEquals(container.getStudents().size(),oldSize  + newStudents.size());
        assertTrue(container.getStudents().containsAll(oldStudents));
        assertTrue(container.getStudents().containsAll(newStudents));
    }

    @Test
    public void removeStudentsTest(){
        Containers container = new Containers("winemaker");
        container.addNewStudents(List.of(Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLUE));
        int oldSize = container.getStudents().size();

        container.removeStudents(List.of(Color.BLUE));
        assertEquals(container.getStudents().size(), oldSize-1);
        assertTrue(container.getStudents().containsAll(List.of(Color.BLUE, Color.GREEN, Color.YELLOW)));
    }

   @Test
   public void removeStudentsExceptionTest(){

       Exception exception = assertThrows(RuntimeException.class, () -> {
           Containers container = new Containers("jester");
           //container currently has an empty list of available students and removeStudents should throw the exception

           container.removeStudents(List.of(Color.RED));

       });
       String actualMessage = exception.getMessage();
       assertTrue(actualMessage.contains("does not contain all selected students"));
   }

   @Test
   public void useTileTest()
   {
       Containers container = new Containers("botanist");
       for(int i = 0; i <4 ; i++){
           assertTrue(container.useTile());
       }

       assertFalse(container.useTile());
   }

   @Test
   public void bringBackTileTest(){
        Containers  container = new Containers("botanist");
        for(int i = 0; i < 4; i++){
            container.useTile();
            container.bringBackTile();
        }
        //there should be 4 tiles in container because every used tile has been brought back
       for(int i = 0; i <4 ; i++){
           assertTrue(container.useTile());
       }

       assertFalse(container.useTile());
   }

    @Test
    public void bringBackTileExceptionTest(){
        Exception exception = assertThrows(RuntimeException.class, () -> {
            Containers container = new Containers("botanist");
            //container currently has 4 (max number) tiles

            container.bringBackTile();

        });
        String actualMessage = exception.getMessage();
        assertEquals("Too many tiles, max number is 4", actualMessage);

    }

    @Test
    public void costTest(){
        Set<String> names = new HashSet<>(Constants.CONTAINERS_PERSONALITIES.keySet());
        names.add("botanist");//variable names contains all personalities that are classified as Containers
        for (String name :names) {
            Containers container = new Containers(name);
            assertEquals(Constants.PERSONALITIES_STARTING_PRICE.get(name), container.getCost());
        }
    }

}