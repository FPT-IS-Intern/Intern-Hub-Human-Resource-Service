package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ticket_approvals", schema = "schema_hrm")
public class TicketApproval extends AuditEntity {
    @Id
    @Column(name = "approval_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Size(max = 50)
    @Column(name = "action", length = 50)
    private String action;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @Column(name = "action_at")
    private long actionAt;

    @Column(name = "created_at")
    private Long createdAt;

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


}