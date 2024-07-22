module com.jomariabejo.motorph {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires java.sql;
    requires com.opencsv;
    requires org.apache.pdfbox;
    requires static lombok;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.jomariabejo.motorph to javafx.fxml;
    exports com.jomariabejo.motorph;
    opens com.jomariabejo.motorph.model to javafx.fxml;
    exports com.jomariabejo.motorph.model;
}