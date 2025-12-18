package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.hr.*;
import com.jomariabejo.motorph.utility.PermissionChecker;
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
public class HumanResourceAdministratorNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    @FXML
    private Button btnHumanResourceDashboard;

    @FXML
    private Button btnEmployees;

    @FXML
    private Button btnTimesheets;

    @FXML
    private Button btnLeaveRequests;

    @FXML
    private Button btnOvertimeRequests;

    @FXML
    private Button btnAnnouncement;

    public HumanResourceAdministratorNavigationController() {

    }

    @FXML
    public void initialize() {
        // This method is called automatically after FXML injection
        // Ensure all buttons are visible and managed by default
        // Use Platform.runLater to ensure buttons are fully initialized
        javafx.application.Platform.runLater(() -> {
            setAllButtonsVisible(true);
        });
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
        // Update button visibility after mainViewController is set and FXML is loaded
        javafx.application.Platform.runLater(() -> {
            if (this.mainViewController != null) {
                updateButtonVisibility();
            }
        });
    }

    public void humanResourceDashboardOnAction() {
        if (mainViewController == null) {
            return;
        }
        mainViewController.rewriteLabel("/ Human Resource / Dashboard");

        loadView("/com/jomariabejo/motorph/role/human-resource/hr-dashboard.fxml", controller -> {

            if (controller instanceof HRDashboardController hrDashboardController) {
                hrDashboardController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }

    public void employeesOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Employees");

        loadView("/com/jomariabejo/motorph/role/human-resource/employee.fxml", controller -> {

            if (controller instanceof EmployeeController employeeController) {
                employeeController.setHumanResourceAdministratorNavigationController(this);
                employeeController.setup();
            }
        });
    }

    public void timesheetsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Timesheets");

        loadView("/com/jomariabejo/motorph/role/human-resource/timesheets.fxml", controller -> {

            if (controller instanceof TimesheetController timesheetController) {
                timesheetController.setHumanResourceAdministratorNavigationController(this);
                timesheetController.setup();
            }
        });
    }

    public void leaveRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Leave Requests");

        loadView("/com/jomariabejo/motorph/role/human-resource/leave-requests.fxml", controller -> {

            if (controller instanceof LeaveRequestController leaveRequestController) {
                leaveRequestController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }

    public void overtimeRequestsOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Overtime Requests");

        loadView("/com/jomariabejo/motorph/role/human-resource/overtime-requests.fxml", controller -> {

            if (controller instanceof OvertimeRequestsController overtimeRequestController) {
                overtimeRequestController.setHumanResourceAdministratorNavigationController(this);
            }
        });
    }

    public void announcementOnAction() {
        mainViewController.rewriteLabel("/ Human Resource / Announcements");

        loadView("/com/jomariabejo/motorph/role/human-resource/announcement.fxml", controller -> {
            if (controller instanceof AnnouncementController announcementController) {
                announcementController.setHumanResourceAdministratorNavigationController(this);
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
            throw new RuntimeException(ioException);
        }
    }

    /**
     * Update button visibility based on user permissions.
     * Hides buttons that the user doesn't have permission to access.
     */
    public void updateButtonVisibility() {
        // Check if buttons are initialized
        if (btnHumanResourceDashboard == null) {
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

        // Check if user is HR Administrator - if so, show all buttons
        boolean isHRAdmin = user.getRoleID() != null && 
                           user.getRoleID().getRoleName() != null &&
                           "HR Administrator".equalsIgnoreCase(user.getRoleID().getRoleName());
        
        // If HR admin, show all buttons
        if (isHRAdmin) {
            setAllButtonsVisible(true);
            return;
        }

        // Otherwise, check individual permissions
        // Always set managed first, then visible to ensure synchronization
        if (btnHumanResourceDashboard != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_DASHBOARD_VIEW, serviceFactory);
            btnHumanResourceDashboard.setManaged(visible);
            btnHumanResourceDashboard.setVisible(visible);
        }

        if (btnEmployees != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_EMPLOYEES_VIEW, serviceFactory);
            btnEmployees.setManaged(visible);
            btnEmployees.setVisible(visible);
        }

        if (btnTimesheets != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_TIMESHEETS_VIEW, serviceFactory);
            btnTimesheets.setManaged(visible);
            btnTimesheets.setVisible(visible);
        }

        if (btnLeaveRequests != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_LEAVE_REQUESTS_VIEW, serviceFactory);
            btnLeaveRequests.setManaged(visible);
            btnLeaveRequests.setVisible(visible);
        }

        if (btnOvertimeRequests != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_OVERTIME_REQUESTS_VIEW, serviceFactory);
            btnOvertimeRequests.setManaged(visible);
            btnOvertimeRequests.setVisible(visible);
        }

        if (btnAnnouncement != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.HR_ANNOUNCEMENTS_MANAGE, serviceFactory);
            btnAnnouncement.setManaged(visible);
            btnAnnouncement.setVisible(visible);
        }
    }

    private void setAllButtonsVisible(boolean visible) {
        // Ensure visible and managed are always synchronized - set managed first
        if (btnHumanResourceDashboard != null) {
            btnHumanResourceDashboard.setManaged(visible);
            btnHumanResourceDashboard.setVisible(visible);
        }
        if (btnEmployees != null) {
            btnEmployees.setManaged(visible);
            btnEmployees.setVisible(visible);
        }
        if (btnTimesheets != null) {
            btnTimesheets.setManaged(visible);
            btnTimesheets.setVisible(visible);
        }
        if (btnLeaveRequests != null) {
            btnLeaveRequests.setManaged(visible);
            btnLeaveRequests.setVisible(visible);
        }
        if (btnOvertimeRequests != null) {
            btnOvertimeRequests.setManaged(visible);
            btnOvertimeRequests.setVisible(visible);
        }
        if (btnAnnouncement != null) {
            btnAnnouncement.setManaged(visible);
            btnAnnouncement.setVisible(visible);
        }
    }
}
