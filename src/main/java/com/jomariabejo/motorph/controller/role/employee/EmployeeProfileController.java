package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter
@Setter
public class EmployeeProfileController {


    private EmployeeRoleNavigationController employeeRoleNavigationController;

    @FXML
    private BorderPane employeeProfileBorderPane;

    @FXML
    private Label actionLabel;

    @FXML
    private Button changePasswordButton;

    @FXML
    Button lastButton;

    public EmployeeProfileController() {

    }

    public void setupBorderTopPane() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-top-pane.fxml"));
            VBox anchorPane = (VBox) fxmlLoader.load();

            employeeProfileBorderPane.setTop(anchorPane);

            ProfileTopPane profileTopPane = fxmlLoader.getController();
            profileTopPane.setEmployeeProfileController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setupBorderCenterPane() {

    }

    @FXML
    private Label address;

    @FXML
    private Label dateOfBirth;

    @FXML
    private Label department;

    @FXML
    private Label employeeNumber;

    @FXML
    private Label firstName;

    @FXML
    private Label lastName;

    @FXML
    private Label position;

    @FXML
    void changePasswordClicked() {
        this.employeeRoleNavigationController.getMainViewController().rewriteLabel("/ Employee / Change Password");
    }

    /**
     * TODO IT CREATE NEW UI WHEN MORE INFO IS CLICKED
     *
     * @throws IOException
     */
    @FXML
    void moreInfoClicked() {
        if ("Profile".equals(lastButton.getText())) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/dashboard.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                employeeProfileBorderPane.setCenter(anchorPane);
                actionLabel.setText("Profile");
                lastButton.setText("More Info");
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace(); // Consider logging this exception instead of printing
            }
        } else {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/profile-more-info.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                employeeProfileBorderPane.setCenter(anchorPane);
                actionLabel.setText("More Info");
                lastButton.setText("Profile");
            } catch (IOException e) {
                // Handle the exception appropriately
                e.printStackTrace(); // Consider logging this exception instead of printing
            }
        }
    }

    public void profileClicked() throws IOException {


    }
}
