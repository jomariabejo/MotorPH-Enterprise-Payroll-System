package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tin_compliance", schema = "payroll_system", indexes = {
        @Index(name = "idx_tin_employee_id", columnList = "EmployeeID")
})
public class TinCompliance {
    @Id
    @Column(name = "TINComplianceID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "TINNumber", nullable = false, length = 20)
    private String tINNumber;

    @Column(name = "DateRegistered", nullable = false)
    private LocalDate dateRegistered;

}