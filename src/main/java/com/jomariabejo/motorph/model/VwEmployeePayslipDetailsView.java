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
@Table(name = "vw_employee_payslip_details_view", schema = "payroll_system")
public class VwEmployeePayslipDetailsView {
    @Column(name = "PayslipID", nullable = false)
    private Integer payslipID;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "`CONCAT(e.FirstName, ' ',e.LastName)`", length = 201)
    private String cONCATEFirstNameELastName;

    @Column(name = "PeriodStartDate", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "PeriodEndDate", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "GrossIncome", nullable = false, precision = 18, scale = 4)
    private BigDecimal grossIncome;

    @Column(name = "TotalBenefits", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalBenefits;

    @Column(name = "TotalDeductions", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalDeductions;

    @Column(name = "NetPay", nullable = false, precision = 18, scale = 4)
    private BigDecimal netPay;

    @Column(name = "PayrollRunDate")
    private LocalDate payrollRunDate;

    @Column(name = "TransactionType", length = 50)
    private String transactionType;

    @Column(name = "TransactionAmount", precision = 18, scale = 4)
    private BigDecimal transactionAmount;

    @Column(name = "TransactionDate")
    private LocalDate transactionDate;

}