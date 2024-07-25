package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import lombok.Getter;
import lombok.Setter;

public class PayrollAdministratorNavigationController {

    @Getter
    @Setter
    private MainViewController mainViewController;

    public PayrollAdministratorNavigationController() {
    }

    public PayrollAdministratorNavigationController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void dashboardOnActtion() {
        mainViewController.rewriteLabel("/ Finance / Dashboard");
    }

    public void payrollOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll");
    }

    public void payrollApprovalOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll Approval");
    }

    public void payrollTransactionOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll Transaction");
    }

    public void overtimeOnAction() {
        mainViewController.rewriteLabel("/ Finance / Overtime");
    }

    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payslip");
    }

    public void payslipHistoryOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payslip History");
    }
}
