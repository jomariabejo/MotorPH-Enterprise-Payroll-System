package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "leave_request_type", schema = "payroll_system")
public class LeaveRequestType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveTypeID", nullable = false)
    private Integer id;

    @Column(name = "LeaveTypeName", length = 50)
    private String leaveTypeName;

    @Column(name = "MaxCredits", precision = 8, scale = 2)
    private BigDecimal maxCredits;

}