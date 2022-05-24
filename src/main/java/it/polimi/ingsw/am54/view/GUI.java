package it.polimi.ingsw.am54.view;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class GUI {
    public Socket connectToServer() {
        return null;
    }

    public void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
}
