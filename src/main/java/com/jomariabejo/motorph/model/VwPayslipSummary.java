package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_payslip_summary", schema = "payroll_system")
public class VwPayslipSummary {
    @Column(name = "PayslipID", nullable = false)
    private Integer payslipID;

    @Column(name = "PayslipNumber", nullable = false, length = 20)
    private String payslipNumber;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "PeriodStartDate", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "PeriodEndDate", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "EmployeeName", nullable = false, length = 100)
    private String employeeName;

    @Column(name = "EmployeePosition", nullable = false, length = 100)
    private String employeePosition;

    @Column(name = "GrossIncome", nullable = false, precision = 18, scale = 4)
    private BigDecimal grossIncome;

    @Column(name = "TotalBenefits", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalBenefits;

    @Column(name = "TotalDeductions", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalDeductions;

    @Column(name = "NetPay", nullable = false, precision = 18, scale = 4)
    private BigDecimal netPay;

}