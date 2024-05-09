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
    private TimesheetService timesheetService;
    private EmployeeService employeeService;
    private AllowanceService allowanceService;
    private DeductionService deductionService;
    private TaxService taxService;
    private PayslipService payslipService;


    public GeneratePayslipController() {
        timesheetService = new TimesheetService();
        employeeService = new EmployeeService();
        allowanceService = new AllowanceService();
        deductionService = new DeductionService();
        taxService = new TaxService();
        payslipService = new PayslipService();
    }

    @FXML
    private TableView<EmployeePayrollSummaryReport> MONTHLY_PAYROLL_SUMMARY_REPORT;

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
            ArrayList<Deduction> DEDUCTIONS = new ArrayList<>();
            ArrayList<Tax> TAXES = new ArrayList<>();

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
                System.out.println("BASIC        SALARY = " + GROSS_INCOMES.get(i).basicSalary());
                System.out.println("GROSS INCOME SALARY = " + GROSS_INCOMES.get(i).computeGrossIncome());

                tax.setWithheldTax(
                        taxService.computeTax(
                            GROSS_INCOMES.get(i).basicSalary(),
                            GROSS_INCOMES.get(i).computeGrossIncome(),
                            deduction));
                TAXES.add(tax);

                System.out.println("SSS       DEDUCTION = " + deduction.getSss());
                System.out.println("PHILHEALTHDEDUCTION = " + deduction.getPhilhealth());
                System.out.println("PAGIBIG   DEDUCTION = " + deduction.getPagibig());
                System.out.println("THE TAX IS = " + tax.getWithheldTax());
                System.out.println("THE TOTAL DEDUCTION = " + deduction.totalContributions().add(tax.getWithheldTax()));
                System.out.println("Check tax string");
                tax.toString();
                DEDUCTIONS.add(deduction);

                AccountNumber accountNumber = employeeService.fetchEmployeeAccountNumber(GROSS_INCOMES.get(i).employeeId());
                BigDecimal netPay = GROSS_INCOMES.get(i).computeGrossIncome().subtract(deduction.totalContributions()).subtract(tax.getWithheldTax());

                /**
                 employeeNumber;✅
                 startPayDate;✅
                 endPayDate;✅
                 employeeFullName;✅
                 position;✅
                 department;✅
                 socialSecurityNumber;✅
                 philhealthNumber;✅
                 pagIbigNumber;✅
                 tinNumber;✅
                 monthlyRate;✅
                 hourlyRate;✅
                 totalRegularHoursWorked;✅
                 totalOvertimeHoursWorked;✅✅
                 riceSubsidy;✅
                 phoneAllowance;✅
                 clothingAllowance;✅
                 totalAllowance;✅
                 grossIncome;✅
                 socialSecurityContribution;✅
                 philhealthContribution;✅
                 pagIbigContribution;✅
                 withholdingTax;
                 totalDeductions;
                 netPay;
                 */
                EmployeePayrollSummaryReport employeePayrollSummaryReport = new EmployeePayrollSummaryReport();
                employeePayrollSummaryReport.setEmployeeNumber(GROSS_INCOMES.get(i).employeeId());
                employeePayrollSummaryReport.setStartPayDate(Date.valueOf(dp_startPayDate.getValue()));
                employeePayrollSummaryReport.setEndPayDate(Date.valueOf(dp_endPayDate.getValue()));
                employeePayrollSummaryReport.setEmployeeFullName(GROSS_INCOMES.get(i).employeeName());
                employeePayrollSummaryReport.setPosition(GROSS_INCOMES.get(i).position());
                employeePayrollSummaryReport.setDepartment(GROSS_INCOMES.get(i).department());
                employeePayrollSummaryReport.setEmployeeFullName(accountNumber.sss());
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

                // TODO: ONCACTION VIEW FOR EACH PAYSLIP
                // TODO: CREATE SAVE PAYSLIP
                executeButton.setText("Save");
            }
            injectEmployeePayrollSummaryReportToTableView(employeePayrollSummaryReports);
        }
        // handle save generated payslips
        else {
            if (executeButton.getText().equals("Save")) {
                boolean isSave = AlertUtility.showConfirmation("Generated Payslip Confirmation", "Save Payslips", "Are you you sure you want to save the generated payslips?");

                    if (isSave) {
//                        payslipService.saveGeneratedPayslip(MONTHLY_PAYROLL_SUMMARY_REPORT);
                    }
            }
        }
    }

    public void injectEmployeePayrollSummaryReportToTableView(ArrayList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        MONTHLY_PAYROLL_SUMMARY_REPORT.setItems(FXCollections.observableList(employeePayrollSummaryReports));
    }
}
