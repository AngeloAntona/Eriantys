module it.polimi.ingsw.am54 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens it.polimi.ingsw.am54 to com.google.gson, javafx.fxml;
    exports it.polimi.ingsw.am54.model to com.google.gson;
    exports it.polimi.ingsw.am54.network to com.google.gson;
    exports it.polimi.ingsw.am54;
    exports it.polimi.ingsw.am54.view;
    exports it.polimi.ingsw.am54.model.controllers to com.google.gson;
}