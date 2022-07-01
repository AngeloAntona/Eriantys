package it.polimi.ingsw.am54.view;
import it.polimi.ingsw.am54.network.updateMessage;

public interface View {
    public void initialize();
    public void displayMessage(String message);
    public void displayErrorMessage(String message);
}
