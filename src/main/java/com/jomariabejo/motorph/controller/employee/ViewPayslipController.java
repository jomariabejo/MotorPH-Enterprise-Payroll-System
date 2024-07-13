package com.jomariabejo.motorph.controller.employee;


import com.jomariabejo.motorph.entity.Allowance;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.service.*;
import com.jomariabejo.motorph.utility.AlertUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class ViewPayslipController {

    private PayslipService payslipService;

    public ViewPayslipController() {
        this.payslipService = new PayslipService();
    }

    @FXML
    private TextField tf_clothing_allowance;

    @FXML
    private Label tf_employee_id;

    @FXML
    private Label tf_employee_name;

    @FXML
    private Label tf_employee_position;

    @FXML
    private TextField tf_gross_income;

    @FXML
    private TextField tf_hourly_rate;

    @FXML
    private TextField tf_monthly_rate;

    @FXML
    private TextField tf_overtime_hours_worked;

    @FXML
    private TextField tf_pagibig;

    @FXML
    private Label tf_payslip_number;

    @FXML
    private Label tf_period_end_date;

    @FXML
    private Label tf_period_start_date;

    @FXML
    private TextField tf_philhealth;

    @FXML
    private TextField tf_phone_allowance;

    @FXML
    private TextField tf_regular_hours_worked;

    @FXML
    private TextField tf_rice_subsidy;

    @FXML
    private TextField tf_social_security_system;

    @FXML
    private TextField tf_summary_benefits;

    @FXML
    private TextField tf_summary_deductions;

    @FXML
    private TextField tf_summary_gross_income;

    @FXML
    private TextField tf_take_home_pay;

    @FXML
    private TextField tf_total_benefits;

    @FXML
    private TextField tf_total_deductions;

    @FXML
    private TextField tf_withhold_tax;


    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @FXML
    void exit_button_event(ActionEvent event) {
        tf_employee_id.getScene().getWindow().hide();
    }

    @FXML
    private void initialize() {
        hideButtons();
    }

    public void initPayslipId(int payslipId) throws SQLException {
        Payslip.PayslipViewer payslipViewer = payslipService.fetchPayslipBreakdown(payslipId);

        // Set text on labels
        tf_employee_id.setText(String.valueOf(payslipViewer.employeeId()));
        tf_employee_name.setText(payslipViewer.employeeName());
        tf_employee_position.setText(payslipViewer.positionName());
        tf_payslip_number.setText(String.valueOf(payslipId));
        tf_period_start_date.setText(payslipViewer.payPeriodStart().toString());
        tf_period_end_date.setText(payslipViewer.payPeriodEnd().toString());

        // Set text on text fields
        tf_gross_income.setText("Php " + payslipViewer.grossIncome());
        tf_hourly_rate.setText("Php " + payslipViewer.hourlyRate());
        tf_monthly_rate.setText("Php " + payslipViewer.monthlyRate());
        tf_overtime_hours_worked.setText(String.valueOf(payslipViewer.totalOvertimeHoursWorked()));
        tf_pagibig.setText("Php " + payslipViewer.pagibig());
        tf_philhealth.setText("Php " + payslipViewer.philhealth());
        tf_phone_allowance.setText("Php " + payslipViewer.phone());
        tf_regular_hours_worked.setText(String.valueOf(payslipViewer.totalRegularHoursWorked()));
        tf_rice_subsidy.setText("Php " + payslipViewer.rice());
        tf_social_security_system.setText("Php " + payslipViewer.sss());
        tf_clothing_allowance.setText("Php " + payslipViewer.clothing());
        tf_summary_benefits.setText("Php " + payslipViewer.totalBenefits());
        tf_summary_deductions.setText("Php " + payslipViewer.totalDeduction());
        tf_summary_gross_income.setText("Php " + payslipViewer.grossIncome());
        tf_take_home_pay.setText("Php " + payslipViewer.takeHomePay());
        tf_total_benefits.setText("Php " + payslipViewer.totalBenefits());
        tf_total_deductions.setText("Php " + payslipViewer.totalDeduction());
        tf_withhold_tax.setText("Php " + payslipViewer.withheldTax());
    }

    @FXML
    public void cancelClicked(ActionEvent actionEvent) {
        hideThisWindow();
    }

    @FXML
    public void saveClciked(ActionEvent actionEvent) {
        boolean isPayslipUpdateConfirmed =
                AlertUtility.showConfirmation("Payslip modification confirmation.",
                        "Are you sure you want to modify this payslip?",
                        "Once submitted, you can't turn it back to its original values.");

        if (isPayslipUpdateConfirmed) {
            executePayslipModification();
        } else {
            hideThisWindow();
        }
    }

    private void hideThisWindow() {
        this.tf_employee_id.getScene().getWindow().hide();
    }

    private void executePayslipModification() {
        modifyDeduction();
        modifyTax();
        modifyPayslip();
        modifyAllowance();
    }

    private void modifyAllowance() {
        AllowanceService allowanceService = new AllowanceService();
        Allowance allowance = new Allowance();
        allowance.setEmployeeID(Integer.parseInt(this.tf_employee_id.getText()));
        allowance.setDateModified(Timestamp.from(Instant.now()));
        allowance.setRiceAllowance((int) Double.parseDouble(this.tf_rice_subsidy.getText().replace("Php ", "")));
        allowance.setPhoneAllowance((int) Double.parseDouble(this.tf_phone_allowance.getText().replace("Php ", "")));
        allowance.setClothingAllowance((int) Double.parseDouble(this.tf_clothing_allowance.getText().replace("Php ", "")));
        int totalAmount = allowance.getRiceAllowance() + allowance.getPhoneAllowance() + allowance.getClothingAllowance();
        allowance.setTotalAmount(totalAmount);
        allowanceService.updateAllowance(allowance, Integer.parseInt(this.tf_employee_id.getText()));
    }

    private void modifyTax() {
        TaxService taxService = new TaxService();
        TaxCategoryService taxCategoryService = new TaxCategoryService();


        Tax tax = new Tax();
        tax.setWithheldTax(BigDecimal.valueOf(Float.parseFloat(this.tf_withhold_tax.getText().replace("Php ", ""))));
        tax.setTaxCategoryId(taxCategoryService.fetchTaxCategoryIdByMonthlyRate(BigDecimal.valueOf(Float.parseFloat(this.tf_monthly_rate.getText().replace("Php ", "")))));

        tax.setTaxableIncome(
                taxService.computeTaxableIncome(
                        BigDecimal.valueOf(Float.parseFloat(this.tf_gross_income.getText().replace("Php ", ""))),
                        BigDecimal.valueOf(Float.parseFloat(this.tf_summary_deductions.getText().replace("Php ", ""))))
        );

        int payslipId = Integer.parseInt(this.tf_payslip_number.getText());
        taxService.modifyTaxByPayslipId(tax, payslipId);
    }

    private void modifyDeduction() {
        DeductionService deductionService = new DeductionService();

        Deduction deduction = new Deduction();
        deduction.setPagibig(BigDecimal.valueOf(Float.parseFloat(this.tf_pagibig.getText().replace("Php ", ""))));
        deduction.setPhilhealth(BigDecimal.valueOf(Float.parseFloat(this.tf_philhealth.getText().replace("Php ", ""))));
        deduction.setSss(BigDecimal.valueOf(Float.parseFloat(this.tf_social_security_system.getText().replace("Php ", ""))));

        deductionService.modifyDeduction(deduction, Integer.parseInt(tf_payslip_number.getText()));
    }

    private void modifyPayslip() {
        Payslip payslip = new Payslip();
        payslip.setPayslipID(Integer.parseInt(tf_payslip_number.getText()));

        BigDecimal regularHoursWorked = BigDecimal.valueOf(Float.parseFloat(this.tf_regular_hours_worked.getText()));
        BigDecimal overtimeHoursWorked = BigDecimal.valueOf(Float.parseFloat(this.tf_overtime_hours_worked.getText()));
        BigDecimal totalHoursWorked = regularHoursWorked.add(overtimeHoursWorked);
        payslip.setTotalHoursWorked(totalHoursWorked);

        payslip.setGrossIncome(BigDecimal.valueOf(Float.valueOf(tf_gross_income.getText().replace("Php ", ""))));

        payslip.setNetIncome(
                payslip.getGrossIncome()
                        .add
                                (
                                        BigDecimal.valueOf(
                                                Float.valueOf(this.tf_total_benefits.getText()
                                                        .replace("Php ", "")))
                                )
                        .subtract
                                (
                                        BigDecimal.valueOf(
                                                Float.parseFloat(this.tf_total_deductions.getText()
                                                        .replace("Php ", ""))))
        );

        this.payslipService.modifyPayslip(payslip);
    }


    private void hideButtons() {
        submitButton.setVisible(false);
        submitButton.setManaged(false);

        cancelButton.setVisible(false);
        cancelButton.setManaged(false);
    }

    public void displayButtons() {
        submitButton.setVisible(true);
        submitButton.setManaged(true);

        cancelButton.setVisible(true);
        cancelButton.setManaged(true);
    }
}
