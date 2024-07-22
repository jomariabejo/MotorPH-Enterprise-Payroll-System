package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "department", schema = "payroll_system")
public class Department {
    @Id
    @Column(name = "DepartmentID", nullable = false)
    private Integer id;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @Lob
    @Column(name = "Description")
    private String description;

}