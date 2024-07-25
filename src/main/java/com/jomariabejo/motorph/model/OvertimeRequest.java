package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "overtime_request", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_overtime", columnList = "EmployeeID")
})
public class OvertimeRequest {
    @Id
    @Column(name = "RequestID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "DateRequested", nullable = false)
    private Instant dateRequested;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "HoursRequested", nullable = false, precision = 8, scale = 4)
    private BigDecimal hoursRequested;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "HRRemarks")
    private String hRRemarks;

}