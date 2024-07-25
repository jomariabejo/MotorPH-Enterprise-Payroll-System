package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "leave_balance", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_leave_balance", columnList = "EmployeeID"),
        @Index(name = "idx_leave_type_balance", columnList = "LeaveTypeID")
})
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BalanceID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LeaveTypeID", nullable = false)
    private LeaveRequestType leaveTypeID;

    @ColumnDefault("0.00")
    @Column(name = "Balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal balance;

}