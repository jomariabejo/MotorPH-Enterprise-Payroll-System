package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reimbursement_transactions", schema = "payroll_system", indexes = {
        @Index(name = "idx_reimbursement_transactions_request", columnList = "RequestID")
})
public class ReimbursementTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "RequestID", nullable = false)
    private ReimbursementRequest requestID;

    @Column(name = "TransactionDate", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "Amount", nullable = false, precision = 18, scale = 4)
    private BigDecimal amount;

    @Column(name = "PaidTo", nullable = false, length = 100)
    private String paidTo;

    @Column(name = "PaymentMethod", length = 50)
    private String paymentMethod;

    @Lob
    @Column(name = "Details")
    private String details;

}