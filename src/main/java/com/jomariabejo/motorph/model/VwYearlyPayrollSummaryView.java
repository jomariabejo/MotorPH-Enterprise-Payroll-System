package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_yearly_payroll_summary_view", schema = "payroll_system")
public class VwYearlyPayrollSummaryView {
    @Column(name = "Year")
    private Integer year;

    @Column(name = "NumberOfPayrolls", nullable = false)
    private Long numberOfPayrolls;

    @Column(name = "TotalEmployees", precision = 42)
    private BigDecimal totalEmployees;

    @Column(name = "TotalGrossIncome", precision = 40, scale = 4)
    private BigDecimal totalGrossIncome;

    @Column(name = "TotalBenefits", precision = 40, scale = 4)
    private BigDecimal totalBenefits;

    @Column(name = "TotalDeductions", precision = 40, scale = 4)
    private BigDecimal totalDeductions;

    @Column(name = "TotalNetPay", precision = 40, scale = 4)
    private BigDecimal totalNetPay;

    @Column(name = "TotalPayslips", nullable = false)
    private Long totalPayslips;

    @Column(name = "TotalChanges", nullable = false)
    private Long totalChanges;

}