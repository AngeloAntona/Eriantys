module it.polimi.ingsw.am54 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;
    requires com.sun.jna;
    requires com.sun.jna.platform;


    opens it.polimi.ingsw.am54 to com.google.gson, javafx.fxml;
    opens it.polimi.ingsw.am54.model;
    exports it.polimi.ingsw.am54.model to com.google.gson;
    exports it.polimi.ingsw.am54.network to com.google.gson;
    exports it.polimi.ingsw.am54;
    exports it.polimi.ingsw.am54.view;
    exports it.polimi.ingsw.am54.network.exceptions to com.google.gson;
    exports it.polimi.ingsw.am54.view.gui;
    exports it.polimi.ingsw.am54.view.cli;
    exports it.polimi.ingsw.am54.view.gui.controllers;
}