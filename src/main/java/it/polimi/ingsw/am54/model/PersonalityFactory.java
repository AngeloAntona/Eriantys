package it.polimi.ingsw.am54.model;
import static it.polimi.ingsw.am54.model.Constants.CONTAINERS_PERSONALITIES;

public class PersonalityFactory {
    public static Personality generate(String personalityName){
        if(CONTAINERS_PERSONALITIES.containsKey(personalityName)){
            return new Containers(personalityName);
        } else {
            return new Modifier(personalityName);
        }
    }
}
