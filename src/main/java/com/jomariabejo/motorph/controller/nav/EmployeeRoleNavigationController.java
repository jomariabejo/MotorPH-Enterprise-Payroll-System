package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

public class EmployeeRoleNavigationController {

    @Getter
    @Setter
    private MainViewController mainViewController;

    @FXML
    void contactSupportOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / Contact Support");
    }

    public void fileLeaveRequestOnAction(ActionEvent actionEvent) {
        this.mainViewController.rewriteLabel("/ Employee / File Leave Request");
    }

    @FXML
    void overtimeOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / Overtime");
    }

    @FXML
    void overviewOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / Overview");
    }

    @FXML
    void payslipOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / Payslip");
    }

    @FXML
    void submitTimesheetOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / Submit Timesheet");
    }

    @FXML
    void viewLeaveBalanceOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / View Leave Balance");
    }

    @FXML
    void viewLeaveHistoryOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / View Leave History");
    }

    @FXML
    void viewTimesheetOnAction(ActionEvent event) {
        this.mainViewController.rewriteLabel("/ Employee / View Timesheet");
    }

    // Default constructor
    public EmployeeRoleNavigationController() {
        // Required by FXMLLoader
    }

    public EmployeeRoleNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void showHi() {
        System.out.println("Hello");
    }
}
