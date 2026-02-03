package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "schema_hrm")
public class User extends AuditEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)

    //tránh sau khi mapping position bằng null;
    @JoinColumn(name = "position_id", updatable = false)
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private User mentor;

    @Size(max = 100)
    @Column(name = "full_name", length = 100)
    private String fullName;

    @Size(max = 12)
    @Column(name = "id_number", length = 12)
    private String idNumber;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 100)
    @Column(name = "company_email", length = 100)
    private String companyEmail;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;

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

    @Column(name = "internship_start_date")
    private LocalDate internshipStartDate;

    @Column(name = "internship_end_date")
    private LocalDate internshipEndDate;

    @NotNull
    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "sys_status", length = 50)
    private String sysStatus;
}