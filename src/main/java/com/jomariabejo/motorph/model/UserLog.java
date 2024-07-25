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
@Table(name = "user_log", schema = "payroll_system", indexes = {
        @Index(name = "idx_user_log_user", columnList = "UserID")
})
public class UserLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @Column(name = "Action", nullable = false)
    private String action;

    @Column(name = "IPAddress", length = 50)
    private String iPAddress;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "LogDateTime", nullable = false)
    private Instant logDateTime;

}