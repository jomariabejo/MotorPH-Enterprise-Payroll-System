package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payroll_approval", schema = "payroll_system", indexes = {
        @Index(name = "idx_payroll_approval", columnList = "PayrollID")
})
public class PayrollApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApprovalID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayrollID", nullable = false)
    private Payroll payrollID;

    @Column(name = "ApproverID", nullable = false)
    private Integer approverID;

    @Column(name = "ApprovalDate", nullable = false)
    private Instant approvalDate;

    @Column(name = "Status", nullable = false, length = 50)
    private String status;

}