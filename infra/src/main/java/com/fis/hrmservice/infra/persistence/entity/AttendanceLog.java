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
@Table(name = "attendance_logs", schema = "schema_hrm")
public class AttendanceLog extends AuditEntity {
    @Id
    @Column(name = "attendance_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "work_date")
    private long workDate;

    @Column(name = "check_in_time")
    private long checkInTime;

    @Column(name = "check_out_time")
    private long checkOutTime;

    @Size(max = 20)
    @Column(name = "attendance_status", length = 20)
    private String attendanceStatus;

    @Size(max = 50)
    @Column(name = "source", length = 50)
    private String source;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

    @JoinColumn(name = "created_by")
    private Long createdBy;

    @JoinColumn(name = "updated_by")
    private Long updatedBy;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

}