package it.polimi.ingsw.am54.model;
import static it.polimi.ingsw.am54.model.Constants.CONTAINERS_PERSONALITIES;

/**
 * Factory pattern that allows creation of both Modifier and Containers
 */
public class PersonalityFactory {
    /**
     * Generates personality (of type Containers of Modifier) based on its name
     * @param personalityName name of personality
     * @return Container or Modifier
     */
    public static Personality generate(String personalityName){
        if(CONTAINERS_PERSONALITIES.containsKey(personalityName)){
            return new Containers(personalityName);
        } else {
            return new Modifier(personalityName);
        }
    }
}
