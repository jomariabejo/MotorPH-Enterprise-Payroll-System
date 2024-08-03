package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "leave_request", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_leave_request", columnList = "EmployeeID"),
        @Index(name = "idx_leave_type", columnList = "LeaveTypeID")
})
@NamedQueries({
        @NamedQuery(
                name = "isLeaveRequestDatesOverlap",
                query = "SELECT CASE " +
                        "WHEN COUNT(lrt) > 0 THEN TRUE " +
                        "ELSE FALSE " +
                        "END " +
                        "FROM LeaveRequest lrt " +
                        "WHERE lrt.employeeID.id = :employeeId " +
                        "AND (lrt.startDate <= :endDate AND lrt.endDate >= :startDate)"
        )
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

    @Column(name = "DateRequested", nullable = false, updatable = false)
    private LocalDateTime dateRequested;

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

    @Column(name = "AdminApprovalDate", nullable = true, updatable = true)
    private LocalDateTime adminApprovalDate;

    @Lob
    @Column(name = "Description", nullable = false)
    private String description;

    @PrePersist
    protected void onCreate() {
        if (dateRequested == null) {
            dateRequested = LocalDateTime.now();
        }
    }
}