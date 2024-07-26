package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.accounting.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.function.Consumer;

@Setter
@Getter
public class PayrollAdministratorNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    public PayrollAdministratorNavigationController() {
    }

    public void dashboardOnActtion() {
        mainViewController.rewriteLabel("/ Finance / Dashboard");

        loadView("/com/jomariabejo/motorph/role/accounting/accounting-dashboard.fxml", controller -> {

            if (controller instanceof AccountingDashboard accountingDashboard) {
                accountingDashboard.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void payrollOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll");

        loadView("/com/jomariabejo/motorph/role/accounting/payroll.fxml", controller -> {

            if (controller instanceof PayrollController payrollController) {
                payrollController.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void payrollApprovalOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll Approval");

        loadView("/com/jomariabejo/motorph/role/accounting/payroll-approval.fxml", controller -> {

            if (controller instanceof PayrollApprovalController payrollApprovalController) {
                payrollApprovalController.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void payrollTransactionOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll Transaction");

        loadView("/com/jomariabejo/motorph/role/accounting/payroll-transaction.fxml", controller -> {

            if (controller instanceof PayrollTransactionController payrollTransactionController) {
                payrollTransactionController.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void overtimeOnAction() {
        mainViewController.rewriteLabel("/ Finance / Overtime");

        loadView("/com/jomariabejo/motorph/role/accounting/overtime-approval.fxml", controller -> {

            if (controller instanceof OvertimeApprovalController overtimeApprovalController) {
                overtimeApprovalController.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payslip");

        loadView("/com/jomariabejo/motorph/role/accounting/payslip.fxml", controller -> {

            if (controller instanceof PayslipController payslipController) {
                payslipController.setPayrollAdministratorNavigationController(this);
            }
        });
    }

    public void payslipHistoryOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payslip History");

        loadView("/com/jomariabejo/motorph/role/accounting/payslip-history.fxml", controller -> {

            if (controller instanceof PayslipHistoryController payslipHistoryController) {
                payslipHistoryController.setPayrollAdministratorNavigationController(this);
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
