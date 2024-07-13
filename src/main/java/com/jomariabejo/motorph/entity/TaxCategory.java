package com.jomariabejo.motorph.entity;

public class TaxCategory {
    private int taxCategoryID;
    private int minimumMonthlyRate;
    private int maximumMonthlyRate;
    private int taxRate;
    private int additionalTaxRate;
    private String name;

    public TaxCategory() {
    }

    public TaxCategory(int minimumMonthlyRate, int maximumMonthlyRate, int taxRate, int additionalTaxRate, String name) {
        this.minimumMonthlyRate = minimumMonthlyRate;
        this.maximumMonthlyRate = maximumMonthlyRate;
        this.taxRate = taxRate;
        this.additionalTaxRate = additionalTaxRate;
        this.name = name;
    }

    public TaxCategory(int taxCategoryID, int minimumMonthlyRate, int maximumMonthlyRate, int taxRate, int additionalTaxRate, String name) {
        this.taxCategoryID = taxCategoryID;
        this.minimumMonthlyRate = minimumMonthlyRate;
        this.maximumMonthlyRate = maximumMonthlyRate;
        this.taxRate = taxRate;
        this.additionalTaxRate = additionalTaxRate;
        this.name = name;
    }

    public int getTaxCategoryID() {
        return taxCategoryID;
    }

    public void setTaxCategoryID(int taxCategoryID) {
        this.taxCategoryID = taxCategoryID;
    }

    public int getMinimumMonthlyRate() {
        return minimumMonthlyRate;
    }

    public void setMinimumMonthlyRate(int minimumMonthlyRate) {
        this.minimumMonthlyRate = minimumMonthlyRate;
    }

    public int getMaximumMonthlyRate() {
        return maximumMonthlyRate;
    }

    public void setMaximumMonthlyRate(int maximumMonthlyRate) {
        this.maximumMonthlyRate = maximumMonthlyRate;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getAdditionalTaxRate() {
        return additionalTaxRate;
    }

    public void setAdditionalTaxRate(int additionalTaxRate) {
        this.additionalTaxRate = additionalTaxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
