package com.jomariabejo.motorph.controller.role.employee;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private TextField tfAddress;

    @FXML
    private TextField tfDOB;

    @FXML
    private TextField tfDepartment;

    @FXML
    private TextField tfEmployeeNumber;

    @FXML
    private TextField tfFirstName;

    @FXML
    private TextField tfLastName;

    @FXML
    private TextField tfPosition;

    public EmployeeProfileController() {
    }

    @FXML
    private void initialize() {
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

    public void rewriteTextField() {
        tfAddress.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getAddress()
        );

        tfDOB.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getBirthday()
                        .toString()
        );

        tfPosition.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getPositionID()
                        .getPositionName()
        );

        tfLastName.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getLastName()
        );

        tfFirstName.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getFirstName()
        );

        tfEmployeeNumber.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getId()
                        .toString()
        );

        tfDepartment.setText(
                this.employeeRoleNavigationController
                        .getMainViewController()
                        .getEmployee()
                        .getPositionID()
                        .getDepartmentID().getDepartmentName()
        );
    }
}
