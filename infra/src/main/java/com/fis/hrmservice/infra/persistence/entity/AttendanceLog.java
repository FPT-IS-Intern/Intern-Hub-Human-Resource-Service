package com.fis.hrmservice.infra.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "attendance_logs", schema = "schema_hrm")
public class AttendanceLog {
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

    @Size(max = 20)
    @Column(name = "attendance_status", length = 20)
    private String attendanceStatus;

    @Size(max = 50)
    @Column(name = "source", length = 50)
    private String source;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "version")
    private Integer version;

    @Size(max = 255)
    @Column(name = "created_by")
    private String createdBy;

    @Size(max = 255)
    @Column(name = "updated_by")
    private String updatedBy;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;


}