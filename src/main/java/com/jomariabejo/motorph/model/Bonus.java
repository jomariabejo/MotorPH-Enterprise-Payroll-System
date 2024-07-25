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
@Table(name = "bonus", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee", columnList = "EmployeeNumber")
})
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BonusID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "EmployeeNumber", nullable = false)
    private Employee employeeNumber;

    @Column(name = "BonusAmount", nullable = false, precision = 18, scale = 4)
    private BigDecimal bonusAmount;

    @Column(name = "BonusDate", nullable = false)
    private LocalDate bonusDate;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "CreatedBy", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "CreatedDate", nullable = false)
    private Instant createdDate;

    @Column(name = "LastModifiedBy", length = 100)
    private String lastModifiedBy;

    @Column(name = "LastModifiedDate")
    private Instant lastModifiedDate;

}