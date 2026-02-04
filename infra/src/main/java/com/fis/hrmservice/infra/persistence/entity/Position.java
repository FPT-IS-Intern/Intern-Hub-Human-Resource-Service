package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "positions", schema = "schema_hrm")
public class Position extends AuditEntity {
  @Id
  @Column(name = "position_id", nullable = false)
  private Long id;

  @Size(max = 10)
  @Column(name = "name", length = 10)
  private String name;

  @Column(name = "description", length = Integer.MAX_VALUE)
  private String description;

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
