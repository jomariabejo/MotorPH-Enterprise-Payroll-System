package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "timesheet", schema = "payroll_system")
public class Timesheet {
    @Id
    @Column(name = "TimesheetID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID")
    private Employee employeeID;

    @Column(name = "Date")
    private LocalDate date;

    @Column(name = "TimeIn")
    private LocalTime timeIn;

    @Column(name = "TimeOut")
    private LocalTime timeOut;

    @Column(name = "HoursWorked", precision = 8, scale = 2)
    private BigDecimal hoursWorked;

    @Column(name = "Remarks")
    private String remarks;

}