package it.polimi.ingsw.am54.model;


import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for class Containers
 */
public class ContainersTest  {



    /**
     * Checks if method addNewStudents adds correct number and type of students
     * @see Containers#addNewStudents(List)
     */
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


    /**
     * Checks if RuntimeException is thrown when too many students are added to Container card
     * @see Containers#addNewStudents(List)
     */
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




    /**
     * Checks if removeStudents() removes correct type and amount of students
     * @see Containers#removeStudents(List)
     */
    @Test
    public void removeStudentsTest(){
        Containers container = new Containers("winemaker");
        container.addNewStudents(List.of(Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLUE));
        int oldSize = container.getStudents().size();

        container.removeStudents(List.of(Color.BLUE, Color.YELLOW));
        assertEquals(container.getStudents().size(), oldSize-2);
        assertTrue(container.getStudents().containsAll(List.of(Color.BLUE, Color.GREEN)));
    }


   /**
    * Checks if exception is thrown when tyring to remove non-existing students from Container card
    * @see Containers#removeStudents(List)
    */
   @Test
   public void removeStudentsExceptionTest(){

       Exception exception = assertThrows(RuntimeException.class, () -> {
           Containers container = new Containers("jester");
           /*
            *  container currently has an empty list of available students and removeStudents should throw the exception
            */


           container.removeStudents(List.of(Color.RED));

       });
       String actualMessage = exception.getMessage();
       assertTrue(actualMessage.contains("does not contain all selected students"));
   }


   /**
    * Checks if useTile() can be called exactly for times (without using method bringBackTile())
    * @see Containers#useTile()
    * @see  Containers#bringBackTile()
    */
   @Test
   public void useTileTest()
   {
       Containers container = new Containers("botanist");
       for(int i = 0; i <4 ; i++){
           assertTrue(container.useTile());
       }

       assertFalse(container.useTile());
   }


   /**
    * Checks functionality of bringBackTile()
    * @see Containers#bringBackTile()
    */
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


    /**
     * Checks if calling bringBackTile on card with 4 noEntry tiles causes exception to be thrown
     * @see Containers#bringBackTile()
     */
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


    /**
     * Checks if correct value is set for cost of every Container type Personality
     * @see Personality
     * @see Constants#PERSONALITIES_STARTING_PRICE
     */
    @Test
    public void costTest(){
        Set<String> names = new HashSet<>(Constants.CONTAINERS_PERSONALITIES.keySet());
        names.add("botanist");
        //set names contains all personalities that are classified as Containers

        for (String name :names) {
            Containers container = new Containers(name);
            assertEquals(Constants.PERSONALITIES_STARTING_PRICE.get(name), container.getCost());
        }
    }

}