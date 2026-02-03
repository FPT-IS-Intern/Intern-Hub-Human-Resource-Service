package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "feedback_attachments", schema = "schema_hrm")
public class FeedbackAttachment extends AuditEntity {
    @Id
    @Column(name = "attachment_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private FeedbackMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url", length = Integer.MAX_VALUE)
    private String fileUrl;

    @Column(name = "uploaded_at")
    private long uploadedAt;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

}