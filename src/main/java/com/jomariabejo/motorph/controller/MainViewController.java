package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.hibernate.annotations.processing.Suppress;

import java.io.IOException;

public class MainViewController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;
    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;
    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;
    private SystemAdministratorNavigationController systemAdministratorNavigationController;


    @Getter
    @Setter
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label selectedButtonLabel;

    public void rewriteLabel(String string) {
        selectedButtonLabel.setText(string);
    }

    public MainViewController() {
        employeeRoleNavigationController = new EmployeeRoleNavigationController();
        humanResourceAdministratorNavigationController = new HumanResourceAdministratorNavigationController();
        payrollAdministratorNavigationController = new PayrollAdministratorNavigationController();
        systemAdministratorNavigationController = new SystemAdministratorNavigationController();
    }

    public void initializeUserNavigation(String username) {
        displayEmployeeRoleNavigation();
    }



    private void displayHumanResourceNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-human-resource.fxml"));

            // Load the UI (AnchorPane)
            AnchorPane humanResourceRoleNavigation = loader.load();
            mainBorderPane.setLeft(humanResourceRoleNavigation);

            // Load the controller separately
            humanResourceAdministratorNavigationController = loader.getController();
            humanResourceAdministratorNavigationController.setMainViewController(this);
            humanResourceAdministratorNavigationController.humanResourceDashboardOnAction();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }


    private void displayAccountingNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-accounting.fxml"));

            // Load the UI (AnchorPane)
            AnchorPane humanResourceRoleNavigation = loader.load();
            mainBorderPane.setLeft(humanResourceRoleNavigation);

            // Load the controller separately
            payrollAdministratorNavigationController = loader.getController();
            payrollAdministratorNavigationController.setMainViewController(this);
            payrollAdministratorNavigationController.dashboardOnActtion();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private void displaySystemAdminNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-system-administrator.fxml"));

            // Load the UI (AnchorPane)
            AnchorPane humanResourceRoleNavigation = loader.load();
            mainBorderPane.setLeft(humanResourceRoleNavigation);

            // Load the controller separately
            systemAdministratorNavigationController = loader.getController();
            systemAdministratorNavigationController.setMainViewController(this);
            systemAdministratorNavigationController.dashboard();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    private void displayEmployeeRoleNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-employee.fxml"));
            AnchorPane employeeRoleNavigation = loader.load();
            mainBorderPane.setLeft(employeeRoleNavigation);

            employeeRoleNavigationController = loader.getController();
            employeeRoleNavigationController.setMainViewController(this);
            employeeRoleNavigationController.overviewOnAction();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Menu roles of the main view🫴
     */
    public void menuItemEmployeeOnAction(ActionEvent actionEvent) {
        displayEmployeeRoleNavigation();
    }

    public void menuItemHumanResourceOnAction(ActionEvent actionEvent) {
        displayHumanResourceNavigation();
    }

    public void menuItemAccountingOnAction(ActionEvent actionEvent) {
        displayAccountingNavigation();
    }

    public void menuISystemAdminOnAction(ActionEvent actionEvent) {
        displaySystemAdminNavigation();
    }
}
