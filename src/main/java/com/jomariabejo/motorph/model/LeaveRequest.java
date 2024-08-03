package com.jomariabejo.motorph.model;

import com.jomariabejo.motorph.utility.NetworkUtils;
import com.jomariabejo.motorph.utility.TimestampUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "leave_request", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_leave_request", columnList = "EmployeeID"),
        @Index(name = "idx_leave_type", columnList = "LeaveTypeID")
})
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveRequestID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LeaveTypeID", nullable = false)
    private LeaveRequestType leaveTypeID;

    @Column(name = "DateRequested")
    private Timestamp dateRequested;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AdminApprovalEmployeeID")
    private Employee adminApprovalEmployeeID;

    @Column(name = "AdminApprovalDate")
    private Instant adminApprovalDate;

    @Lob
    @Column(name = "Description", nullable = false)
    private String description;
}