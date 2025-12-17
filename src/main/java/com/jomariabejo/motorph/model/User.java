package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "payroll_system", uniqueConstraints = {
        @UniqueConstraint(name = "idx_username", columnNames = {"Username"}),
        @UniqueConstraint(name = "idx_email", columnNames = {"Email"})
})
@NamedQueries({
        @NamedQuery(
                name = "findUser",
                query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"
        ),
        @NamedQuery(
                name = "findEmployeeEmail",
                query = "SELECT u FROM User u WHERE u.employee = :employee"
        )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Integer id;

    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "RoleID", nullable = false)
    private Role roleID;

    /**
     * Multi-role support: additional roles assigned to the user.
     * user.roleID remains the primary/default role for backwards compatibility.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            schema = "payroll_system",
            joinColumns = @JoinColumn(name = "UserID", referencedColumnName = "UserID"),
            inverseJoinColumns = @JoinColumn(name = "RoleID", referencedColumnName = "RoleID")
    )
    private Set<Role> roles = new HashSet<>();

    @ColumnDefault("'Active'")
    @Column(name = "Status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeId", nullable = false)
    private Employee employee;

}