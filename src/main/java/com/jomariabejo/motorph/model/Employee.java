package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee", schema = "payroll_system", indexes = {
        @Index(name = "idx_position", columnList = "PositionID")
}, uniqueConstraints = {
        @UniqueConstraint(name = "SSSNumber", columnNames = {"SSSNumber"}),
        @UniqueConstraint(name = "PhilhealthNumber", columnNames = {"PhilhealthNumber"}),
        @UniqueConstraint(name = "TINNumber", columnNames = {"TINNumber"}),
        @UniqueConstraint(name = "PagibigNumber", columnNames = {"PagibigNumber"})
})
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeNumber", nullable = false)
    private Integer id;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "Birthday", nullable = false)
    private Date birthday;

    @Column(name = "DateHired", nullable = false)
    private Date dateHired;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "PhoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "SSSNumber", nullable = false, length = 20)
    private String sSSNumber;

    @Column(name = "PhilhealthNumber", nullable = false, length = 20)
    private String philhealthNumber;

    @Column(name = "TINNumber", nullable = false, length = 20)
    private String tINNumber;

    @Column(name = "PagibigNumber", nullable = false, length = 20)
    private String pagibigNumber;

    @ColumnDefault("'PROBATIONARY'")
    @Column(name = "Status", nullable = false, length = 255)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PositionID", nullable = false)
    private Position positionID;

    @ColumnDefault("0.0000")
    @Column(name = "BasicSalary", nullable = false, precision = 18, scale = 4)
    private BigDecimal basicSalary;

    @ColumnDefault("0.0000")
    @Column(name = "RiceSubsidy", nullable = false, precision = 18, scale = 4)
    private BigDecimal riceSubsidy;

    @ColumnDefault("0.0000")
    @Column(name = "PhoneAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal phoneAllowance;

    @ColumnDefault("0.0000")
    @Column(name = "ClothingAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal clothingAllowance;

    @ColumnDefault("0.0000")
    @Column(name = "GrossSemiMonthlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal grossSemiMonthlyRate;

    @ColumnDefault("0.0000")
    @Column(name = "HourlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyRate;

}