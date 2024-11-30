package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
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
                        "WHERE lrt.employeeID.employeeNumber = :employeeId " +
                        "AND (lrt.startDate <= :endDate AND lrt.endDate >= :startDate)"
        ),
        @NamedQuery(
                name = "getYearsOfLeaveRequest",
                query = "SELECT DISTINCT YEAR(lr.dateRequested) AS Year\n" +
                        "FROM LeaveRequest lr\n" +
                        "WHERE lr.employeeID = :employeeID\n " +
                        "ORDER BY Year(lr.dateRequested) DESC"
        ),
        @NamedQuery(
                name = "fetchLeaveRequestForEmployee",
                query = "SELECT lr\n" +
                        "FROM LeaveRequest lr\n" +
                        "JOIN LeaveRequestType lt ON lt.id = lr.leaveTypeID.id\n" +
                        "WHERE lr.employeeID = :employee\n" +
                        "  AND MONTHNAME(lr.dateRequested) = :month\n" +
                        "  AND YEAR(lr.dateRequested) = :year\n" +
                        "  AND lr.status = :status\n" +
                        "  AND lt.leaveTypeName = :leaveTypeName\n" +
                        "ORDER BY lr.dateRequested DESC\n"),
//        @NamedQuery(
//                name = "countTotalPaidLeaveDaysByEmployeeAndPeriod",
//                query = "SELECT SUM(LEAST(DATEDIFF(COALESCE(lr.endDate, :payslipEndPeriod), " +
//                        "GREATEST(lr.startDate, :payslipStartPeriod)) + 1, " +
//                        "DATEDIFF(:payslipEndPeriod, :payslipStartPeriod) + 1)) " +
//                        "FROM LeaveRequest lr " +
//                        "WHERE lr.employeeID = :employee " +
//                        "AND lr.isPaid = true " +
//                        "AND (lr.startDate <= :payslipEndPeriod AND lr.endDate >= :payslipStartPeriod)"
//        ),
        @NamedQuery(
                name = "findLeaveRequestsByStatus",
                query = "SELECT lr FROM LeaveRequest lr WHERE lr.status = :status ORDER BY lr.dateRequested"
        ),
        @NamedQuery(
                name = "findLeaveRequestsByEmployeeNameOrEmployeeNumber",
                query = "SELECT lr FROM LeaveRequest lr " +
                        "JOIN lr.employeeID e " +
                        "WHERE (LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')) " +
                        "AND LOWER(e.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) " +
                        "OR CAST(e.employeeNumber AS string) LIKE LOWER(CONCAT('%', :searchedText, '%'))"
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
    private Date dateRequested;

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

    @Column(name = "isPaid", columnDefinition = "boolean default false")
    private Boolean isPaid;

    public LeaveRequest() {

    }

    @PrePersist
    protected void onCreate() {
        if (dateRequested == null) {
            dateRequested = Date.valueOf(LocalDate.now());
        }
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", employeeID=" + employeeID +
                ", leaveTypeID=" + leaveTypeID +
                ", dateRequested=" + dateRequested +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                ", adminApprovalEmployeeID=" + adminApprovalEmployeeID +
                ", adminApprovalDate=" + adminApprovalDate +
                ", description='" + description + '\'' +
                '}';
    }

    public String getEmployeeName() {
        if (leaveTypeID != null) {
            return employeeID.getFirstName() + " " + employeeID.getLastName();
        }
        return "";
    }

    public String getLeaveType() {
        if (leaveTypeID != null) {
            return leaveTypeID.getLeaveTypeName();
        }
        return "";
    }
}