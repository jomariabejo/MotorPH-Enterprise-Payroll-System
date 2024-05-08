package com.jomariabejo.motorph.service;

import com.jomariabejo.motorph.entity.Deduction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxService {
    private DeductionService deductionService;

    public TaxService() {
        deductionService = new DeductionService();
    }


    public BigDecimal computeTax(BigDecimal basicSalary, BigDecimal grossSalary, Deduction deduction) {

        BigDecimal totalContribution = deduction.totalContributions();
        System.out.println(deduction.toString());
        BigDecimal taxableIncome = basicSalary.subtract(totalContribution);

        BigDecimal withholdingTax = BigDecimal.ZERO;

        if (taxableIncome.compareTo(new BigDecimal("20832")) <= 0)
            withholdingTax = BigDecimal.ZERO;
        else if (taxableIncome.compareTo(new BigDecimal("33333")) < 0)
            withholdingTax = taxableIncome.subtract(new BigDecimal("20833")).multiply(new BigDecimal("0.2"));
        else if (taxableIncome.compareTo(new BigDecimal("66667")) < 0)
            withholdingTax = taxableIncome.subtract(new BigDecimal("33333")).multiply(new BigDecimal("0.25")).add(new BigDecimal("2500"));
        else if (taxableIncome.compareTo(new BigDecimal("166667")) < 0)
            withholdingTax = taxableIncome.subtract(new BigDecimal("66667")).multiply(new BigDecimal("0.3")).add(new BigDecimal("10833"));
        else if (taxableIncome.compareTo(new BigDecimal("666667")) < 0)
            withholdingTax = taxableIncome.subtract(new BigDecimal("166667")).multiply(new BigDecimal("0.32")).add(new BigDecimal("40833.33"));
        else
            withholdingTax = taxableIncome.subtract(new BigDecimal("666667")).multiply(new BigDecimal("0.35")).add(new BigDecimal("200833.33"));

        // Round the withholding tax to 2 decimal places
        withholdingTax = withholdingTax.setScale(2, RoundingMode.HALF_UP);

        return withholdingTax;
    }


}
