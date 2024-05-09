package com.jomariabejo.motorph.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class EmployeePayrollSummaryReport {
    private int employeeNumber, allowanceId;
    private Date startPayDate, endPayDate;
    private String employeeFullName, position, department, socialSecurityNumber, philhealthNumber, pagIbigNumber, tinNumber;
    private BigDecimal monthlyRate, hourlyRate, taxableIncome, totalRegularHoursWorked, totalOvertimeHoursWorked, riceSubsidy, phoneAllowance, clothingAllowance, totalAllowance, grossIncome, socialSecurityContribution, philhealthContribution, pagIbigContribution, withholdingTax, totalDeductions, netPay;

    public EmployeePayrollSummaryReport() {
    }

    public EmployeePayrollSummaryReport(int employeeNumber, Date startPayDate, Date endPayDate, String employeeFullName, String position, String department, String socialSecurityNumber, String philhealthNumber, String pagIbigNumber, String tinNumber, BigDecimal monthlyRate, BigDecimal hourlyRate, BigDecimal totalRegularHoursWorked, BigDecimal totalOvertimeHoursWorked, BigDecimal riceSubsidy, BigDecimal phoneAllowance, BigDecimal clothingAllowance, BigDecimal totalAllowance, BigDecimal grossIncome, BigDecimal socialSecurityContribution, BigDecimal philhealthContribution, BigDecimal pagIbigContribution, BigDecimal withholdingTax, BigDecimal totalDeductions, BigDecimal netPay) {
        this.employeeNumber = employeeNumber;
        this.startPayDate = startPayDate;
        this.endPayDate = endPayDate;
        this.employeeFullName = employeeFullName;
        this.position = position;
        this.department = department;
        this.socialSecurityNumber = socialSecurityNumber;
        this.philhealthNumber = philhealthNumber;
        this.pagIbigNumber = pagIbigNumber;
        this.tinNumber = tinNumber;
        this.monthlyRate = monthlyRate;
        this.hourlyRate = hourlyRate;
        this.totalRegularHoursWorked = totalRegularHoursWorked;
        this.totalOvertimeHoursWorked = totalOvertimeHoursWorked;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.totalAllowance = totalAllowance;
        this.grossIncome = grossIncome;
        this.socialSecurityContribution = socialSecurityContribution;
        this.philhealthContribution = philhealthContribution;
        this.pagIbigContribution = pagIbigContribution;
        this.withholdingTax = withholdingTax;
        this.totalDeductions = totalDeductions;
        this.netPay = netPay;
    }

    public int getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(int employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public void setPhilhealthNumber(String philhealthNumber) {
        this.philhealthNumber = philhealthNumber;
    }

    public String getPagIbigNumber() {
        return pagIbigNumber;
    }

    public void setPagIbigNumber(String pagIbigNumber) {
        this.pagIbigNumber = pagIbigNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(BigDecimal grossIncome) {
        this.grossIncome = grossIncome;
    }

    public BigDecimal getSocialSecurityContribution() {
        return socialSecurityContribution;
    }

    public void setSocialSecurityContribution(BigDecimal socialSecurityContribution) {
        this.socialSecurityContribution = socialSecurityContribution;
    }

    public BigDecimal getPhilhealthContribution() {
        return philhealthContribution;
    }

    public void setPhilhealthContribution(BigDecimal philhealthContribution) {
        this.philhealthContribution = philhealthContribution;
    }

    public BigDecimal getPagIbigContribution() {
        return pagIbigContribution;
    }

    public void setPagIbigContribution(BigDecimal pagIbigContribution) {
        this.pagIbigContribution = pagIbigContribution;
    }

    public BigDecimal getWithholdingTax() {
        return withholdingTax;
    }

    public void setWithholdingTax(BigDecimal withholdingTax) {
        this.withholdingTax = withholdingTax;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public Date getStartPayDate() {
        return startPayDate;
    }

    public void setStartPayDate(Date startPayDate) {
        this.startPayDate = startPayDate;
    }

    public Date getEndPayDate() {
        return endPayDate;
    }

    public void setEndPayDate(Date endPayDate) {
        this.endPayDate = endPayDate;
    }

    public BigDecimal getMonthlyRate() {
        return monthlyRate;
    }

    public void setMonthlyRate(BigDecimal monthlyRate) {
        this.monthlyRate = monthlyRate;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getTotalRegularHoursWorked() {
        return totalRegularHoursWorked;
    }

    public void setTotalRegularHoursWorked(BigDecimal totalRegularHoursWorked) {
        this.totalRegularHoursWorked = totalRegularHoursWorked;
    }

    public BigDecimal getTotalOvertimeHoursWorked() {
        return totalOvertimeHoursWorked;
    }

    public void setTotalOvertimeHoursWorked(BigDecimal totalOvertimeHoursWorked) {
        this.totalOvertimeHoursWorked = totalOvertimeHoursWorked;
    }

    public BigDecimal getRiceSubsidy() {
        return riceSubsidy;
    }

    public void setRiceSubsidy(BigDecimal riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public BigDecimal getPhoneAllowance() {
        return phoneAllowance;
    }

    public void setPhoneAllowance(BigDecimal phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public BigDecimal getClothingAllowance() {
        return clothingAllowance;
    }

    public void setClothingAllowance(BigDecimal clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public BigDecimal getTotalAllowance() {
        return totalAllowance;
    }

    public void setTotalAllowance(BigDecimal totalAllowance) {
        this.totalAllowance = totalAllowance;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    @Override
    public String toString() {
        return "EmployeePayrollSummaryReport{" +
                "employeeNumber=" + employeeNumber +
                ", employeeFullName='" + employeeFullName + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", socialSecurityNumber='" + socialSecurityNumber + '\'' +
                ", philhealthNumber='" + philhealthNumber + '\'' +
                ", pagIbigNumber='" + pagIbigNumber + '\'' +
                ", tinNumber='" + tinNumber + '\'' +
                ", grossIncome=" + grossIncome +
                ", socialSecurityContribution=" + socialSecurityContribution +
                ", philhealthContribution=" + philhealthContribution +
                ", pagIbigContribution=" + pagIbigContribution +
                ", withholdingTax=" + withholdingTax +
                ", netPay=" + netPay +
                '}';
    }

    public BigDecimal computeTotalAllowance() {
        return this.clothingAllowance.add(this.phoneAllowance).add(this.riceSubsidy);
    }

    public BigDecimal computeTotalDeductions() {
        return this.getSocialSecurityContribution().add(this.getPhilhealthContribution()).add(this.getPagIbigContribution().add(this.withholdingTax));
    }

    public int getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(int allowanceId) {
        this.allowanceId = allowanceId;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
    }
}
