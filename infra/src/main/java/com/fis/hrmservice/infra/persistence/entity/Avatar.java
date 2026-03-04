package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avatars")
public class Avatar extends AuditEntity {
  @Id
  @Column(name = "avatar_id", nullable = false)
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "avatar_url", length = Integer.MAX_VALUE)
  private String avatarUrl;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "version")
  private Integer version;

  @Column(name = "status", length = 50)
  private String status;

  @Column(name = "file_type", length = 100)
  private String fileType;

  @Column(name = "file_size")
  private Long fileSize;
}
