package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payroll", schema = "payroll_system")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PayrollID", nullable = false)
    private Integer id;

    @Column(name = "PayrollRunDate", nullable = false)
    private LocalDate payrollRunDate;

    @Column(name = "PeriodStartDate", nullable = false)
    private LocalDate periodStartDate;

    @Column(name = "PeriodEndDate", nullable = false)
    private LocalDate periodEndDate;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @Column(name = "CreatedBy", length = 100)
    private String createdBy;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Column(name = "LastModifiedBy", length = 100)
    private String lastModifiedBy;

    @Column(name = "LastModifiedDate")
    private Instant lastModifiedDate;

}