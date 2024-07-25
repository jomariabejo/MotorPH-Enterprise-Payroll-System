package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_reimbursement_summary_view", schema = "payroll_system")
public class VwReimbursementSummaryView {
    @Column(name = "RequestID", nullable = false)
    private Integer requestID;

    @Column(name = "EmployeeNumber", nullable = false)
    private Integer employeeNumber;

    @Column(name = "EmployeeName", length = 201)
    private String employeeName;

    @Column(name = "RequestDate", nullable = false)
    private LocalDate requestDate;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

    @Column(name = "ApprovedBy")
    private Integer approvedBy;

    @Column(name = "ApprovedDate")
    private Instant approvedDate;

    @Column(name = "ProcessedBy")
    private Integer processedBy;

    @Column(name = "ProcessedDate")
    private Instant processedDate;

    @Column(name = "TransactionID")
    private Integer transactionID;

    @Column(name = "TransactionDate")
    private LocalDate transactionDate;

    @Column(name = "TransactionAmount", precision = 18, scale = 4)
    private BigDecimal transactionAmount;

    @Column(name = "PaidTo", length = 100)
    private String paidTo;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Lob
    @Column(name = "Details")
    private String details;

}