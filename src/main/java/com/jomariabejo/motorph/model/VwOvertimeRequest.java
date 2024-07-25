package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_overtime_requests", schema = "payroll_system")
public class VwOvertimeRequest {
    @Column(name = "RequestID", nullable = false)
    private Integer requestID;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "DateRequested", nullable = false)
    private Instant dateRequested;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "HoursRequested", nullable = false, precision = 8, scale = 4)
    private BigDecimal hoursRequested;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "`HR Remarks`")
    private String hRRemarks;

}