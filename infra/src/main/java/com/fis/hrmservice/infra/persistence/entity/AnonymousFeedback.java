package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "anonymous_feedbacks", schema = "schema_hrm")
public class AnonymousFeedback extends AuditEntity {
    @Id
    @Column(name = "feedback_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "feedback_type_id", nullable = false)
    private FeedbackType feedbackType;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private User creator;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @JoinColumn(name = "created_by")
    private Long createdBy;

    @JoinColumn(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;
}