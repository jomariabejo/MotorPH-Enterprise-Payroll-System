package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "department", schema = "payroll_system",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"DepartmentName"})})
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DepartmentID", nullable = false)
    private Integer id;

    @Column(name = "DepartmentName", nullable = false, length = 100)
    private String departmentName;

    @Lob
    @Column(name = "Description")
    private String description;

}