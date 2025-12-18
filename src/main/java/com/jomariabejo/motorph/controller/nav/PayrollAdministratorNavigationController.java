package com.jomariabejo.motorph.controller.nav;

import com.jomariabejo.motorph.constants.PermissionConstants;
import com.jomariabejo.motorph.controller.MainViewController;
import com.jomariabejo.motorph.controller._ViewLoader;
import com.jomariabejo.motorph.controller.role.accounting.*;
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
public class PayrollAdministratorNavigationController implements _ViewLoader {

    private MainViewController mainViewController;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnPayrollTransaction;

    @FXML
    private Button btnOvertime;

    @FXML
    private Button btnPayslip;

    @FXML
    private Button btnBonus;

    @FXML
    private Button btnPagibigRate;

    @FXML
    private Button btnPhilhealthRate;

    @FXML
    private Button btnSssRate;

    @FXML
    private Button btnReimbursement;

    @FXML
    private Button btnReimbursementTransaction;

    public PayrollAdministratorNavigationController() {
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

    public void dashboardOnActtion() {
        if (mainViewController == null) {
            return;
        }
        mainViewController.rewriteLabel("/ Finance / Dashboard");

        loadView("/com/jomariabejo/motorph/role/accounting/accounting-dashboard.fxml", controller -> {

            if (controller instanceof AccountingDashboard accountingDashboard) {
                accountingDashboard.setPayrollAdministratorNavigationController(this);
                accountingDashboard.loadData();
                accountingDashboard.populateKPICards();
                accountingDashboard.populateCharts();
            }
        });
    }

    public void payrollOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payroll");

        loadView("/com/jomariabejo/motorph/role/accounting/payroll.fxml", controller -> {

            if (controller instanceof PayrollController payrollController) {
                payrollController.setPayrollAdministratorNavigationController(this);
                payrollController.populatePayrolls();
            }
        });
    }

    // Payroll Approval functionality has been integrated into the Payroll view
    // Removed separate navigation button as it was redundant

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
                overtimeApprovalController.populateOvertimeRequests();
            }
        });
    }

    public void payslipOnAction() {
        mainViewController.rewriteLabel("/ Finance / Payslip");

        loadView("/com/jomariabejo/motorph/role/accounting/payslip.fxml", controller -> {

            if (controller instanceof PayslipController payslipController) {
                payslipController.setPayrollAdministratorNavigationController(this);
                payslipController.populatePayslips();
            }
        });
    }

    // Payslip History button removed - functionality can be accessed from Payslip view if needed

    public void bonusOnAction() {
        mainViewController.rewriteLabel("/ Finance / Bonus");

        loadView("/com/jomariabejo/motorph/role/accounting/bonus.fxml", controller -> {
            if (controller instanceof BonusController bonusController) {
                bonusController.setPayrollAdministratorNavigationController(this);
                bonusController.populateBonuses();
            }
        });
    }

    // TIN Compliance button removed

    public void pagibigRateOnAction() {
        mainViewController.rewriteLabel("/ Finance / Pagibig Contribution Rate");

        loadView("/com/jomariabejo/motorph/role/accounting/pagibig-rate.fxml", controller -> {
            if (controller instanceof PagibigContributionRateController rateController) {
                rateController.setPayrollAdministratorNavigationController(this);
                rateController.populateRates();
            }
        });
    }

    public void philhealthRateOnAction() {
        mainViewController.rewriteLabel("/ Finance / Philhealth Contribution Rate");

        loadView("/com/jomariabejo/motorph/role/accounting/philhealth-rate.fxml", controller -> {
            if (controller instanceof PhilhealthContributionRateController rateController) {
                rateController.setPayrollAdministratorNavigationController(this);
                rateController.populateRates();
            }
        });
    }

    public void sssRateOnAction() {
        mainViewController.rewriteLabel("/ Finance / SSS Contribution Rate");

        loadView("/com/jomariabejo/motorph/role/accounting/sss-rate.fxml", controller -> {
            if (controller instanceof SssContributionRateController rateController) {
                rateController.setPayrollAdministratorNavigationController(this);
                rateController.populateRates();
            }
        });
    }

    // Payroll Changes button removed

    public void reimbursementOnAction() {
        mainViewController.rewriteLabel("/ Finance / Reimbursement");

        loadView("/com/jomariabejo/motorph/role/accounting/reimbursement.fxml", controller -> {
            if (controller instanceof ReimbursementController reimbursementController) {
                reimbursementController.setPayrollAdministratorNavigationController(this);
                reimbursementController.populateRequests();
            }
        });
    }

    public void reimbursementTransactionOnAction() {
        mainViewController.rewriteLabel("/ Finance / Reimbursement Transactions");

        loadView("/com/jomariabejo/motorph/role/accounting/reimbursement-transaction.fxml", controller -> {
            if (controller instanceof ReimbursementTransactionController transactionController) {
                transactionController.setPayrollAdministratorNavigationController(this);
                transactionController.populateTransactions();
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

        // Check if user is Payroll Administrator - if so, show all buttons
        boolean isPayrollAdmin = user.getRoleID() != null && 
                                user.getRoleID().getRoleName() != null &&
                                "Payroll Administrator".equalsIgnoreCase(user.getRoleID().getRoleName());
        
        // If Payroll admin, show all buttons
        if (isPayrollAdmin) {
            setAllButtonsVisible(true);
            return;
        }

        // Otherwise, check individual permissions
        // Always set managed first, then visible to ensure synchronization
        if (btnDashboard != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_DASHBOARD_VIEW, serviceFactory);
            btnDashboard.setManaged(visible);
            btnDashboard.setVisible(visible);
        }

        if (btnPayroll != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_MANAGE, serviceFactory);
            btnPayroll.setManaged(visible);
            btnPayroll.setVisible(visible);
        }

        // Payroll Approval button removed - functionality integrated into Payroll view

        if (btnPayrollTransaction != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_TRANSACTIONS_VIEW, serviceFactory);
            btnPayrollTransaction.setManaged(visible);
            btnPayrollTransaction.setVisible(visible);
        }

        if (btnOvertime != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_OVERTIME_APPROVE, serviceFactory);
            btnOvertime.setManaged(visible);
            btnOvertime.setVisible(visible);
        }

        if (btnPayslip != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_PAYSLIP_VIEW, serviceFactory);
            btnPayslip.setManaged(visible);
            btnPayslip.setVisible(visible);
        }

        // Payslip History button removed

        if (btnBonus != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_BONUS_MANAGE, serviceFactory);
            btnBonus.setManaged(visible);
            btnBonus.setVisible(visible);
        }

        // TIN Compliance button removed

        if (btnPagibigRate != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_PAGIBIG_RATES_MANAGE, serviceFactory);
            btnPagibigRate.setManaged(visible);
            btnPagibigRate.setVisible(visible);
        }

        if (btnPhilhealthRate != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_PHILHEALTH_RATES_MANAGE, serviceFactory);
            btnPhilhealthRate.setManaged(visible);
            btnPhilhealthRate.setVisible(visible);
        }

        if (btnSssRate != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_SSS_RATES_MANAGE, serviceFactory);
            btnSssRate.setManaged(visible);
            btnSssRate.setVisible(visible);
        }

        // Payroll Changes button removed

        if (btnReimbursement != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_REIMBURSEMENT_MANAGE, serviceFactory);
            btnReimbursement.setManaged(visible);
            btnReimbursement.setVisible(visible);
        }

        if (btnReimbursementTransaction != null) {
            boolean visible = PermissionChecker.hasPermission(user, PermissionConstants.PAYROLL_REIMBURSEMENT_TRANSACTIONS_VIEW, serviceFactory);
            btnReimbursementTransaction.setManaged(visible);
            btnReimbursementTransaction.setVisible(visible);
        }
    }

    private void setAllButtonsVisible(boolean visible) {
        // Ensure visible and managed are always synchronized - set managed first
        if (btnDashboard != null) {
            btnDashboard.setManaged(visible);
            btnDashboard.setVisible(visible);
        }
        if (btnPayroll != null) {
            btnPayroll.setManaged(visible);
            btnPayroll.setVisible(visible);
        }
        // Payroll Approval button removed - functionality integrated into Payroll view
        if (btnPayrollTransaction != null) {
            btnPayrollTransaction.setManaged(visible);
            btnPayrollTransaction.setVisible(visible);
        }
        if (btnOvertime != null) {
            btnOvertime.setManaged(visible);
            btnOvertime.setVisible(visible);
        }
        if (btnPayslip != null) {
            btnPayslip.setManaged(visible);
            btnPayslip.setVisible(visible);
        }
        // Payslip History button removed
        if (btnBonus != null) {
            btnBonus.setManaged(visible);
            btnBonus.setVisible(visible);
        }
        // TIN Compliance button removed
        if (btnPagibigRate != null) {
            btnPagibigRate.setManaged(visible);
            btnPagibigRate.setVisible(visible);
        }
        if (btnPhilhealthRate != null) {
            btnPhilhealthRate.setManaged(visible);
            btnPhilhealthRate.setVisible(visible);
        }
        if (btnSssRate != null) {
            btnSssRate.setManaged(visible);
            btnSssRate.setVisible(visible);
        }
        // Payroll Changes button removed
        if (btnReimbursement != null) {
            btnReimbursement.setManaged(visible);
            btnReimbursement.setVisible(visible);
        }
        if (btnReimbursementTransaction != null) {
            btnReimbursementTransaction.setManaged(visible);
            btnReimbursementTransaction.setVisible(visible);
        }
    }
}
