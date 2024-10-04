package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "reimbursement_requests", schema = "payroll_system", indexes = {
        @Index(name = "idx_reimbursement_requests_employee", columnList = "EmployeeNumber")
})
@NamedQueries({
        @NamedQuery(
                name = "fetchEmployeeYearsOfReimbursement",
                query = "SELECT DISTINCT YEAR(RR.requestDate) AS Years FROM ReimbursementRequest RR WHERE RR.employeeNumber =: reimbursementOwner ORDER BY YEAR(RR.requestDate) DESC"
        ),
        @NamedQuery(
                name = "fetchReimbursementsByEmployeeAndYear",
                query = "SELECT RR FROM ReimbursementRequest RR WHERE RR.employeeNumber =: reimbursementOwner AND YEAR(RR.requestDate) = :year ORDER BY YEAR(RR.requestDate) DESC"
        )
})
public class ReimbursementRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID", nullable = false)
    private Integer reimbursementRequestId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "EmployeeNumber", nullable = false)
    private Employee employeeNumber;

    @Column(name = "RequestDate", nullable = false)
    private Date requestDate;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;

    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "ProcessedBy")
    private Employee processedBy;

    @Column(name = "ProcessedDate")
    private Date processedDate;

    // Custom getter for employee number
    public Integer getEmployeeNumber() {
        return employeeNumber != null ? employeeNumber.getEmployeeNumber() : null;
    }
}