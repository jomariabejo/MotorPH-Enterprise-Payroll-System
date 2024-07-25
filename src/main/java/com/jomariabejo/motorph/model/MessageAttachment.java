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
@Table(name = "message_attachments", schema = "payroll_system", indexes = {
        @Index(name = "idx_message", columnList = "message_id")
})
public class MessageAttachment {
    @Id
    @Column(name = "attachment_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "upload_timestamp", nullable = false)
    private Instant uploadTimestamp;

}