package com.jomariabejo.motorph.entity;

import java.math.BigDecimal;
import java.sql.Date;
public class Payslip {
    private int payslipID;
    private int employeeID;
    private int allowanceID;
    private int taxID;
    private int deductionID;
    private BigDecimal totalHoursWorked;
    private BigDecimal grossIncome;
    private BigDecimal netIncome;
    private Date payPeriodStart;
    private Date payPeriodEnd;
    private Date dateCreated;

    public Payslip() {
    }

    public Payslip(int employeeID, int allowanceID, int taxID, BigDecimal totalHoursWorked, BigDecimal grossIncome, BigDecimal netIncome, Date payPeriodStart, Date payPeriodEnd, Date dateCreated) {
        this.employeeID = employeeID;
        this.allowanceID = allowanceID;
        this.taxID = taxID;
        this.totalHoursWorked = totalHoursWorked;
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.dateCreated = dateCreated;
    }

    public Payslip(int payslipID, int employeeID, int allowanceID, int taxID, BigDecimal totalHoursWorked, BigDecimal grossIncome, BigDecimal netIncome, Date payPeriodStart, Date payPeriodEnd, Date dateCreated) {
        this.payslipID = payslipID;
        this.employeeID = employeeID;
        this.allowanceID = allowanceID;
        this.taxID = taxID;
        this.totalHoursWorked = totalHoursWorked;
        this.grossIncome = grossIncome;
        this.netIncome = netIncome;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.dateCreated = dateCreated;
    }

    public int getPayslipID() {
        return payslipID;
    }

    public void setPayslipID(int payslipID) {
        this.payslipID = payslipID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getAllowanceID() {
        return allowanceID;
    }

    public void setAllowanceID(int allowanceID) {
        this.allowanceID = allowanceID;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public BigDecimal getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(BigDecimal totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(BigDecimal grossIncome) {
        this.grossIncome = grossIncome;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public Date getPayPeriodStart() {
        return payPeriodStart;
    }

    public void setPayPeriodStart(Date payPeriodStart) {
        this.payPeriodStart = payPeriodStart;
    }

    public Date getPayPeriodEnd() {
        return payPeriodEnd;
    }

    public void setPayPeriodEnd(Date payPeriodEnd) {
        this.payPeriodEnd = payPeriodEnd;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getDeductionID() {
        return deductionID;
    }

    public void setDeductionID(int deductionID) {
        this.deductionID = deductionID;
    }

    @Override
    public String toString() {
        return "Payslip{" +
                "payslipID=" + payslipID +
                ", employeeID=" + employeeID +
                ", allowanceID=" + allowanceID +
                ", taxID=" + taxID +
                ", deductionID=" + deductionID +
                ", totalHoursWorked=" + totalHoursWorked +
                ", grossIncome=" + grossIncome +
                ", netIncome=" + netIncome +
                ", payPeriodStart=" + payPeriodStart +
                ", payPeriodEnd=" + payPeriodEnd +
                ", dateCreated=" + dateCreated +
                '}';
    }
}
