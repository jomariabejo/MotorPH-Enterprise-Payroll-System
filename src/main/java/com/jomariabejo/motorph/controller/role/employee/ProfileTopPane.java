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

@Getter
@Setter
public class ProfileTopPane {

    private EmployeeProfileController employeeProfileController;

    @FXML
    private Label actionLabel;

    @FXML
    private Button changePasswordButton;

    @FXML
    private Button lastButton;

    @FXML
    void changePasswordClicked(ActionEvent event) {

    }

    @FXML
    void moreInfoClicked(ActionEvent event) {
        if (lastButton.getText().equals("Profile")) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/dashboard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                this.employeeProfileController.getEmployeeProfileBorderPane().setCenter(anchorPane);
                actionLabel.setText("Profile");
                lastButton.setText("More Info");
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace(); // Consider logging this exception instead of printing
            }
        }
        else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-more-info.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                this.employeeProfileController.getEmployeeProfileBorderPane().setCenter(anchorPane);
                actionLabel.setText("More Info");
                lastButton.setText("Profile");
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace(); // Consider logging this exception instead of printing
            }
        }
    }
}
