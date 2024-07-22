package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee", schema = "payroll_system", indexes = {
        @Index(name = "idx_position", columnList = "PositionID")
})
public class Employee {
    @Id
    @Column(name = "EmployeeNumber", nullable = false)
    private Integer id;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "Birthday")
    private LocalDate birthday;

    @Column(name = "Address")
    private String address;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "SSSNumber", length = 20)
    private String sSSNumber;

    @Column(name = "PhilhealthNumber", length = 20)
    private String philhealthNumber;

    @Column(name = "TINNumber", length = 20)
    private String tINNumber;

    @Column(name = "PagibigNumber", length = 20)
    private String pagibigNumber;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PositionID", nullable = false)
    private Position positionID;

    @Column(name = "BasicSalary", nullable = false, precision = 18, scale = 4)
    private BigDecimal basicSalary;

    @Column(name = "RiceSubsidy", nullable = false, precision = 18, scale = 4)
    private BigDecimal riceSubsidy;

    @Column(name = "PhoneAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal phoneAllowance;

    @Column(name = "ClothingAllowance", nullable = false, precision = 18, scale = 4)
    private BigDecimal clothingAllowance;

    @Column(name = "GrossSemiMonthlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal grossSemiMonthlyRate;

    @Column(name = "HourlyRate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyRate;

}