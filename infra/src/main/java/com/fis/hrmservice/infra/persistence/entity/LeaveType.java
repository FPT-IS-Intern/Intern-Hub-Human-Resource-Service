package com.fis.hrmservice.infra.persistence.entity;

import com.intern.hub.starter.security.entity.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "leave_types", schema = "schema_hrm")
public class LeaveType extends AuditEntity {
    @Id
    @Column(name = "leave_type_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "type_name")
    private String typeName;

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

    @Size(max = 50)
    @Column(name = "status", length = 50)
    private String status;


}