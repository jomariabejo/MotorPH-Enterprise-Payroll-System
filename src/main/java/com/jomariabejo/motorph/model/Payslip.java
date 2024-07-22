package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payslip", schema = "payroll_system")
public class Payslip {
    @Id
    @Column(name = "PayslipID", nullable = false)
    private Integer id;

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

    @Column(name = "MonthlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal monthlyRate;

    @Column(name = "DailyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal dailyRate;

    @Column(name = "DaysWorked", nullable = false)
    private Integer daysWorked;

    @Column(name = "OvertimeHours", nullable = false, precision = 8, scale = 4)
    private BigDecimal overtimeHours;

    @Column(name = "GrossIncome", nullable = false, precision = 18, scale = 4)
    private BigDecimal grossIncome;

    @Column(name = "RiceSubsidy", nullable = false, precision = 18, scale = 4)
    private BigDecimal riceSubsidy;

    @Column(name = "PhoneAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal phoneAllowance;

    @Column(name = "ClothingAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal clothingAllowance;

    @Column(name = "TotalBenefits", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalBenefits;

    @Column(name = "SSS", nullable = false, precision = 18, scale = 4)
    private BigDecimal sss;

    @Column(name = "Philhealth", nullable = false, precision = 18, scale = 4)
    private BigDecimal philhealth;

    @Column(name = "PagIbig", nullable = false, precision = 18, scale = 4)
    private BigDecimal pagIbig;

    @Column(name = "WithholdingTax", nullable = false, precision = 18, scale = 4)
    private BigDecimal withholdingTax;

    @Column(name = "TotalDeductions", nullable = false, precision = 18, scale = 4)
    private BigDecimal totalDeductions;

    @Column(name = "NetPay", nullable = false, precision = 18, scale = 4)
    private BigDecimal netPay;

}