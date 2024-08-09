package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.employee.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Setter
@Getter
public class EmployeeRoleNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    // Default constructor
    public EmployeeRoleNavigationController() {
        // Required by FXMLLoader
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
            }
        });
    }

    @FXML
    public void myProfileOnAction() {
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
            }
        });
    }


    @Override
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
