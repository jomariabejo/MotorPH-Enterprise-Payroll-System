package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "overtime_request", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee_overtime", columnList = "EmployeeID")
})
@NamedQueries(
        @NamedQuery(
                name = "isEmployeeAlreadyHaveRequest",
                query = "SELECT ovt_req FROM OvertimeRequest ovt_req WHERE ovt_req.employeeID = :employee AND ovt_req.overtimeDate = :date"
        )
)
public class OvertimeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employeeID;

    @Column(name = "DateRequested", nullable = false)
    private Date dateRequested;

    @Column(name = "OvertimeDate", nullable = false)
    private Date overtimeDate;

    @Column(name = "HoursRequested", nullable = false, precision = 8, scale = 4)
    private BigDecimal hoursRequested;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "HRRemarks")
    private String hRRemarks;

}