package com.jomariabejo.motorph.entity;

import java.math.BigDecimal;
import java.sql.Date;

public class Tax {
    private int taxId, employeeId;
    private BigDecimal taxableIncome;
    private int taxCategoryId;
    private BigDecimal withheldTax;
    private Date dateCreated;

    // Constructors
    public Tax() {

    }

    public Tax(BigDecimal taxableIncome, int taxCategoryId, BigDecimal withheldTax) {
        this.taxableIncome = taxableIncome;
        this.taxCategoryId = taxCategoryId;
        this.withheldTax = withheldTax;
    }

    public Tax(int employeeId, BigDecimal taxableIncome, int taxCategoryId, BigDecimal withheldTax, Date dateCreated) {
        this.employeeId = employeeId;
        this.taxableIncome = taxableIncome;
        this.taxCategoryId = taxCategoryId;
        this.withheldTax = withheldTax;
        this.dateCreated = dateCreated;
    }

    // Getters and Setters
    public int getTaxId() {
        return taxId;
    }

    public void setTaxId(int taxId) {
        this.taxId = taxId;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public int getTaxCategoryId() {
        return taxCategoryId;
    }

    public void setTaxCategoryId(int taxCategoryId) {
        this.taxCategoryId = taxCategoryId;
    }

    public BigDecimal getWithheldTax() {
        return withheldTax;
    }

    public void setWithheldTax(BigDecimal withheldTax) {
        this.withheldTax = withheldTax;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    // ToString method
    @Override
    public String toString() {
        return "Tax{" +
                "taxId=" + taxId +
                ", taxableIncome=" + taxableIncome +
                ", taxCategoryId=" + taxCategoryId +
                ", withheldTax=" + withheldTax +
                '}';
    }
}
