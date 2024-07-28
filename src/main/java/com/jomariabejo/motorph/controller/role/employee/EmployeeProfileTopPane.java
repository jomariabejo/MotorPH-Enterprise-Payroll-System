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
public class EmployeeProfileTopPane {

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

        switch (this.lastButton.getText()) {
            case "Profile":
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/my-profile-view.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    this.getEmployeeProfileController().getEmployeeProfileBorderPane().setCenter(anchorPane);
                    lastButton.setText("More Info");
                    this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().
                            rewriteLabel("/ Employee / Profile / ");

                    EmployeeProfileController employeeProfileController = fxmlLoader.getController();
                    employeeProfileController.setEmployeeRoleNavigationController(this.getEmployeeProfileController().getEmployeeRoleNavigationController());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            case "More Info":
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-more-info.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    employeeProfileController.getEmployeeProfileBorderPane().setCenter(anchorPane);
                    lastButton.setText("Profile");
                    this.getEmployeeProfileController().getEmployeeRoleNavigationController().getMainViewController().
                            rewriteLabel("/ Employee / More Info / ");

                    MoreInfoController moreInfoController = fxmlLoader.getController();
                    moreInfoController.setEmployeeProfileController(employeeProfileController);
                    moreInfoController.personalInformationClicked();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
        }
    }
}
