package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "remote_requests", schema = "schema_hrm")
public class RemoteRequest extends AuditEntity {
    @Id
    @Column(name = "ticket_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket tickets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_location_id")
    private WorkLocation workLocation;

    @Column(name = "start_time")
    private long startTime;

    @Column(name = "end_time")
    private long endTime;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

}