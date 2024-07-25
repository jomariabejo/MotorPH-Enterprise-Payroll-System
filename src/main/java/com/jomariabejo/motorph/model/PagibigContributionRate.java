package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pagibig_contribution_rates", schema = "payroll_system", indexes = {
        @Index(name = "idx_pagibig_effective_date", columnList = "EffectiveDate")
})
public class PagibigContributionRate {
    @Id
    @Column(name = "ContributionRateID", nullable = false)
    private Integer id;

    @Column(name = "SalaryBracketFrom", nullable = false, precision = 18, scale = 4)
    private BigDecimal salaryBracketFrom;

    @Column(name = "SalaryBracketTo", nullable = false, precision = 18, scale = 4)
    private BigDecimal salaryBracketTo;

    @Column(name = "EmployeeShare", nullable = false, precision = 18, scale = 4)
    private BigDecimal employeeShare;

    @Column(name = "EmployerShare", nullable = false, precision = 18, scale = 4)
    private BigDecimal employerShare;

    @Column(name = "EffectiveDate", nullable = false)
    private LocalDate effectiveDate;

}