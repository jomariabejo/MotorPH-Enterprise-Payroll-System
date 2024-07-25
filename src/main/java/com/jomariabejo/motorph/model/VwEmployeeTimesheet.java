package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_employee_timesheet", schema = "payroll_system")
public class VwEmployeeTimesheet {
    @Column(name = "TimesheetID", nullable = false)
    private Integer timesheetID;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "TimeIn", nullable = false)
    private LocalTime timeIn;

    @Column(name = "TimeOut")
    private LocalTime timeOut;

    @Column(name = "HoursWorked", precision = 8, scale = 2)
    private BigDecimal hoursWorked;

    @Column(name = "Remarks")
    private String remarks;

}