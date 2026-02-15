package com.fis.hrmservice.infra.persistence.adapter.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import com.fis.hrmservice.infra.persistence.repository.attendance.AttendanceLogRepository;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * Mock implementation of AttendanceRepositoryPort using in-memory storage.
 * TODO: Replace with real
 * JPA repository implementation when database is ready.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryAdapter implements AttendanceRepositoryPort {

  private final AttendanceLogRepository attendanceLogRepository;
  private final UserJpaRepository userJpaRepository;

  @Override
  public AttendanceLogModel save(AttendanceLogModel attendanceLogModel) {
    AttendanceLog entity = toEntity(attendanceLogModel);
    AttendanceLog savedEntity = attendanceLogRepository.save(entity);
    return toModel(savedEntity);
  }

  @Override
  public Optional<AttendanceLogModel> findByUserIdAndDate(Long userId, LocalDate workDate) {
    return attendanceLogRepository.findByUser_IdAndWorkDate(userId, workDate).map(this::toModel);
  }

  @Override
  public AttendanceLogModel update(AttendanceLogModel attendanceLogModel) {
    return save(attendanceLogModel);
  }

  private AttendanceLog toEntity(AttendanceLogModel model) {
    if (model == null) {
      return null;
    }
    AttendanceLog entity = new AttendanceLog();
    if (model.getAttendanceId() > 0) {
      entity.setId(model.getAttendanceId());
    }
    if (model.getUser() != null) {
      userJpaRepository.findById(model.getUser().getUserId()).ifPresent(entity::setUser);
    }
    entity.setWorkDate(model.getWorkDate());
    // Convert long timestamp to Instant
    if (model.getCheckInTime() > 0) {
      entity.setCheckInTime(java.time.Instant.ofEpochMilli(model.getCheckInTime()));
    }
    if (model.getCheckOutTime() > 0) {
      entity.setCheckOutTime(java.time.Instant.ofEpochMilli(model.getCheckOutTime()));
    }
    entity.setAttendanceStatus(model.getAttendanceStatus());
    entity.setSource(model.getSource());
    // Audit fields are handled by AuditEntity listener or manually if needed
    return entity;
  }

  private AttendanceLogModel toModel(AttendanceLog entity) {
    if (entity == null) {
      return null;
    }
    return AttendanceLogModel.builder()
        .attendanceId(entity.getId() != null ? entity.getId() : 0L)
        .user(entity.getUser() != null ? com.fis.hrmservice.domain.model.user.UserModel.builder()
            .userId(entity.getUser().getId())
            .fullName(entity.getUser().getFullName())
            .companyEmail(entity.getUser().getCompanyEmail())
            .build() : null)
        .workDate(entity.getWorkDate())
        .checkInTime(entity.getCheckInTime() != null ? entity.getCheckInTime().toEpochMilli() : 0L)
        .checkOutTime(entity.getCheckOutTime() != null ? entity.getCheckOutTime().toEpochMilli() : 0L)
        .attendanceStatus(entity.getAttendanceStatus())
        .source(entity.getSource())
        .build();
  }
}
