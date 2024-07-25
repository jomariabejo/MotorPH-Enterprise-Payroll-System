package com.jomariabejo.motorph.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;

/**
 * Mapping for DB view
 */
@Getter
@Setter
@Entity
@Immutable
@Table(name = "vw_employee_details", schema = "payroll_system")
public class VwEmployeeDetail {
    @Column(name = "EmployeeNumber", nullable = false)
    private Integer employeeNumber;

    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "FirstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "Address", nullable = false)
    private String address;

    @Column(name = "PhoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "PositionName", nullable = false, length = 100)
    private String positionName;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

}