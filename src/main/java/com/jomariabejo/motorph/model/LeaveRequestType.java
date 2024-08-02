package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "leave_request_type", schema = "payroll_system")
@NamedQueries(
        @NamedQuery(
                name = "findMaxCreditsByLeaveTypeName",
                query = "SELECT lrt.maxCredits FROM LeaveRequestType lrt WHERE lrt.leaveTypeName = :leaveTypeName"
        )
)
public class LeaveRequestType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveTypeID", nullable = false)
    private Integer id;

    @Column(name = "LeaveTypeName", length = 50)
    private String leaveTypeName;

    @Column(name = "MaxCredits")
    private Integer maxCredits;

}