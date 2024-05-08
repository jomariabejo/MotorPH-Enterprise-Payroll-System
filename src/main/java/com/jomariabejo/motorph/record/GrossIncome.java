package com.jomariabejo.motorph.record;

import java.math.BigDecimal;

public record GrossIncome(
        int employeeId,
        String employeeName,
        String position,
        String department,
        BigDecimal basicSalary,
        BigDecimal hourslyRate,
        BigDecimal total_regular_hours_worked,
        BigDecimal total_overtime_hours,
        BigDecimal total_regular_hours_worked_salary,
        BigDecimal total_overtime_hours_worked_salary,
        int rice_subsidy,
        int clothing_allowance,
        int phone_allowance,
        int total_allowance) {
    public BigDecimal computeGrossIncome() {
        return total_regular_hours_worked_salary.add(total_overtime_hours_worked_salary);
    }
}
