package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_active_employees", schema = "payroll_system")
public class VwActiveEmployee {
    @Column(name = "EmployeeNumber", nullable = false)
    private Integer employeeNumber;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "PositionID", nullable = false)
    private Integer positionID;

    @Column(name = "PositionName", nullable = false, length = 100)
    private String positionName;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

}