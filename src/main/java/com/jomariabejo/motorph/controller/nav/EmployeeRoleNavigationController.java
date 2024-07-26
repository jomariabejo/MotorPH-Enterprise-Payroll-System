package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller.role.employee.EmployeeOverview;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeRoleNavigationController {

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
    }

    @FXML
    public void fileLeaveRequestOnAction() {
        mainViewController.rewriteLabel("/ Employee / File Leave Request");
    }

    @FXML
    public void overtimeOnAction() {
        mainViewController.rewriteLabel("/ Employee / Overtime");
    }

    @FXML
    public void overviewOnAction() {
        mainViewController.rewriteLabel("/ Employee / Overview");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jomariabejo/motorph/role/employee/dashboard.fxml"));
            AnchorPane overviewAnchorPane = loader.load();
            mainViewController.getMainBorderPane().setCenter(overviewAnchorPane);

            EmployeeOverview employeeOverview= loader.getController();
            employeeOverview.setEmployeeRoleNavigationController(this);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    @FXML
    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Employee / Payslip");
    }

    @FXML
    public void submitTimesheetOnAction() {
        mainViewController.rewriteLabel("/ Employee / Submit Timesheet");
    }

    @FXML
    public void viewLeaveBalanceOnAction() {
        mainViewController.rewriteLabel("/ Employee / View Leave Balance");
    }

    @FXML
    public void viewLeaveHistoryOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Leave History");
    }

    @FXML
    public void viewTimesheetOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / View Timesheet");
    }

    public void reimbursementOnAction() {
        this.mainViewController.rewriteLabel("/ Employee / Reimbursement");
    }
}
