package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "leave_balance", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_leave_balance", columnList = "EmployeeID"),
        @Index(name = "idx_leave_type_balance", columnList = "LeaveTypeID")
})
@NamedQueries({
        @NamedQuery(
                name = "LeaveBalance.findBalanceByEmployeeAndLeaveType",
                query = "SELECT lb.balance " +
                        "FROM LeaveBalance lb " +
                        "JOIN lb.leaveTypeID lrt " +
                        "WHERE lb.employee.id = :employeeId AND lrt.leaveTypeName = :leaveTypeName"
        )
})
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BalanceID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LeaveTypeID", nullable = false)
    private LeaveRequestType leaveTypeID;

    @ColumnDefault("0")
    @Column(name = "Balance", nullable = false)
    private Integer balance;

}