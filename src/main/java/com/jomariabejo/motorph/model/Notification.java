package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notifications", schema = "payroll_system", indexes = {
        @Index(name = "idx_employee", columnList = "employee_id")
})
public class Notification {
    @Id
    @Column(name = "notification_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Lob
    @Column(name = "notification_type", nullable = false)
    private String notificationType;

    @Lob
    @Column(name = "notification_content", nullable = false)
    private String notificationContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @ColumnDefault("0")
    @Column(name = "read_status", nullable = false)
    private Boolean readStatus = false;

}