module com.jomariabejo.motorph {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires MaterialFX;

    opens com.jomariabejo.motorph to javafx.fxml;
    exports com.jomariabejo.motorph;
    opens com.jomariabejo.motorph.controller to javafx.fxml;
    exports com.jomariabejo.motorph.controller;
}