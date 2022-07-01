package it.polimi.ingsw.am54.view;
import it.polimi.ingsw.am54.network.updateMessage;

public interface ViewController {
    public void initialPhase();
    public void initGame();
    public void planningPhase();
    public void actionPhase();
    public void waitPhase();
    public void displayMessage(String message);
    public void update(updateMessage update);
    public void playerDisconnected(String playerName);
}
