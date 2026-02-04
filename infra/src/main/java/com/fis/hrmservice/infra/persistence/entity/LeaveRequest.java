package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "leave_requests", schema = "schema_hrm")
public class LeaveRequest extends AuditEntity {
  @Id
  @Column(name = "ticket_id", nullable = false)
  private long id;

  @MapsId
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_id", nullable = false)
  private Ticket tickets;

  @Column(name = "total_days")
  private Integer totalDays;

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
}
