module it.polimi.ingsw.am54 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens it.polimi.ingsw.am54 to javafx.fxml;
    exports it.polimi.ingsw.am54;
}