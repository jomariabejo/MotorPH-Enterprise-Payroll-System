package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "timesheet", schema = "payroll_system")
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TimesheetID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "TimeIn", nullable = false)
    private LocalTime timeIn;

    @Column(name = "TimeOut")
    private LocalTime timeOut;

    @ColumnDefault("(case when ((`TimeOut` is not null) and (`TimeIn` is not null)) then (time_to_sec(timediff(least(`TimeOut`,_utf8mb4'17:00:00'),greatest(`TimeIn`,_utf8mb4'08:00:00'))) / 3600.0) else NULL end)")
    @Column(name = "HoursWorked", precision = 8, scale = 2)
    private BigDecimal hoursWorked;

    @Column(name = "Remarks")
    private String remarks;

    @ColumnDefault("'Not Submitted'")
    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Approver")
    private Employee approver;

    public enum Status {
        APPROVED,
        DISAPPROVED,
        SUBMITTED,
        NOT_SUBMITTED
    }
}