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
@Table(name = "payroll_transactions", schema = "payroll_system", indexes = {
        @Index(name = "idx_payroll_transactions", columnList = "PayrollID, PayslipID")
})
public class PayrollTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransactionID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayrollID", nullable = false)
    private Payroll payrollID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayslipID", nullable = false)
    private Payslip payslipID;

    @Column(name = "TransactionType", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "TransactionAmount", nullable = false, precision = 18, scale = 4)
    private BigDecimal transactionAmount;

    @Column(name = "TransactionDate", nullable = false)
    private LocalDate transactionDate;

    @Lob
    @Column(name = "Description")
    private String description;

}