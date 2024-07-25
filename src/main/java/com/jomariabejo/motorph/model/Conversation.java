package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "conversation", schema = "payroll_system", indexes = {
        @Index(name = "idx_participant1", columnList = "participant1_employee_id"),
        @Index(name = "idx_participant2", columnList = "participant2_employee_id")
})
public class Conversation {
    @Id
    @Column(name = "conversation_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant1_employee_id", nullable = false)
    private Employee participant1Employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant2_employee_id", nullable = false)
    private Employee participant2Employee;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_message_timestamp", nullable = false)
    private Instant lastMessageTimestamp;

    @Lob
    @Column(name = "last_message_content", nullable = false)
    private String lastMessageContent;

    @ColumnDefault("0")
    @Column(name = "unread_count", nullable = false)
    private Integer unreadCount;

}