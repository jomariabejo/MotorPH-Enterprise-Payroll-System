package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_timesheet_summary_view", schema = "payroll_system")
public class VwTimesheetSummaryView {
    @Column(name = "TimesheetID", nullable = false)
    private Integer timesheetID;

    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "EmployeeName", length = 201)
    private String employeeName;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "HoursWorked", precision = 8, scale = 2)
    private BigDecimal hoursWorked;

    @Column(name = "OvertimeHours", nullable = false, precision = 30, scale = 4)
    private BigDecimal overtimeHours;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

}