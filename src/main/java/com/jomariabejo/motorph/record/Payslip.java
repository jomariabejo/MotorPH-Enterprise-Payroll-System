package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record Payslip (
        int payslipNumber,
        int employeeId,
        String employeeName,
        String periodStartDate,
        String periodEndDate,
        String employeePosition,
        String employeeDepartment,
        // Earnings
        BigDecimal monthlyRate,
        BigDecimal hourlyRate,
        BigDecimal totalRegularHoursWorked,
        BigDecimal overtime,
        BigDecimal grossIncome,
        // Benefits
        int riceSubsidy,
        int phoneAllowance,
        int clothingAllowance,
        int totalBenefits,

        // Deductions
        BigDecimal sss,
        BigDecimal philhealth,
        BigDecimal pagibig,
        BigDecimal withholdingTax,
        BigDecimal totalDeductions,
        BigDecimal takeHomePay
        ) {
}
