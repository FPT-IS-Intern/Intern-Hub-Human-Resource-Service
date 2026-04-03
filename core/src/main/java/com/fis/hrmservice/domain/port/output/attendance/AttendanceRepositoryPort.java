package com.fis.hrmservice.domain.port.output.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.intern.hub.library.common.dto.PaginatedData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepositoryPort {
  AttendanceLogModel save(AttendanceLogModel attendanceLog);

  List<AttendanceLogModel> findAllByUserIdAndDate(Long userId, LocalDate workDate);

  List<AttendanceLogModel> findAllOpenByDate(LocalDate workDate);

  Optional<AttendanceLogModel> findOpenSessionByUserAndDate(Long userId, LocalDate workDate);

  Optional<AttendanceLogModel> findLatestByUserAndDate(Long userId, LocalDate workDate);

  boolean existsCheckedInBranchByUserAndDate(Long userId, LocalDate workDate, UUID branchId);

  boolean existsByUserIdAndWorkDate(Long userId, LocalDate workDate);

  /**
   * Finds the single attendance record for a user on a date.
   * Used for normal reads (e.g. status check).
   */
  Optional<AttendanceLogModel> findByUserAndDate(Long userId, LocalDate workDate);

  /**
   * Finds the attendance record with a pessimistic write lock.
   * Must be used inside a transaction when modifying the record (e.g. ABSENT →
   * CHECK_IN_LATE).
   */
  Optional<AttendanceLogModel> findByUserAndDateForUpdate(Long userId, LocalDate workDate);

  AttendanceLogModel update(AttendanceLogModel attendanceLog);

  PaginatedData<AttendanceLogModel> filterAttendanceLogs(FilterAttendanceCommand command, int page, int size);

  Double getCheckInOnTimePercent(LocalDate fromDate, LocalDate toDate);
  Double getCheckInLateTimePercent(LocalDate fromDate, LocalDate toDate);
  Double getNotAttendancePercent(LocalDate fromDate, LocalDate toDate);

  List<AttendanceInWeekCommand> getAttendanceInWeekByUserId(Long userId);

  Integer totalWorkDate(Long userId);

  Integer totalLateTime(Long userId);
}
