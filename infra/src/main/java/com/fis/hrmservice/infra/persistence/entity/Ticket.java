package com.fis.hrmservice.infra.persistence.entity;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket extends AuditEntity {
  @Id
  @Column(name = "ticket_id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "ticket_type_id", nullable = false)
  private TicketType ticketType;

  @Column(name = "start_at")
  private LocalDate startAt;

  @Column(name = "end_at")
  private LocalDate endAt;

  @Column(name = "reason", length = Integer.MAX_VALUE)
  private String reason;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20)
  private TicketStatus status;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @ColumnDefault("'{}'")
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "user_info_temp")
  private Map<String, Object> userInfoTemp;
}
