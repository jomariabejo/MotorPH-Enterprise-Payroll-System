package com.jomariabejo.motorph.controller;

import atlantafx.base.theme.*;
import com.jomariabejo.motorph.controller.nav.EmployeeRoleNavigationController;
import com.jomariabejo.motorph.controller.nav.HumanResourceAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.PayrollAdministratorNavigationController;
import com.jomariabejo.motorph.controller.nav.SystemAdministratorNavigationController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Getter
@Setter
public class MainViewController implements _ViewLoader {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Label selectedButtonLabel;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblRoleName;

    public MainViewController() {
    }

    public void rewriteLabel(String string) {
        selectedButtonLabel.setText(string);
    }

    public void initializeUserNavigation(String username) {
        displayEmployeeRoleNavigation();
    }

    private void displayHumanResourceNavigation() {
        loadView("/com/jomariabejo/motorph/nav/role-human-resource.fxml", controller -> {
            if (controller instanceof HumanResourceAdministratorNavigationController) {
                HumanResourceAdministratorNavigationController humanResourceController = (HumanResourceAdministratorNavigationController) controller;
                humanResourceController.setMainViewController(this);
                humanResourceController.humanResourceDashboardOnAction();
            }
        });
    }


    private void displayAccountingNavigation() {
        loadView("/com/jomariabejo/motorph/nav/role-accounting.fxml", controller -> {
            if (controller instanceof PayrollAdministratorNavigationController) {
                PayrollAdministratorNavigationController accountingController = (PayrollAdministratorNavigationController) controller;
                accountingController.setMainViewController(this);
                accountingController.dashboardOnActtion();
            }
        });
    }

    private void displaySystemAdminNavigation() {
        loadView("/com/jomariabejo/motorph/nav/role-system-administrator.fxml", controller -> {
            if (controller instanceof SystemAdministratorNavigationController) {
                SystemAdministratorNavigationController systemAdminController = (SystemAdministratorNavigationController) controller;
                systemAdminController.setMainViewController(this);
                systemAdminController.dashboard();
            }
        });
    }

    private void displayEmployeeRoleNavigation() {
        loadView("/com/jomariabejo/motorph/nav/role-employee.fxml", controller -> {
            if (controller instanceof EmployeeRoleNavigationController) {
                EmployeeRoleNavigationController employeeController = (EmployeeRoleNavigationController) controller;
                employeeController.setMainViewController(this);
                employeeController.overviewOnAction();
            }
        });
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

    @Override
    public <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Load the UI (AnchorPane)
            AnchorPane pane = loader.load();
            // Depending on the context, you might want to set this in different areas of the BorderPane
            mainBorderPane.setLeft(pane);

            // Initialize the controller
            T controller = loader.getController();
            controllerInitializer.accept(controller);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public void closeClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void lightModeClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    public void darkModeClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    public void nordLightClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordLight().getUserAgentStylesheet());
    }

    public void nordDarkClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    public void cupertinoLightClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
    }

    public void cupertinoDarkLight(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
    }

    public void draculaClicked(ActionEvent actionEvent) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }
}
