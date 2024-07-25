package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payroll_changes", schema = "payroll_system", indexes = {
        @Index(name = "idx_payroll_changes", columnList = "PayrollID")
})
public class PayrollChange {
    @Id
    @Column(name = "ChangeID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "PayrollID", nullable = false)
    private Payroll payrollID;

    @Column(name = "ModifiedBy", nullable = false, length = 100)
    private String modifiedBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
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