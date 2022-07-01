package it.polimi.ingsw.am54.model;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.am54.model.Constants.CONTAINERS_PERSONALITIES;
import static it.polimi.ingsw.am54.model.Constants.PERSONALITIES_STARTING_PRICE;

/**
 * Factory pattern that allows creation of both Modifier and Containers.
 */
public class PersonalityFactory {
    /**
     * Generates personality (of type Containers of Modifier) based on its name.
     * @param personalityName name of personality
     * @return Container or Modifier
     * @throws RuntimeException if name of personality isn't in the list of personalities
     */
    public static Personality generate(String personalityName) throws RuntimeException{
        if(CONTAINERS_PERSONALITIES.containsKey(personalityName)){
            return new Containers(personalityName);
        } else if (PERSONALITIES_STARTING_PRICE.containsKey(personalityName)){
            return new Modifier(personalityName);
        } else {
            throw new RuntimeException("Personality name not existing: " + personalityName);
        }
    }

    public static List<Personality> generateAll(List<String> personalityNames) throws RuntimeException{
        return personalityNames.stream().map(PersonalityFactory::generate).toList();
    }

}
