package it.polimi.ingsw.am54.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalityFactoryTest {
    /**
     * checks that PersonalityFactory returns correctly
     * Container or Modifier based on the personalityName.
     * In case the personality Name isn't a known personality
     * we check that the factory correctly throws a RuntimeException.
     * @see PersonalityFactory#generate(String)
     */
    @Test
    public void factoryTest(){
        Personality expectedModifier = PersonalityFactory.generate("glutton");
        Personality expectedContainer = PersonalityFactory.generate("courtesan");
        assertSame(expectedContainer.getClass(), Containers.class);
        assertSame(expectedModifier.getClass(), Modifier.class);
        assertThrows(RuntimeException.class,() -> {
           Personality expectedThrow = PersonalityFactory.generate("chicken") ;
        });
    }

}