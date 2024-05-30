package com.jomariabejo.motorph.controller.personalinformation;


import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.service.PayslipService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ViewMyPayslipController {

    private PayslipService payslipService;

    public ViewMyPayslipController() {
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
    void exit_button_event(ActionEvent event) {
        tf_employee_id.getScene().getWindow().hide();
    }

    public void initPayslipId(int payslipId) throws SQLException {
        Payslip.PayslipViewer payslipViewer = payslipService.fetchPayslip(payslipId);

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

}
