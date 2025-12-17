package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.systemadministrator.*;
import com.jomariabejo.motorph.utility.CustomAlert;
import com.jomariabejo.motorph.utility.PermissionChecker;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Setter
@Getter
public class SystemAdministratorNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        // Update button visibility after mainViewController is set and FXML is loaded
        javafx.application.Platform.runLater(() -> {
            if (this.mainViewController != null) {
                updateButtonVisibility();
            }
        });
    }

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnLogs;

    @FXML
    private Button btnRoles;

    @FXML
    private Button btnPermissions;

    @FXML
    private Button btnRolePermission;

    @FXML
    private Button btnAnnouncements;

    public SystemAdministratorNavigationController() {
        // Default constructor required by JavaFX FXML loader.
    }

    public boolean hasPermission(String permissionName) {
        if (mainViewController == null || mainViewController.getUser() == null || mainViewController.getServiceFactory() == null) {
            return false;
        }
        return PermissionChecker.hasPermission(mainViewController.getUser(), permissionName, mainViewController.getServiceFactory());
    }

    public boolean requirePermission(String permissionName, String actionLabel) {
        if (hasPermission(permissionName)) {
            return true;
        }
        String label = (actionLabel == null || actionLabel.isBlank()) ? "this action" : actionLabel;
        CustomAlert alert = new CustomAlert(
                Alert.AlertType.ERROR,
                "Permission denied",
                "You don't have permission to perform " + label + "."
        );
        // NOTE: showAndWait() is not allowed during animation/layout processing (e.g., Timeline auto-refresh).
        // Use non-blocking show() and schedule it on the next pulse to avoid IllegalStateException.
        javafx.application.Platform.runLater(alert::show);
        return false;
    }

    @FXML
    public void initialize() {
        // This method is called automatically after FXML injection
        // Buttons are now available, but mainViewController might not be set yet
        // Ensure all buttons are visible and managed by default
        javafx.application.Platform.runLater(() -> setAllButtonsVisible(true));
    }

    @FXML
    public void dashboard() {
        if (mainViewController == null) {
            return;
        }
        mainViewController.rewriteLabel("/ System Administrator / Dashboard");

        loadView("/com/jomariabejo/motorph/role/systemadmin/dashboard.fxml", controller -> {
            if (controller instanceof DashboardController dashboardController) {
                dashboardController.setSystemAdministratorNavigationController(this);
                // Populate statistics after controller is set
                dashboardController.populateStatistics();
            }
        });
    }

    @FXML
    public void logs() {
        mainViewController.rewriteLabel("/ System Administrator / Logs");

        loadView("/com/jomariabejo/motorph/role/systemadmin/logs.fxml", controller -> {
            if (controller instanceof UserLogsController userLogController) {
                userLogController.setSystemAdministratorNavigationController(this);
                // Populate logs after controller is set
                // Use Platform.runLater to ensure UI is ready
                javafx.application.Platform.runLater(() -> {
                    userLogController.populateLogs();
                    // Start auto-refresh after logs are populated
                    userLogController.startAutoRefresh();
                });
            }
        });
    }

    @FXML
    public void permissions() {
        mainViewController.rewriteLabel("/ System Administrator / Permissions");

        loadView("/com/jomariabejo/motorph/role/systemadmin/permissions.fxml", controller -> {
            if (controller instanceof PermissionController permissionController) {
                permissionController.setSystemAdministratorNavigationController(this);
                // Populate permissions after controller is set
                javafx.application.Platform.runLater(permissionController::populatePermissions);
            }
        });
    }

    @FXML
    public void announcements() {
        mainViewController.rewriteLabel("/ System Administrator / Announcements");

        loadView("/com/jomariabejo/motorph/role/systemadmin/announcement.fxml", controller -> {
            if (controller instanceof com.jomariabejo.motorph.controller.role.systemadministrator.AnnouncementController announcementController) {
                announcementController.setSystemAdministratorNavigationController(this);
                javafx.application.Platform.runLater(announcementController::populateAnnouncements);
            }
        });
    }

    @FXML
    public void rolePermission() {
        mainViewController.rewriteLabel("/ System Administrator / Role-Permission");

        loadView("/com/jomariabejo/motorph/role/systemadmin/role-permission.fxml", controller -> {
            if (controller instanceof com.jomariabejo.motorph.controller.role.employee.RolePermissionController rolePermissionController) {
                rolePermissionController.setSystemAdministratorNavigationController(this);
                // Populate permissions first, then roles
                rolePermissionController.populatePermissions();
                rolePermissionController.populateRoles();
                
                // Ensure first role is selected and permissions are loaded after both populate methods complete
                javafx.application.Platform.runLater(rolePermissionController::ensureFirstRoleSelected);
            }
        });
    }

    @FXML
    public void roles() {
        mainViewController.rewriteLabel("/ System Administrator / Roles");

        loadView("/com/jomariabejo/motorph/role/systemadmin/role.fxml", controller -> {
            if (controller instanceof com.jomariabejo.motorph.controller.role.systemadministrator.RoleController roleController) {
                roleController.setSystemAdministratorNavigationController(this);
                // Populate roles after controller is set
                javafx.application.Platform.runLater(roleController::populateRoles);
            }
        });
    }

    @FXML
    public void users() {
        mainViewController.rewriteLabel("/ System Administrator / Users");

        loadView("/com/jomariabejo/motorph/role/systemadmin/user.fxml", controller -> {
            if (controller instanceof UserController userController) {
                userController.setSystemAdministratorNavigationController(this);
                // Populate users after controller is set
                userController.populateUsers();
            }
        });
    }

    public <T> void loadView(String fxmlPath, Consumer<T> controllerInitializer) {
        if (mainViewController == null || mainViewController.getMainBorderPane() == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Load the UI (AnchorPane)
            AnchorPane pane = loader.load();
            this.mainViewController.getMainBorderPane().setCenter(pane);

            // Initialize the controller
            T controller = loader.getController();
            controllerInitializer.accept(controller);
        } catch (IOException ioException) {
            throw new IllegalStateException("Failed to load view: " + fxmlPath, ioException);
        }
    }

    /**
     * Update button visibility based on user permissions.
     * Hides buttons that the user doesn't have permission to access.
     */
    public void updateButtonVisibility() {
        // Check if buttons are initialized
        if (btnDashboard == null) {
            // Buttons not yet initialized, try again later
            javafx.application.Platform.runLater(this::updateButtonVisibility);
            return;
        }

        if (mainViewController == null || mainViewController.getUser() == null || mainViewController.getServiceFactory() == null) {
            // If user/serviceFactory not available, show all buttons by default (better UX)
            setAllButtonsVisible(true);
            return;
        }

        var user = mainViewController.getUser();
        var serviceFactory = mainViewController.getServiceFactory();

        // Check if user is System Administrator - if so, show all buttons
        boolean isSystemAdmin = user.getRoleID() != null && 
                               user.getRoleID().getRoleName() != null &&
                               "System Administrator".equalsIgnoreCase(user.getRoleID().getRoleName());
        
        // If system admin, show all buttons
        if (isSystemAdmin) {
            setAllButtonsVisible(true);
            return;
        }

        // Otherwise, check individual permissions
        // Always set managed first, then visible to ensure synchronization
        if (btnDashboard != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_DASHBOARD_VIEW, serviceFactory);
            btnDashboard.setManaged(visible);
            btnDashboard.setVisible(visible);
        }

        if (btnUsers != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_USERS_VIEW, serviceFactory);
            btnUsers.setManaged(visible);
            btnUsers.setVisible(visible);
        }

        if (btnLogs != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_LOGS_VIEW, serviceFactory);
            btnLogs.setManaged(visible);
            btnLogs.setVisible(visible);
        }

        if (btnRoles != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_ROLES_VIEW, serviceFactory);
            btnRoles.setManaged(visible);
            btnRoles.setVisible(visible);
        }

        if (btnPermissions != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_PERMISSIONS_VIEW, serviceFactory);
            btnPermissions.setManaged(visible);
            btnPermissions.setVisible(visible);
        }

        if (btnRolePermission != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_ROLE_PERMISSIONS_VIEW, serviceFactory);
            btnRolePermission.setManaged(visible);
            btnRolePermission.setVisible(visible);
        }

        if (btnAnnouncements != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.SYSTEM_ADMIN_ANNOUNCEMENTS_MANAGE, serviceFactory);
            btnAnnouncements.setManaged(visible);
            btnAnnouncements.setVisible(visible);
        }
    }

    private void setAllButtonsVisible(boolean visible) {
        // Ensure visible and managed are always synchronized
        if (btnDashboard != null) {
            btnDashboard.setManaged(visible);
            btnDashboard.setVisible(visible);
        }
        if (btnUsers != null) {
            btnUsers.setManaged(visible);
            btnUsers.setVisible(visible);
        }
        if (btnLogs != null) {
            btnLogs.setManaged(visible);
            btnLogs.setVisible(visible);
        }
        if (btnRoles != null) {
            btnRoles.setManaged(visible);
            btnRoles.setVisible(visible);
        }
        if (btnPermissions != null) {
            btnPermissions.setManaged(visible);
            btnPermissions.setVisible(visible);
        }
        if (btnRolePermission != null) {
            btnRolePermission.setManaged(visible);
            btnRolePermission.setVisible(visible);
        }
        if (btnAnnouncements != null) {
            btnAnnouncements.setManaged(visible);
            btnAnnouncements.setVisible(visible);
        }
    }

}
