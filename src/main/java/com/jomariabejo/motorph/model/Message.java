package com.jomariabejo.motorph.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messages", schema = "payroll_system", indexes = {
        @Index(name = "idx_sender", columnList = "sender_employee_id"),
        @Index(name = "idx_recipient", columnList = "recipient_employee_id"),
        @Index(name = "idx_conversation", columnList = "conversation_id")
})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_employee_id", nullable = false)
    private Employee senderEmployee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_employee_id", nullable = false)
    private Employee recipientEmployee;

    @Lob
    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @ColumnDefault("'unread'")
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversation_id", nullable = false)
    private Conversation conversation;

}