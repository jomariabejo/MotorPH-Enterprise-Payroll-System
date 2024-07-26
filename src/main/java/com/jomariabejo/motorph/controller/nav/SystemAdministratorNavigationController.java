package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.employee.RolePermissionController;
import com.jomariabejo.motorph.controller.role.systemadministrator.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

public class SystemAdministratorNavigationController implements _ViewLoader {

    @Getter
    @Setter
    @FXML
    private MainViewController mainViewController;

    public SystemAdministratorNavigationController() {

    }

    public SystemAdministratorNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void dashboard() {
        mainViewController.rewriteLabel("/ System Administrator / Dashboard");

        loadView("/com/jomariabejo/motorph/role/systemadmin/dashboard.fxml", controller -> {
            if (controller instanceof DashboardController) {
                DashboardController dashboardController = (DashboardController) controller;
                dashboardController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    @FXML
    public void logs() {
        mainViewController.rewriteLabel("/ System Administrator / Logs");

        loadView("/com/jomariabejo/motorph/role/systemadmin/logs.fxml", controller -> {
            if (controller instanceof UserLogsController) {
                UserLogsController userLogController = (UserLogsController) controller;
                userLogController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    @FXML
    public void permissions() {
        mainViewController.rewriteLabel("/ System Administrator / Permissions");

        loadView("/com/jomariabejo/motorph/role/systemadmin/permissions.fxml", controller -> {
            if (controller instanceof PermissionController) {
                PermissionController permissionController = (PermissionController) controller;
                permissionController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    @FXML
    public void rolePermission() {
        mainViewController.rewriteLabel("/ System Administrator / Role-Permission");

        loadView("/com/jomariabejo/motorph/role/systemadmin/role.fxml", controller -> {
            if (controller instanceof RoleController) {
                RoleController roleController = (RoleController) controller;
                roleController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    @FXML
    public void roles() {
        mainViewController.rewriteLabel("/ System Administrator / Roles");

        loadView("/com/jomariabejo/motorph/role/systemadmin/role-permission.fxml", controller -> {
            if (controller instanceof RolePermissionController) {
                RolePermissionController rolePermissionController = (RolePermissionController) controller;
                rolePermissionController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    @FXML
    public void users() {
        mainViewController.rewriteLabel("/ System Administrator / Users");

        loadView("/com/jomariabejo/motorph/role/systemadmin/user.fxml", controller -> {
            if (controller instanceof UserController) {
                UserController userController = (UserController) controller;
                userController.setSystemAdministratorNavigationController(this);
            }
        });
    }

    public <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Load the UI (AnchorPane)
            AnchorPane pane = loader.load();
            this.mainViewController.getMainBorderPane().setCenter(pane);

            // Initialize the controller
            T controller = loader.getController();
            controllerInitializer.accept(controller);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

}
