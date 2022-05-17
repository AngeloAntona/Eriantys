package it.polimi.ingsw.am54.model;
import static it.polimi.ingsw.am54.model.Constants.CONTAINERS_PERSONALITIES;
import static it.polimi.ingsw.am54.model.Constants.PERSONALITIES_STARTING_PRICE;

/**
 * Factory pattern that allows creation of both Modifier and Containers
 */
public class PersonalityFactory {
    /**
     * Generates personality (of type Containers of Modifier) based on its name
     * @param personalityName name of personality
     * @return Container or Modifier
     * @throws RuntimeException if name of personality isn't in the list of personalities
     */
    public static Personality generate(String personalityName) throws RuntimeException{
        if(CONTAINERS_PERSONALITIES.containsKey(personalityName)){
            return new Containers(personalityName);
        } else if (PERSONALITIES_STARTING_PRICE.keySet().contains(personalityName)){
            return new Modifier(personalityName);
        } else {
            throw new RuntimeException("Personality name not existing: " + personalityName);
        }
    }
}
