package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reimbursement_requests", schema = "payroll_system", indexes = {
        @Index(name = "idx_reimbursement_requests_employee", columnList = "EmployeeNumber")
})
public class ReimbursementRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "EmployeeNumber", nullable = false)
    private Employee employeeNumber;

    @Column(name = "RequestDate", nullable = false)
    private LocalDate requestDate;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;

    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "ApprovedBy")
    private Employee approvedBy;

    @Column(name = "ApprovedDate")
    private Instant approvedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "ProcessedBy")
    private Employee processedBy;

    @Column(name = "ProcessedDate")
    private Instant processedDate;

}