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
    requires java.logging;
    requires java.sql;
    requires com.opencsv;

    opens com.jomariabejo.motorph.entity;
    exports com.jomariabejo.motorph.entity to javafx.fxml;
    opens com.jomariabejo.motorph to javafx.fxml;
    exports com.jomariabejo.motorph;
    opens com.jomariabejo.motorph.controller to javafx.fxml;
    exports com.jomariabejo.motorph.controller;
    exports com.jomariabejo.motorph.service;
    opens com.jomariabejo.motorph.service to javafx.fxml;
    opens com.jomariabejo.motorph.controller.myprofile to javafx.fxml;
    exports com.jomariabejo.motorph.controller.myprofile;
    opens com.jomariabejo.motorph.controller.hr to javafx.fxml;
    exports com.jomariabejo.motorph.controller.hr;
    opens com.jomariabejo.motorph.query;
    exports com.jomariabejo.motorph.query;
    opens com.jomariabejo.motorph.controller.finance to javafx.fxml;
    exports com.jomariabejo.motorph.controller.finance;
    opens com.jomariabejo.motorph.controller.personalinformation to javafx.fxml;
    exports com.jomariabejo.motorph.controller.personalinformation;

}