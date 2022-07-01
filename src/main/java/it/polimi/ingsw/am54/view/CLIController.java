package it.polimi.ingsw.am54.view;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am54.model.Card;
import it.polimi.ingsw.am54.model.TColor;
import it.polimi.ingsw.am54.network.*;
import it.polimi.ingsw.am54.network.exceptions.ServerErrorException;
import it.polimi.ingsw.am54.view.cli.CLI;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.am54.network.Client.*;

public class CLIController implements ViewController {
    private CLI cli;
    private PhaseManager phaseManager;

    public CLIController(CLI cli){
        this.cli = cli;
        Client.viewController = this;
        phaseManager = new PhaseManager(this);
        initializeView();
    }

    public void initializeView() {
        tryConnection();
        initialPhase();
    }

    private void tryConnection() {
        try {
            Map<String, String> input = cli.getServerInfo();
            Client.connectToServer(input.get("address"), Integer.parseInt(input.get("port")));
        } catch (IOException e) {
            displayMessage("Connection couldn't be established, please try again");
            tryConnection();
        }
    }

    @Override
    public void initialPhase() {
        joinGame();
        setUsername();
        selectMage();
        if(cli.getId() < 3)
            selectTowerColor();
        phaseManager.startGame();
    }

    @Override
    public void initGame() {
        //cli.printFile("src/main/resources/CLI_images/Eriantys_title_BW.txt");
    }

    protected void setUsername()  {
        try {
            String username = cli.getUsername();
            command("set_username", username);
        } catch (ServerErrorException e) {
           displayMessage("Username taken, please try again");
           setUsername();
        }
    }

    protected  void joinGame() {

        String selection = cli.joinGame();
        int id = 0;
        boolean advanced = Client.getParameter(selection).equals("true");
        try {
             id = Client.joinGame(selection);
        } catch (ServerErrorException e) {
            cli.displayMessage("FATAL ERROR CANNOT CONNECT \n CLOSING APP");
            try {
                command("end", null);
            } catch (ServerErrorException ex) {
                System.exit(405);
            }
            System.exit(404);
        }
        cli.clearScreen();
        //cli.printFile("/CLI_images/Eriantys_title_BW.txt");
        displayMessage("Welcome to the game, your id is " + id);
        cli.setId(id);
        cli.setAdvanced(advanced);
    }

    protected  void selectMage()  {
        List<Mage> available = Client.getAvailableMages();
        int selection = cli.selectMage(available);

        try {
            command("select_mage",available.get(selection));
        } catch (ServerErrorException e) {
            cli.displayMessage("Please select valid mage!");
            selectMage();
        }
    }

    protected  void selectTowerColor()  {
        List<TColor> available;
        available = Client.getAvailableTowerColors();
        int selection = cli.selectTower(available);

        try {
            command("select_tower",available.get(selection));
        } catch (ServerErrorException e) {
            cli.displayMessage("Please select valid tower color!");
            selectTowerColor();
        }

    }

    @Override
    public void planningPhase() {
        Card selectedCard = cli.selectAssistantCard();
        try {
            command("select_assistant_card", selectedCard);
        } catch (ServerErrorException e) {
            cli.displayMessage("Card unavailable, please repeat selection!");
            planningPhase();
        }
        cli.setCardPlayed(selectedCard);
        cli.removeFromHand(selectedCard);
        phaseManager.waitState();
    }

    @Override
    public void actionPhase() {
        boolean done = false;
        cli.clearScreen();
        cli.showUpdate();
        cli.setMovedInTurn(0);
        cli.nextRound();
        String command;
        while (!done){
            command = cli.commandSelection();
            switch (command) {
                case "move_students" -> moveStudents();
                case "move_mn" -> moveMN();
                case "select_cloud" -> {cloudSelection(); done = true;}
                case "use_personality" -> usePersonality();
                case "show_my_board" -> cli.showMyBoard();
                case "show_islands" -> cli.showIslands();
                case "show_other_boards" -> cli.otherBoards();
            }
        }
        phaseManager.waitState();
    }

    private void cloudSelection() {
        int cloud = cli.selectCloud();

        try {
            command("select_cloud", cloud);
        } catch (ServerErrorException e) {
            cli.displayMessage("Cloud unavailable, please repeat selection");
            cloudSelection();
        }

        cli.removeCommand("select_cloud");
    }

    private void usePersonality() {
        JsonObject object = cli.usePersonality();
        if(object == null)
            return;
        try {
            command("use_personality", object);
        } catch (ServerErrorException e) {
            cli.displayMessage("Illegal move, repeat selection");
            usePersonality();
            return;
        }
        if(object.get("card").toString().contains("archer"))
            cli.setAdditionalMN(2);
        Client.receiveSelfUpdate();
    }

    private void moveMN() {
        if(cli.getCardPlayed() == null){
            cli.displayMessage("You need to select assistant card before moving Mother Nature");
            return;
        }
        int selectedMoves = cli.moveMN();
        try {
            command("move_mn", selectedMoves);
        } catch (ServerErrorException e) {
            cli.displayMessage("Invalid selection\nPlease try again");
        }
        cli.removeCommand("move_mn");
        cli.setAdditionalMN(0);
        Client.receiveSelfUpdate();
    }


    private void moveStudents() {
        int movedInTurn = cli.getMovedInTurn();

        if(movedInTurn == 3){
            cli.displayMessage("Max number of students already moved");
            return;
        }

        JsonObject out = cli.moveStudents();

        try {
            command(Messages.moveStudents, out);
        } catch (ServerErrorException e) {
            cli.displayMessage("Illegal move, repeat selection");
            moveStudents();
            return;
        }
        int moved = Integer.parseInt(out.get("num_stud").toString());
        cli.removeStudents(out);
        cli.setMovedInTurn(movedInTurn + moved);
        if(cli.getMovedInTurn() == 3)
            cli.removeCommand("move_students");
        Client.receiveSelfUpdate();
    }

    @Override
    public void waitPhase() {
        displayMessage("Please wait for your turn");
        phaseManager.waitState();
    }

    @Override
    public void displayWin(String winner) {
        displayMessage(winner);
    }

    @Override
    public void displayMessage(String message) {
        cli.displayMessage(message);
    }

    @Override
    public void update(updateMessage update) {
        if(update.currentPlayer == null || !update.currentPlayer.equals(cli.myGameBoard.username)) {
            cli.update(update);
            phaseManager.waitState();
        } else {
            cli.dataUpdate(update);
        }
    }

    @Override
    public void playerDisconnected(String playerName) {
        cli.playerDisconnected(playerName);
    }
}
