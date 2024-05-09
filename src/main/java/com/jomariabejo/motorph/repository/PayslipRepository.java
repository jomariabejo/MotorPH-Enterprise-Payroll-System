package com.jomariabejo.motorph.repository;

import com.jomariabejo.motorph.entity.Deduction;
import com.jomariabejo.motorph.entity.EmployeePayrollSummaryReport;
import com.jomariabejo.motorph.entity.Payslip;
import com.jomariabejo.motorph.entity.Tax;
import com.jomariabejo.motorph.service.DeductionService;
import com.jomariabejo.motorph.service.TaxCategoryService;
import com.jomariabejo.motorph.service.TaxService;
import com.jomariabejo.motorph.utility.AutoIncrementUtility;
import com.jomariabejo.motorph.utility.DateUtility;

import java.util.ArrayList;

public class PayslipRepository {
    private TaxService taxService;
    private TaxCategoryService taxCategoryService;
    private DeductionService deductionService;

    public PayslipRepository() {
        this.taxService = new TaxService();
        this.deductionService = new DeductionService();
        this.taxCategoryService = new TaxCategoryService();
    }

    /**
     * Save multiple payslip into database.
     * @param employeePayrollSummaryReports
     */
    public void saveMultiplePayslip(ArrayList<EmployeePayrollSummaryReport> employeePayrollSummaryReports) {
        employeePayrollSummaryReports.stream().forEach( x-> {
            Payslip payslip = new Payslip();
            // The id for payslip, tax, deduction are same
            int payslipId = AutoIncrementUtility.getNextAutoIncrementValueForPayslip();


            Deduction deduction = new Deduction();
            deduction.setDeductionID(payslipId);
            deduction.setEmployeeID(x.getEmployeeNumber());
            deduction.setPagibig(x.getPagIbigContribution());
            deduction.setPhilhealth(x.getPhilhealthContribution());
            deduction.setSss(x.getSocialSecurityContribution());
            deduction.setDateCreated(DateUtility.getDate());
            this.deductionService.saveDeduction(deduction);

            Tax tax = new Tax();
            tax.setTaxId(payslipId);
            tax.setEmployeeId(x.getEmployeeNumber());
            tax.setWithheldTax(x.getWithholdingTax());
            tax.setTaxableIncome(x.getTaxableIncome());
            tax.setDateCreated(DateUtility.getDate());
            tax.setTaxCategoryId(taxCategoryService.fetchTaxCategoryIdByMonthlyRate(x.getGrossIncome()));
            this.taxService.saveTax(tax);

            payslip.setPayslipID((payslipId));
            payslip.setTaxID(payslipId);
            payslip.setDeductionID(payslipId);
            payslip.setEmployeeID(x.getEmployeeNumber());
            payslip.setAllowanceID(x.getAllowanceId());
            payslip.setPayPeriodEnd(x.getStartPayDate());
            payslip.setPayPeriodEnd(x.getEndPayDate());
            payslip.setTotalHoursWorked(x.getTotalOvertimeHoursWorked().add(x.getTotalRegularHoursWorked()));
            payslip.setGrossIncome(x.getGrossIncome());
            payslip.setNetIncome(x.getNetPay());
            payslip.setDateCreated(DateUtility.getDate());

            savePayslip(payslip);
        });
    }

    private void savePayslip(Payslip payslip) {
//        String query =
    }

//    public int monthlyRateTo
}
