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

public class EmployeeRoleNavigationController implements _ViewLoader {

    @Getter
    @Setter
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
            if (controller instanceof ContactSupportController) {
                ContactSupportController contactSupportController = (ContactSupportController) controller;
                contactSupportController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void fileLeaveRequestOnAction() {
        mainViewController.rewriteLabel("/ Employee / File Leave Request");

        loadView("/com/jomariabejo/motorph/role/employee/file-leave-request.fxml", controller -> {
            if (controller instanceof FileLeaveRequestController) {
                FileLeaveRequestController fileLeaveRequest = (FileLeaveRequestController) controller;
                fileLeaveRequest.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void overtimeOnAction() {
        mainViewController.rewriteLabel("/ Employee / Overtime");

        loadView("/com/jomariabejo/motorph/role/employee/overtime.fxml", controller -> {
            if (controller instanceof OvertimeController) {
                OvertimeController overtimeController = (OvertimeController) controller;
                overtimeController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void overviewOnAction() {
        mainViewController.rewriteLabel("/ Employee / Overview");
        loadView("/com/jomariabejo/motorph/role/employee/dashboard.fxml", controller -> {
            if (controller instanceof EmployeeOverviewController) {
                EmployeeOverviewController employeeOverviewController = (EmployeeOverviewController) controller;
                employeeOverviewController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Employee / Payslip");

        loadView("/com/jomariabejo/motorph/role/employee/payslip.fxml", controller -> {
            if (controller instanceof PayslipController) {
                PayslipController payslipController = (PayslipController) controller;
                payslipController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void submitTimesheetOnAction() {
        mainViewController.rewriteLabel("/ Employee / Submit Timesheet");

        loadView("/com/jomariabejo/motorph/role/employee/payslip.fxml", controller -> {
            if (controller instanceof PayslipController) {
                PayslipController payslipController = (PayslipController) controller;
                payslipController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void viewLeaveBalanceOnAction() {
        mainViewController.rewriteLabel("/ Employee / View Leave Balance");

        loadView("/com/jomariabejo/motorph/role/employee/view-leave-balance.fxml", controller -> {

            if (controller instanceof ViewLeaveBalanceController) {
                ViewLeaveBalanceController viewLeaveBalance = (ViewLeaveBalanceController) controller;
                viewLeaveBalance.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void viewLeaveHistoryOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Leave History");

        loadView("/com/jomariabejo/motorph/role/employee/view-leave-history.fxml", controller -> {

            if (controller instanceof ViewLeaveHistoryController) {
                ViewLeaveHistoryController viewLeaveHistory = (ViewLeaveHistoryController) controller;
                viewLeaveHistory.setEmployeeRoleNavigationController(this);
            }
        });
    }

    @FXML
    public void viewTimesheetOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Timesheet");

        loadView("/com/jomariabejo/motorph/role/employee/view-timesheet.fxml", controller -> {

            if (controller instanceof ViewTimesheetController) {
                ViewTimesheetController viewTimesheetController = (ViewTimesheetController) controller;
                viewTimesheetController.setEmployeeRoleNavigationController(this);
            }
        });
    }

    public void reimbursementOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / Reimbursement");

        loadView("/com/jomariabejo/motorph/role/employee/reimbursement.fxml", controller -> {

            if (controller instanceof ReimbursementController) {
                ReimbursementController reimbursementController = (ReimbursementController) controller;
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
