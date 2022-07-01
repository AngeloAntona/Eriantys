package it.polimi.ingsw.am54.network;

import it.polimi.ingsw.am54.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class that servers for updating clients.
 * Used instead of Game because using Game can cause abstract class instancing exception.
 */
public class updateMessage {
    public final int mother;
    public List<Player> players;
    public List<Island> islands;
    public Map<Integer,List<Color>> clouds;
    public ArrayList<Modifier> modifiers;
    public ArrayList<Containers> containers;

    public String currentPlayer;
    public String description;

    public updateMessage(Game game){
        this.players = game.listPlayers;
        this.islands = game.islands;
        this.clouds = game.clouds;
        this.mother = game.MotherNature;

        this.modifiers = new ArrayList<>();
        this.containers = new ArrayList<>();

        for(Personality p : game.getPersonality()) {
            if (p.getClass() == Modifier.class) {
                modifiers.add((Modifier) p);
            } else{
                containers.add((Containers) p);
            }
        }
    }
}

