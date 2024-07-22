package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "payroll_system", uniqueConstraints = {
        @UniqueConstraint(name = "idx_role_name", columnNames = {"RoleName"})
})
public class Role {
    @Id
    @Column(name = "RoleID", nullable = false)
    private Integer id;

    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @Column(name = "Description")
    private String description;

}