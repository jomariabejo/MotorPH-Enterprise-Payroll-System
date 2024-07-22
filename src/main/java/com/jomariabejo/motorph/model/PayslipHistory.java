package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payslip_history", schema = "payroll_system", indexes = {
        @Index(name = "idx_payslip_history", columnList = "PayslipID")
})
public class PayslipHistory {
    @Id
    @Column(name = "HistoryID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PayslipID", nullable = false)
    private Payslip payslipID;

    @Column(name = "ModifiedBy", nullable = false, length = 100)
    private String modifiedBy;

    @Column(name = "ModificationDate", nullable = false)
    private Instant modificationDate;

    @Column(name = "FieldChanged", nullable = false, length = 100)
    private String fieldChanged;

    @Lob
    @Column(name = "OldValue")
    private String oldValue;

    @Lob
    @Column(name = "NewValue")
    private String newValue;

}