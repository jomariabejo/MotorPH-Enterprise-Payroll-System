package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "payslip", schema = "payroll_system")
@NamedQueries(
        {
                @NamedQuery(
                        name = "fetchPayslipByEmployeeAndYear",
                        query = "SELECT ps FROM Payslip ps WHERE " +
                                "ps.employeeID = :employeeId AND " +
                                "FUNCTION('YEAR', ps.payrollRunDate) = :year " +
                                "ORDER BY FUNCTION('MONTH', ps.payrollRunDate) DESC"
                ),
                @NamedQuery(
                        name = "fetchEmployeeYearsOfPayslip",
                        query = "SELECT DISTINCT YEAR(PS.payrollRunDate) AS Years FROM Payslip PS WHERE PS.employeeID = :employeeId ORDER BY YEAR(PS.payrollRunDate) DESC"
                ),
                @NamedQuery(
                        name = "fetchYearToDateFigures",
                        query = "SELECT new com.jomariabejo.motorph.model.YearToDateFigures(" +
                                "SUM(PS.grossIncome), " +
                                "SUM(PS.totalBenefits), " +
                                "SUM(PS.bonus), " +
                                "SUM(PS.taxableIncome), " +
                                "SUM(PS.withholdingTax), " +
                                "SUM(PS.totalDeductions), " +
                                "SUM(PS.netPay)) " +
                                "FROM Payslip PS WHERE PS.employeeID = :employeeId AND YEAR(PS.payrollRunDate) = :year"
                )
        }
)
public class Payslip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PayslipID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayrollID", nullable = false)
    private Payroll payrollID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "PeriodStartDate", nullable = false)
    private Date periodStartDate;

    @Column(name = "PeriodEndDate", nullable = false)
    private Date periodEndDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ColumnDefault("0")
    @JoinColumn(name = "EmployeePosition", nullable = false)
    private Position employeePosition;

    @Column(name = "EmployeeName", nullable = false, length = 100)
    private String employeeName;

    @Column(name = "MonthlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal monthlyRate;

    @Column(name = "DailyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal dailyRate;

    @Column(name = "DaysWorked", nullable = false)
    private Integer daysWorked;

    @Column(name = "HourlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyRate;

    @Column(name = "TotalHoursWorked", nullable = false)
    private BigDecimal totalHoursWorked;

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
    private Date payrollRunDate;

    @Column(name = "PaymentDate")
    private Date paymentDate;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeDepartment")
    private Department employeeDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeManager")
    private Employee employeeManager;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy")
    private Employee createdBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedDate")
    private Timestamp createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LastModifiedBy")
    private Employee lastModifiedBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "LastModifiedDate")
    private Timestamp lastModifiedDate;

    @Column(name = "PayslipNumber", nullable = false, length = 20)
    private String payslipNumber;

    public void toPayslipFormat() {

    }
}