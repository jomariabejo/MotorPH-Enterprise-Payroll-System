package com.jomariabejo.motorph.controller.role.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class ProfileTopPane {

    private static final Logger LOGGER = Logger.getLogger(ProfileTopPane.class.getName());

    private EmployeeProfileController employeeProfileController;

    @FXML
    private Label actionLabel;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button lastButton;

    @FXML
    void changePasswordClicked(ActionEvent event) {
        // Implement the change password logic here
    }

    @FXML
    void moreInfoClicked() {
        String fxmlPath = lastButton.getText().equals("Profile")
                ? "/com/jomariabejo/motorph/role/employee/dashboard.fxml"
                : "/com/jomariabejo/motorph/role/employee/profile-more-info.fxml";
        String newActionLabel = lastButton.getText().equals("Profile") ? "Profile" : "More Info";
        String newLastButtonText = lastButton.getText().equals("Profile") ? "More Info" : "Profile";

        loadFXML(fxmlPath, newActionLabel, newLastButtonText);
    }

    private void loadFXML(String fxmlPath, String actionLabelText, String lastButtonText) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane anchorPane = fxmlLoader.load();
            employeeProfileController.getEmployeeProfileBorderPane().setCenter(anchorPane);
            actionLabel.setText(actionLabelText);
            lastButton.setText(lastButtonText);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML: " + fxmlPath, e);
        }
    }
}
