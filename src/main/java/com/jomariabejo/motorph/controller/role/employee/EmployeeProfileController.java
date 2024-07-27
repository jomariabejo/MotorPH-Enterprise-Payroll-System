package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button lastButton;

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

    public EmployeeProfileController() {
    }

    public void setupBorderTopPane() {
        loadVBox("/com/jomariabejo/motorph/role/employee/profile-top-pane.fxml");
    }

    private void loadVBox(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            VBox vBox = fxmlLoader.load();
            employeeProfileBorderPane.setTop(vBox);
            EmployeeProfileTopPane profileTopPane = fxmlLoader.getController();
            profileTopPane.setEmployeeProfileController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
