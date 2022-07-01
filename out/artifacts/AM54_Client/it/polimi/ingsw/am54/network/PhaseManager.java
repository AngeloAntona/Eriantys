package it.polimi.ingsw.am54.network;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.view.GUIController;
import it.polimi.ingsw.am54.view.ViewController;

import static it.polimi.ingsw.am54.network.Client.*;

/**
 * the PhaseManager contains methods to control sequence of phases in the game.
 */
public class PhaseManager {
    private ViewController vc;

    public PhaseManager(ViewController vc) {
        this.vc = vc;
    }

    /**
     * this method starts the game by calling Client.startGame().
     * If the server starts the game this method calls viewController.initGame()
     * and goes into the waitState.
     * @see Client#startGame()
     * @see #waitState()
     */
    public void startGame(){
        Client.startGame();
        String resp = getCommand(receiveCommand());
        System.out.println(resp);
        if(resp.equals("wait")){
            vc.initGame();
            waitState();
        }

        //end();
    }

    /**
     * this method waits for a phase message from the server and 
     * selects the right phase function call from the viewController 
     * @see Client#receiveCommand() 
     * @see ViewController
     */
    private void truewaitState(){
        String response = receiveCommand();
        System.out.println("phase: " + response);
        switch (getCommand(response)) {
            case "wait" -> vc.waitPhase();
            case "planning_turn" -> vc.planningPhase();
            case "next_turn" -> vc.actionPhase();
            case "end" -> end();
            case "Winner" -> vc.displayMessage(response);
            default -> vc.displayMessage(response);
        }
    }

    /**
     * this method wraps around truewaitState() for compatibility with 
     * the GUI thread. 
     * @see PhaseManager#truewaitState()
     */
    public void waitState() {
        if(vc instanceof GUIController) {
            new Thread(this::truewaitState).start();
        } else {
            truewaitState();
        }
    }

    /**
     * this method decodes the update message from the server and calls 
     * viewController.update()
     * @see ViewController#update(updateMessage) 
     * @param json
     */
    public void update(String json){
        Gson gson = new Gson();
        updateMessage update = gson.fromJson(json, new TypeToken<updateMessage>(){}.getType());
        vc.update(update);
        waitState();
    }
}
