package com.jomariabejo.motorph.utility;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

public class CustomAlert extends Alert {

    public CustomAlert(AlertType alertType, String title, String contentText) {
        super(alertType);
        setCustomDimensions();
        setTitle(title);
        setContentText(contentText);
        applyCustomCSS();
    }

    private void setCustomDimensions() {
        // Allow the DialogPane to resize based on its content
        getDialogPane().setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        getDialogPane().setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }

    private void applyCustomCSS() {
        // Load the CSS stylesheet
        getDialogPane().getStylesheets().add(getClass().getResource("/com/jomariabejo/motorph/css/alert.css").toExternalForm());
    }
}
