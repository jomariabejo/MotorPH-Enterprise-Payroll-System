package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permission", schema = "payroll_system", uniqueConstraints = {
        @UniqueConstraint(name = "idx_permission_name", columnNames = {"PermissionName"})
})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionID", nullable = false)
    private Integer id;

    @Column(name = "PermissionName", nullable = false, length = 50)
    private String permissionName;

    @Column(name = "Description")
    private String description;

    @Column(name = "Category", length = 100)
    private String category;

}