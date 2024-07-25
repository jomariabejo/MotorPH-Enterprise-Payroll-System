package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payslip", schema = "payroll_system")
public class Payslip {
    @Id
    @Column(name = "PayslipID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayrollID", nullable = false)
    private Payroll payrollID;

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

    @ColumnDefault("0.0000")
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

    @Column(name = "CompanyID")
    private Integer companyID;

    @Column(name = "CompanyName", length = 100)
    private String companyName;

    @Column(name = "PayrollRunDate")
    private LocalDate payrollRunDate;

    @Column(name = "PaymentDate")
    private LocalDate paymentDate;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Column(name = "EmployeeDepartment", length = 100)
    private String employeeDepartment;

    @Column(name = "EmployeeManager", length = 100)
    private String employeeManager;

    @Column(name = "LeaveDays")
    private Integer leaveDays;

    @Column(name = "AbsenceDays")
    private Integer absenceDays;

    @Column(name = "TaxableIncome", precision = 18, scale = 4)
    private BigDecimal taxableIncome;

    @Column(name = "TaxableBenefits", precision = 18, scale = 4)
    private BigDecimal taxableBenefits;

    @Column(name = "Bonus", precision = 18, scale = 4)
    private BigDecimal bonus;

    @Column(name = "OtherDeductions", precision = 18, scale = 4)
    private BigDecimal otherDeductions;

    @Column(name = "CreatedBy", length = 100)
    private String createdBy;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Column(name = "LastModifiedBy", length = 100)
    private String lastModifiedBy;

    @Column(name = "LastModifiedDate")
    private Instant lastModifiedDate;

}