package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_employee_leave_balance", schema = "payroll_system")
public class VwEmployeeLeaveBalance {
    @Column(name = "EmployeeID", nullable = false)
    private Integer employeeID;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "LeaveTypeName", length = 50)
    private String leaveTypeName;

    @Column(name = "Balance", nullable = false, precision = 8, scale = 2)
    private BigDecimal balance;

}