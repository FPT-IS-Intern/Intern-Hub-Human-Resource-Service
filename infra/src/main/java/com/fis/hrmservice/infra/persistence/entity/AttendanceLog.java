package com.fis.hrmservice.infra.persistence.entity;

import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attendance_logs")
public class AttendanceLog extends AuditEntity {
  @Id
  @Column(name = "attendance_id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "work_date")
  private LocalDate workDate;

  @Column(name = "check_in_time")
  private Long checkInTime;

  @Column(name = "check_out_time")
  private Long checkOutTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "attendance_status", length = 20)
  private AttendanceStatus attendanceStatus;

  @Size(max = 50)
  @Column(name = "source", length = 50)
  private String source;

  @Column(name = "check_in_branch_id")
  private UUID checkInBranchId;

  @Column(name = "check_out_branch_id")
  private UUID checkOutBranchId;

  @Column(name = "created_at")
  private Long createdAt;

  @Column(name = "updated_at")
  private Long updatedAt;

  @Column(name = "version")
  private Integer version;

  @Column(name = "created_by")
  private Long createdBy;

  @Column(name = "updated_by")
  private Long updatedBy;

  @Size(max = 50)
  @Column(name = "status", length = 50)
  private String status;
}
