package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "evidences", schema = "schema_hrm")
public class Evidence extends AuditEntity {
    @Id
    @Column(name = "evidence_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Size(max = 255)
    @Column(name = "evidence_folder")
    private String evidenceFolder;

    @Column(name = "evidence_url", length = Integer.MAX_VALUE)
    private String evidenceUrl;

    @Column(name = "uploaded_at")
    private long uploadedAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Size(max = 255)
    @Column(name = "created_by")
    private Long createdBy;

    @Size(max = 255)
    @Column(name = "updated_by")
    private Long updatedBy;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at")
    private Long createdAt;

}