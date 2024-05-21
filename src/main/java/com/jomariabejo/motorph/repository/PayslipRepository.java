package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.database.DatabaseConnectionUtility;
import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.service.DeductionService;
import com.jomariabejo.motorph.service.TaxCategoryService;
import com.jomariabejo.motorph.service.TaxService;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;
import com.jomariabejo.motorph.utility.DateUtility;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PayslipRepository {
    private final TaxService taxService;
    private final TaxCategoryService taxCategoryService;
    private final DeductionService deductionService;
    private int counter;

    public PayslipRepository() {
        this.taxService = new TaxService();
        this.deductionService = new DeductionService();
        this.taxCategoryService = new TaxCategoryService();
    }

    /**
     * Save multiple payslip into database.
     * @param employeePayrollSummaryReports
     */
    public void saveMultiplePayslip(ObservableList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        // Iterate through the list of payroll summary reports
        employeePayrollSummaryReports.stream().forEach(x -> {
            // Create a new Payslip instance
            Payslip payslip = new Payslip();

            // Get the next value of payslip unique identifier
            int payslipId = AutoIncrementUtility.getNextAutoIncrementValueForPayslip();

            // Create a new Deduction instance
            Deduction deduction = new Deduction();
            deduction.setDeductionID(payslipId);
            deduction.setEmployeeID(x.getEmployeeNumber());
            deduction.setPagibig(x.getPagIbigContribution());
            deduction.setPhilhealth(x.getPhilhealthContribution());
            deduction.setSss(x.getSocialSecurityContribution());
            deduction.setDateCreated(DateUtility.getDate());

            // Save the deduction
            this.deductionService.saveDeduction(deduction);

            // Create a new Tax instance
            Tax tax = new Tax();
            tax.setTaxId(payslipId);
            tax.setEmployeeId(x.getEmployeeNumber());
            tax.setWithheldTax(x.getWithholdingTax());
            tax.setTaxableIncome(x.getTaxableIncome());
            tax.setDateCreated(DateUtility.getDate());

            // Fetch tax category ID by monthly rate and save tax
            tax.setTaxCategoryId(taxCategoryService.fetchTaxCategoryIdByMonthlyRate(x.getGrossIncome()));
            this.taxService.saveTax(tax);

            // Set payslip attributes
            payslip.setPayslipID(payslipId);
            payslip.setTaxID(payslipId); //
            payslip.setDeductionID(payslipId);
            payslip.setEmployeeID(x.getEmployeeNumber());
            payslip.setAllowanceID(x.getAllowanceId());
            payslip.setPayPeriodStart(x.getStartPayDate());
            payslip.setPayPeriodEnd(x.getEndPayDate());
            payslip.setTotalHoursWorked(x.getTotalOvertimeHoursWorked().add(x.getTotalRegularHoursWorked()));
            payslip.setGrossIncome(x.getGrossIncome());
            payslip.setNetIncome(x.getNetPay());
            payslip.setDateCreated(DateUtility.getDate());
            // Save the payslip
            savePayslip(payslip);
        });
    }


    private void savePayslip(Payslip payslip) {
        String query = "INSERT INTO payslip (payslip_id, employee_id, alw_id, deduction_id, tax_id, pay_period_start, pay_period_end, total_hours_worked, gross_income, net_income, date_created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";
        counter++;

        try (Connection connection = DatabaseConnectionUtility.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,payslip.getPayslipID());
            preparedStatement.setInt(2,payslip.getEmployeeID());
            preparedStatement.setInt(3,payslip.getAllowanceID());
            preparedStatement.setInt(4,payslip.getDeductionID());
            preparedStatement.setInt(5,payslip.getTaxID());
            preparedStatement.setDate(6,payslip.getPayPeriodStart());
            preparedStatement.setDate(7,payslip.getPayPeriodEnd());
            preparedStatement.setBigDecimal(8,payslip.getTotalHoursWorked());
            preparedStatement.setBigDecimal(9,payslip.getGrossIncome());
            preparedStatement.setBigDecimal(10,payslip.getNetIncome());
            preparedStatement.setDate(11,payslip.getDateCreated());

            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println((rowsAffected > 0) ? ("Save payslip success " + counter)  : ("Not saved " + counter));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public int monthlyRateTo
}
