package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "position", schema = "payroll_system", indexes = {
        @Index(name = "idx_department", columnList = "DepartmentID")
})
public class Position {
    @Id
    @Column(name = "PositionID", nullable = false)
    private Integer id;

    @Column(name = "PositionName", nullable = false, length = 100)
    private String positionName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DepartmentID", nullable = false)
    private Department departmentID;

    @Lob
    @Column(name = "Description")
    private String description;

}