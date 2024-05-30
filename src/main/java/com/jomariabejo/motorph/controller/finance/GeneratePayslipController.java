package com.jomariabejo.motorph.controller.finance;

import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.record.AccountNumber;
import com.jomariabejo.motorph.record.GrossIncome;
import com.jomariabejo.motorph.service.*;
import com.jomariabejo.motorph.utility.AlertUtility;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class GeneratePayslipController {
    private final TimesheetService timesheetService;
    private final EmployeeService employeeService;
    private final AllowanceService allowanceService;
    private final DeductionService deductionService;
    private final TaxService taxService;
    private final PayslipService payslipService;


    public GeneratePayslipController() {
        timesheetService = new TimesheetService();
        employeeService = new EmployeeService();
        allowanceService = new AllowanceService();
        deductionService = new DeductionService();
        taxService = new TaxService();
        payslipService = new PayslipService();
    }

    @FXML
    private TableView<EmployeePayrollSummaryReport> tv_generated_payroll;

    @FXML
    private DatePicker dp_endPayDate;

    @FXML
    private DatePicker dp_startPayDate;

    @FXML
    private Button executeButton;

    @FXML
    private Pagination pagination;

    @FXML
    void executeClicked(ActionEvent event) throws SQLException {
        if (executeButton.getText().equals("Execute")) {

            ArrayList<EmployeePayrollSummaryReport> employeePayrollSummaryReports = new ArrayList<>();

            ArrayList<GrossIncome> GROSS_INCOMES = timesheetService.fetchGrossIncome(Date.valueOf(dp_startPayDate.getValue()), Date.valueOf(dp_endPayDate.getValue()));

            /**
             * Compute deductions based on gross income
             */
            for (int i = 0; i < GROSS_INCOMES.size(); i++) {
                Deduction deduction = new Deduction();
                deduction.setEmployeeID(GROSS_INCOMES.get(i).employeeId());
                deduction.setSss(deductionService.deductSSS(GROSS_INCOMES.get(i).computeGrossIncome()));
                deduction.setPhilhealth(deductionService.deductPhilhealth(GROSS_INCOMES.get(i).basicSalary()));
                deduction.setPagibig(deductionService.deductPagIbig(GROSS_INCOMES.get(i).computeGrossIncome()));

                Tax tax = new Tax();

                tax.setWithheldTax(
                        taxService.computeTax(
                            GROSS_INCOMES.get(i).basicSalary(),
                            GROSS_INCOMES.get(i).computeGrossIncome(),
                            deduction));

                tax.setTaxableIncome(GROSS_INCOMES.get(i).computeGrossIncome().subtract(deduction.totalContributions()));
                AccountNumber accountNumber = employeeService.fetchEmployeeAccountNumber(GROSS_INCOMES.get(i).employeeId());
                BigDecimal totalDeduction = deduction.totalContributions().add(tax.getWithheldTax());
                BigDecimal netPay = GROSS_INCOMES.get(i).computeGrossIncome().subtract(totalDeduction).add(new BigDecimal(GROSS_INCOMES.get(i).total_allowance()));
                EmployeePayrollSummaryReport employeePayrollSummaryReport = new EmployeePayrollSummaryReport();
                employeePayrollSummaryReport.setEmployeeNumber(GROSS_INCOMES.get(i).employeeId());
                employeePayrollSummaryReport.setStartPayDate(Date.valueOf(dp_startPayDate.getValue()));
                employeePayrollSummaryReport.setEndPayDate(Date.valueOf(dp_endPayDate.getValue()));
                employeePayrollSummaryReport.setEmployeeFullName(GROSS_INCOMES.get(i).employeeName());
                employeePayrollSummaryReport.setPosition(GROSS_INCOMES.get(i).position());
                employeePayrollSummaryReport.setDepartment(GROSS_INCOMES.get(i).department());
                employeePayrollSummaryReport.setSocialSecurityNumber(accountNumber.sss());
                employeePayrollSummaryReport.setPhilhealthNumber(accountNumber.philhealth());
                employeePayrollSummaryReport.setPagIbigNumber(accountNumber.pagibig());
                employeePayrollSummaryReport.setTinNumber(accountNumber.tin());
                employeePayrollSummaryReport.setMonthlyRate(GROSS_INCOMES.get(i).basicSalary());
                employeePayrollSummaryReport.setHourlyRate(GROSS_INCOMES.get(i).hourslyRate());
                employeePayrollSummaryReport.setTotalRegularHoursWorked(GROSS_INCOMES.get(i).total_regular_hours_worked());
                employeePayrollSummaryReport.setTotalOvertimeHoursWorked(GROSS_INCOMES.get(i).total_overtime_hours());
                employeePayrollSummaryReport.setRiceSubsidy(new BigDecimal(GROSS_INCOMES.get(i).rice_subsidy()));
                employeePayrollSummaryReport.setPhoneAllowance(new BigDecimal(GROSS_INCOMES.get(i).phone_allowance()));
                employeePayrollSummaryReport.setClothingAllowance(new BigDecimal(GROSS_INCOMES.get(i).clothing_allowance()));
                employeePayrollSummaryReport.setTotalAllowance(employeePayrollSummaryReport.computeTotalAllowance());
                employeePayrollSummaryReport.setTinNumber(accountNumber.tin());
                employeePayrollSummaryReport.setGrossIncome(GROSS_INCOMES.get(i).computeGrossIncome());
                employeePayrollSummaryReport.setSocialSecurityContribution(deduction.getSss());
                employeePayrollSummaryReport.setPhilhealthContribution(deduction.getPhilhealth());
                employeePayrollSummaryReport.setPagIbigContribution(deduction.getPagibig());
                employeePayrollSummaryReport.setTaxableIncome(tax.getTaxableIncome());
                employeePayrollSummaryReport.setWithholdingTax(tax.getWithheldTax());
                employeePayrollSummaryReport.setTotalDeductions(tax.getWithheldTax().add(deduction.totalContributions()));
                employeePayrollSummaryReport.setNetPay(netPay);
                employeePayrollSummaryReport.setAllowanceId(allowanceService.getAllowanceIdByEmployeeId(GROSS_INCOMES.get(i).employeeId()));

                employeePayrollSummaryReports.add(employeePayrollSummaryReport);

                executeButton.setText("Save");
            }
            injectEmployeePayrollSummaryReportToTableView(employeePayrollSummaryReports);
        }
        // handle save generated payslips
        else {
            if (executeButton.getText().equals("Save")) {
                boolean isSaveButtonConfirmed = AlertUtility.showConfirmation("Generated Payslip Confirmation", "Save Payslips", "Are you you sure you want to save the generated payslips?");

                    if (isSaveButtonConfirmed) {
                        payslipService.saveGeneratedPayslip(tv_generated_payroll.getItems());
                    }
            }
        }
    }

    public void injectEmployeePayrollSummaryReportToTableView(ArrayList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        tv_generated_payroll.setItems(FXCollections.observableList(employeePayrollSummaryReports));
    }
}
