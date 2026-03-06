package com.fis.hrmservice.infra.persistence.adapter.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import com.fis.hrmservice.infra.persistence.repository.attendance.AttendanceLogRepository;
import com.fis.hrmservice.infra.persistence.repository.user.UserJpaRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.intern.hub.library.common.dto.PaginatedData;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

/**
 * Mock implementation of AttendanceRepositoryPort using in-memory storage. TODO: Replace with real
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
    List<AttendanceLogModel> logs = findAllByUserIdAndDate(userId, workDate);
    return logs.isEmpty() ? Optional.empty() : Optional.of(logs.get(0));
  }

  @Override
  public List<AttendanceLogModel> findAllByUserIdAndDate(Long userId, LocalDate workDate) {
    return attendanceLogRepository.findAllByUser_IdAndWorkDateOrderByCheckInTimeDesc(userId, workDate).stream()
        .map(this::toModel)
        .toList();
  }

  @Override
  public List<AttendanceLogModel> findAllOpenByDate(LocalDate workDate) {
    return attendanceLogRepository.findAllByWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeAsc(workDate).stream()
        .map(this::toModel)
        .toList();
  }

  @Override
  public Optional<AttendanceLogModel> findOpenSessionByUserAndDate(Long userId, LocalDate workDate) {
    return attendanceLogRepository
        .findFirstByUser_IdAndWorkDateAndCheckOutTimeIsNullOrderByCheckInTimeDesc(userId, workDate)
        .map(this::toModel);
  }

  @Override
  public Optional<AttendanceLogModel> findLatestByUserAndDate(Long userId, LocalDate workDate) {
    return attendanceLogRepository
        .findFirstByUser_IdAndWorkDateOrderByCheckInTimeDesc(userId, workDate)
        .map(this::toModel);
  }

  @Override
  public boolean existsCheckedInBranchByUserAndDate(Long userId, LocalDate workDate, UUID branchId) {
    return attendanceLogRepository.existsByUser_IdAndWorkDateAndCheckInBranchId(userId, workDate, branchId);
  }

  @Override
  public AttendanceLogModel update(AttendanceLogModel attendanceLogModel) {
    return save(attendanceLogModel);
  }

  @Override
  public PaginatedData<AttendanceLogModel> filterAttendanceLogs(FilterAttendanceCommand filterAttendanceCommand, int page, int size) {

    Page<AttendanceLog> attendanceLogPage = attendanceLogRepository.filterAttendanceLogs(
        filterAttendanceCommand.getNameOrEmail(),
        filterAttendanceCommand.getAttendanceStatus(),
        PageRequest.of(page, size));

    List<AttendanceLogModel> attendanceLogModels = attendanceLogPage.getContent().stream()
        .map(this::toModel)
        .toList();

    return PaginatedData.<AttendanceLogModel>builder()
        .items(attendanceLogModels)
        .totalItems(attendanceLogPage.getTotalElements())
        .totalPages(attendanceLogPage.getTotalPages())
        .build();
  }

  @Override
  public Long getCheckInOnTimePercent() {
    return 0L;
  }

  @Override
  public Long getCheckInLateTimePercent() {
    return 0L;
  }

  @Override
  public Long getNotAttendancePercent() {
    return 0L;
  }

  @Override
  public List<AttendanceLogModel> filterAttendance(String keyword, String attendanceStatus) {
    return List.of();
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
    // Convert long timestamp to LocalDateTime using Vietnam timezone
    if (model.getCheckInTime() > 0) {
      entity.setCheckInTime(
          LocalDateTime.ofInstant(
              Instant.ofEpochMilli(model.getCheckInTime()), CoreConstant.VIETNAM_ZONE));
    }
    if (model.getCheckOutTime() > 0) {
      entity.setCheckOutTime(
          LocalDateTime.ofInstant(
              Instant.ofEpochMilli(model.getCheckOutTime()), CoreConstant.VIETNAM_ZONE));
    }
    entity.setAttendanceStatus(model.getAttendanceStatus());
    entity.setSource(model.getSource());
    entity.setCheckInBranchId(model.getCheckInBranchId());
    entity.setCheckOutBranchId(model.getCheckOutBranchId());
    // Audit fields are handled by AuditEntity listener or manually if needed
    return entity;
  }

  private AttendanceLogModel toModel(AttendanceLog entity) {
    if (entity == null) {
      return null;
    }
    return AttendanceLogModel.builder()
        .attendanceId(entity.getId() != null ? entity.getId() : 0L)
        .user(
            entity.getUser() != null
                ? com.fis.hrmservice.domain.model.user.UserModel.builder()
                    .userId(entity.getUser().getId())
                    .fullName(entity.getUser().getFullName())
                    .companyEmail(entity.getUser().getCompanyEmail())
                    .department(entity.getUser().getDepartment() != null
                        ? entity.getUser().getDepartment().getName()
                        : null)
                    .build()
                : null)
        .workDate(entity.getWorkDate())
        .checkInTime(
            entity.getCheckInTime() != null
                ? entity
                    .getCheckInTime()
                    .atZone(CoreConstant.VIETNAM_ZONE)
                    .toInstant()
                    .toEpochMilli()
                : 0L)
        .checkOutTime(
            entity.getCheckOutTime() != null
                ? entity
                    .getCheckOutTime()
                    .atZone(CoreConstant.VIETNAM_ZONE)
                    .toInstant()
                    .toEpochMilli()
                : 0L)
        .attendanceStatus(entity.getAttendanceStatus())
        .source(entity.getSource())
        .checkInBranchId(entity.getCheckInBranchId())
        .checkOutBranchId(entity.getCheckOutBranchId())
        .build();
  }
}
