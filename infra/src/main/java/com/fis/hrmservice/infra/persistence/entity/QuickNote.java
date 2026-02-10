package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "quick_notes")
public class QuickNote extends AuditEntity {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "intern_id", nullable = false)
  private User intern;

//  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "writer_id", nullable = false)
  private User writer;

  @NotNull
  @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
  private String content;

  @NotNull
  @Column(name = "write_date", nullable = false)
  private LocalDate writeDate;

  @Size(max = 50)
  @Column(name = "status", length = 50)
  private String status;

  @Column(name = "version")
  private Integer version;

  @ColumnDefault("false")
  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Column(name = "created_by")
  private Long createdBy;
}
