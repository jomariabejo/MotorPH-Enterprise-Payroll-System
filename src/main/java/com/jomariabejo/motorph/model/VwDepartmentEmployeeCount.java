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
@Table(name = "vw_department_employee_count", schema = "payroll_system")
public class VwDepartmentEmployeeCount {
    @Column(name = "DepartmentID", nullable = false)
    private Integer departmentID;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "EmployeeCount", nullable = false)
    private Long employeeCount;

}