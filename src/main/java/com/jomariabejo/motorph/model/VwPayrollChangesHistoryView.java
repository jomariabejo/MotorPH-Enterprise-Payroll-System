package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_payroll_changes_history_view", schema = "payroll_system")
public class VwPayrollChangesHistoryView {
    @Column(name = "ChangeID", nullable = false)
    private Integer changeID;

    @Column(name = "PayrollID", nullable = false)
    private Integer payrollID;

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

    @Column(name = "PayrollRunDate")
    private LocalDate payrollRunDate;

}