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
@Table(name = "vw_payroll_summary_view", schema = "payroll_system")
public class VwPayrollSummaryView {
    @Column(name = "PayrollID", nullable = false)
    private Integer payrollID;

    @Column(name = "PayrollRunDate", nullable = false)
    private LocalDate payrollRunDate;

    @Column(name = "NumberOfEmployees", nullable = false)
    private Long numberOfEmployees;

    @Column(name = "TotalGrossIncome", precision = 40, scale = 4)
    private BigDecimal totalGrossIncome;

    @Column(name = "TotalBenefits", precision = 40, scale = 4)
    private BigDecimal totalBenefits;

    @Column(name = "TotalDeductions", precision = 40, scale = 4)
    private BigDecimal totalDeductions;

    @Column(name = "TotalNetPay", precision = 40, scale = 4)
    private BigDecimal totalNetPay;

    @Column(name = "NumberOfPayslips", nullable = false)
    private Long numberOfPayslips;

    @Column(name = "NumberOfChanges", nullable = false)
    private Long numberOfChanges;

}