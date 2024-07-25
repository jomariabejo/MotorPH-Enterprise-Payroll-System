package com.jomariabejo.motorph.controller;

import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainViewController {

    private EmployeeRoleNavigationController employeeRoleNavigationController;
    private HumanResourceAdministratorNavigationController humanResourceAdministratorNavigationController;
    private PayrollAdministratorNavigationController payrollAdministratorNavigationController;
    private SystemAdministratorNavigationController systemAdministratorNavigationController;


    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label selectedButtonLabel;

    public void rewriteLabel(String string) {
        selectedButtonLabel.setText(string);
    }

    public MainViewController() {
        this.mainBorderPane = mainBorderPane;
         employeeRoleNavigationController = new EmployeeRoleNavigationController();
         humanResourceAdministratorNavigationController = new HumanResourceAdministratorNavigationController(this);
         payrollAdministratorNavigationController = new PayrollAdministratorNavigationController(this);
         systemAdministratorNavigationController = new SystemAdministratorNavigationController(this);
    }

    public void initializeUserNavigation(String username) {
        includeEmployeeRoleNavigation();
//        includeSystemAdminRoleNavigation();
//        includePayrollAdminRoleNavigation();
    }

    private void includeHumanResourceAdminRoleNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-human-resource.fxml"));

            // Load the UI (AnchorPane)
            AnchorPane humanResourceRoleNavigation = loader.load();
            mainBorderPane.setLeft(humanResourceRoleNavigation);

            // Load the controller separately
            humanResourceAdministratorNavigationController = loader.getController();
            humanResourceAdministratorNavigationController.setMainViewController(this);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }


    private void includePayrollAdminRoleNavigation() {

    }

    private void includeSystemAdminRoleNavigation() {

    }

    private void includeEmployeeRoleNavigation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/nav/role-employee.fxml"));
            AnchorPane employeeRoleNavigation = loader.load();
            mainBorderPane.setLeft(employeeRoleNavigation);

            employeeRoleNavigationController = loader.getController();
            employeeRoleNavigationController.setMainViewController(this);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Menu roles of the main view🫴
     */
    public void menuItemEmployeeOnAction(ActionEvent actionEvent) {
        includeEmployeeRoleNavigation();
    }

    public void menuItemHumanResourceOnAction(ActionEvent actionEvent) {
        includeHumanResourceAdminRoleNavigation();
    }

    public void menuItemAccountingOnAction(ActionEvent actionEvent) {
        includePayrollAdminRoleNavigation();
    }

    public void menuISystemAdminOnAction(ActionEvent actionEvent) {
        includeSystemAdminRoleNavigation();
    }
}
