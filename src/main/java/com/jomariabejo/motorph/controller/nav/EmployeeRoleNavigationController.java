package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.employee.*;
import com.jomariabejo.motorph.utility.PermissionChecker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Setter
@Getter
public class EmployeeRoleNavigationController implements _ViewLoader {

    private MainViewController mainViewController;
    
    private Object previousNavigationController;

    @FXML
    private Label lblWelcome;

    @FXML
    private Label lblFirstName;
    
    @FXML
    private Button btnBackToPreviousRole;

    @FXML
    private Button btnMyProfile;

    @FXML
    private Button btnFileLeaveRequest;

    @FXML
    private Button btnViewLeaveBalance;

    @FXML
    private Button btnViewLeaveHistory;

    @FXML
    private Button btnTimesheet;

    @FXML
    private Button btnOvertime;

    @FXML
    private Button btnPayslip;

    @FXML
    private Button btnReimbursement;

    @FXML
    private Button btnContactSupport;

    @FXML
    private Button btnNotification;

    @FXML
    private Button btnConversation;

    // Default constructor
    public EmployeeRoleNavigationController() {
        // Required by FXMLLoader
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

    public EmployeeRoleNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    @FXML
    public void contactSupportOnAction() {
        mainViewController.rewriteLabel("/ Employee / Contact Support");

        loadView("/com/jomariabejo/motorph/role/employee/contact-support.fxml", controller -> {
            if (controller instanceof ContactSupportController contactSupportController) {
                contactSupportController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void fileLeaveRequestOnAction() {
        mainViewController.rewriteLabel("/ Employee / File Leave Request");

        loadView("/com/jomariabejo/motorph/role/employee/file-leave-request.fxml", controller -> {
            if (controller instanceof FileLeaveRequestController fileLeaveRequest) {
                fileLeaveRequest.setEmployeeRoleNavigationController(this);
                fileLeaveRequest.setupLeaveTypesComboBox();
                fileLeaveRequest.configDatePicker();
                fileLeaveRequest.setSingleLeave();
                fileLeaveRequest.getLblLeaveDuration().setText("1");
            }
        });
    }

    @FXML
    public void overtimeOnAction() {
        mainViewController.rewriteLabel("/ Employee / Overtime");

        loadView("/com/jomariabejo/motorph/role/employee/overtime.fxml", controller -> {
            if (controller instanceof OvertimeController overtimeController) {
                overtimeController.setEmployeeRoleNavigationController(this);
                overtimeController.populateMonths();
                overtimeController.populateYears();
                overtimeController.setupOvertimeTableView();
                overtimeController.populateOvertimeTableView();
            }
        });
    }

    @FXML
    public void myProfileOnAction() {
        if (mainViewController == null) {
            return;
        }
        mainViewController.rewriteLabel("/ Employee / My Profile");
        loadView("/com/jomariabejo/motorph/role/employee/my-profile-view.fxml", controller -> {
            if (controller instanceof EmployeeProfileController employeeOverviewController) {
                employeeOverviewController.setEmployeeRoleNavigationController(this);
                employeeOverviewController.setupBorderTopPane();
                employeeOverviewController.rewriteTextField();
            }
        });
    }

    @FXML
    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Employee / Payslip");

        loadView("/com/jomariabejo/motorph/role/employee/payslip.fxml", controller -> {
            if (controller instanceof PayslipController payslipController) {
                payslipController.setEmployeeRoleNavigationController(this);
                payslipController.populateYears();
                payslipController.populatePayslipTableView();
            }
        });
    }


    @FXML
    public void viewLeaveBalanceOnAction() {
        mainViewController.rewriteLabel("/ Employee / View Leave Balance");

        loadView("/com/jomariabejo/motorph/role/employee/leave-balance-view.fxml", controller -> {

            if (controller instanceof LeaveBalanceController viewLeaveBalance) {
                viewLeaveBalance.setEmployeeRoleNavigationController(this);
                viewLeaveBalance.populatePieChart();
            }
        });
    }

    @FXML
    public void viewLeaveHistoryOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Leave History");

        loadView("/com/jomariabejo/motorph/role/employee/leave-history-view.fxml", controller -> {

            if (controller instanceof LeaveHistoryController viewLeaveHistory) {
                viewLeaveHistory.setEmployeeRoleNavigationController(this);
                viewLeaveHistory.setup();
            }
        });
    }

    @FXML
    public void viewTimesheetOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Timesheet");

        loadView("/com/jomariabejo/motorph/role/employee/timesheet-view.fxml", controller -> {

            if (controller instanceof TimesheetController viewTimesheetController) {
                viewTimesheetController.setEmployeeRoleNavigationController(this);
                viewTimesheetController.populateMonths();
                viewTimesheetController.populateYears();
                viewTimesheetController.populateTableview();
            }
        });
    }

    public void reimbursementOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / Reimbursement");

        loadView("/com/jomariabejo/motorph/role/employee/reimbursement.fxml", controller -> {

            if (controller instanceof ReimbursementController reimbursementController) {
                reimbursementController.setEmployeeRoleNavigationController(this);
                reimbursementController.setup();
            }
        });
    }

    @FXML
    public void notificationOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / Notifications");

        loadView("/com/jomariabejo/motorph/role/employee/notification.fxml", controller -> {
            if (controller instanceof NotificationController notificationController) {
                notificationController.setEmployeeRoleNavigationController(this);
                notificationController.setup();
            }
        });
    }

    @FXML
    public void conversationOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / Conversations");

        loadView("/com/jomariabejo/motorph/role/employee/conversation.fxml", controller -> {
            if (controller instanceof ConversationController conversationController) {
                conversationController.setEmployeeRoleNavigationController(this);
                conversationController.setup();
            }
        });
    }

    @Override
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

    public void displayWelcome() {
        String[] welcomeMessage = {"Hi 👋","Hello 🙌","Welcome Back ❤"};


        lblWelcome.setText(welcomeMessage[(int) (Math.random() * welcomeMessage.length)]);
        lblFirstName.setText(mainViewController.getEmployee().getFirstName());
    }

    /**
     * Show back button when Employee navigation is accessed from another role
     */
    public void showBackButton(Object previousNavigationController) {
        this.previousNavigationController = previousNavigationController;
        if (btnBackToPreviousRole != null) {
            String buttonText = getBackButtonText(previousNavigationController);
            btnBackToPreviousRole.setText(buttonText);
            btnBackToPreviousRole.setVisible(true);
            btnBackToPreviousRole.setManaged(true);
        }
    }

    /**
     * Hide back button when Employee navigation is the primary role
     */
    public void hideBackButton() {
        this.previousNavigationController = null;
        if (btnBackToPreviousRole != null) {
            btnBackToPreviousRole.setVisible(false);
            btnBackToPreviousRole.setManaged(false);
        }
    }

    /**
     * Get appropriate button text based on previous navigation type
     */
    private String getBackButtonText(Object previousNavigationController) {
        if (previousNavigationController instanceof SystemAdministratorNavigationController) {
            return "← Back to System Admin";
        } else if (previousNavigationController instanceof HumanResourceAdministratorNavigationController) {
            return "← Back to Human Resource";
        } else if (previousNavigationController instanceof PayrollAdministratorNavigationController) {
            return "← Back to Accounting";
        } else {
            return "← Back to Dashboard";
        }
    }

    /**
     * Navigate back to previous role navigation
     */
    @FXML
    public void navigateBackToPreviousRole() {
        if (mainViewController != null && previousNavigationController != null) {
            mainViewController.navigateBackToPreviousRole();
        }
    }

    /**
     * Update button visibility based on user permissions.
     * Hides buttons that the user doesn't have permission to access.
     */
    public void updateButtonVisibility() {
        // Check if buttons are initialized
        if (btnMyProfile == null) {
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

        // Update visibility based on permissions
        // Always set managed first, then visible to ensure synchronization
        if (btnMyProfile != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_PROFILE_VIEW, serviceFactory);
            btnMyProfile.setManaged(visible);
            btnMyProfile.setVisible(visible);
        }

        if (btnFileLeaveRequest != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_LEAVE_REQUEST, serviceFactory);
            btnFileLeaveRequest.setManaged(visible);
            btnFileLeaveRequest.setVisible(visible);
        }

        if (btnViewLeaveBalance != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_LEAVE_BALANCE_VIEW, serviceFactory);
            btnViewLeaveBalance.setManaged(visible);
            btnViewLeaveBalance.setVisible(visible);
        }

        if (btnViewLeaveHistory != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_LEAVE_HISTORY_VIEW, serviceFactory);
            btnViewLeaveHistory.setManaged(visible);
            btnViewLeaveHistory.setVisible(visible);
        }

        if (btnTimesheet != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_TIMESHEET_VIEW, serviceFactory);
            btnTimesheet.setManaged(visible);
            btnTimesheet.setVisible(visible);
        }

        if (btnOvertime != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_OVERTIME_REQUEST, serviceFactory);
            btnOvertime.setManaged(visible);
            btnOvertime.setVisible(visible);
        }

        if (btnPayslip != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_PAYSLIP_VIEW, serviceFactory);
            btnPayslip.setManaged(visible);
            btnPayslip.setVisible(visible);
        }

        if (btnReimbursement != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_REIMBURSEMENT_REQUEST, serviceFactory);
            btnReimbursement.setManaged(visible);
            btnReimbursement.setVisible(visible);
        }

        if (btnContactSupport != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_CONTACT_SUPPORT, serviceFactory);
            btnContactSupport.setManaged(visible);
            btnContactSupport.setVisible(visible);
        }

        if (btnNotification != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_NOTIFICATIONS_VIEW, serviceFactory);
            btnNotification.setManaged(visible);
            btnNotification.setVisible(visible);
        }

        if (btnConversation != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.EMPLOYEE_MESSAGES_VIEW, serviceFactory);
            btnConversation.setManaged(visible);
            btnConversation.setVisible(visible);
        }
    }

    private void setAllButtonsVisible(boolean visible) {
        // Ensure visible and managed are always synchronized - set managed first
        if (btnMyProfile != null) {
            btnMyProfile.setManaged(visible);
            btnMyProfile.setVisible(visible);
        }
        if (btnFileLeaveRequest != null) {
            btnFileLeaveRequest.setManaged(visible);
            btnFileLeaveRequest.setVisible(visible);
        }
        if (btnViewLeaveBalance != null) {
            btnViewLeaveBalance.setManaged(visible);
            btnViewLeaveBalance.setVisible(visible);
        }
        if (btnViewLeaveHistory != null) {
            btnViewLeaveHistory.setManaged(visible);
            btnViewLeaveHistory.setVisible(visible);
        }
        if (btnTimesheet != null) {
            btnTimesheet.setManaged(visible);
            btnTimesheet.setVisible(visible);
        }
        if (btnOvertime != null) {
            btnOvertime.setManaged(visible);
            btnOvertime.setVisible(visible);
        }
        if (btnPayslip != null) {
            btnPayslip.setManaged(visible);
            btnPayslip.setVisible(visible);
        }
        if (btnReimbursement != null) {
            btnReimbursement.setManaged(visible);
            btnReimbursement.setVisible(visible);
        }
        if (btnContactSupport != null) {
            btnContactSupport.setManaged(visible);
            btnContactSupport.setVisible(visible);
        }
        if (btnNotification != null) {
            btnNotification.setManaged(visible);
            btnNotification.setVisible(visible);
        }
        if (btnConversation != null) {
            btnConversation.setManaged(visible);
            btnConversation.setVisible(visible);
        }
    }
}
