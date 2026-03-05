package com.fis.hrmservice.domain.port.output.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepositoryPort {
  AttendanceLogModel save(AttendanceLogModel attendanceLog);

  Optional<AttendanceLogModel> findByUserIdAndDate(Long userId, LocalDate workDate);

  List<AttendanceLogModel> findAllByUserIdAndDate(Long userId, LocalDate workDate);

  List<AttendanceLogModel> findAllOpenByDate(LocalDate workDate);

  Optional<AttendanceLogModel> findOpenSessionByUserAndDate(Long userId, LocalDate workDate);

  Optional<AttendanceLogModel> findLatestByUserAndDate(Long userId, LocalDate workDate);

  boolean existsCheckedInBranchByUserAndDate(Long userId, LocalDate workDate, UUID branchId);

  AttendanceLogModel update(AttendanceLogModel attendanceLog);

  Long getCheckInOnTimePercent();

  Long getCheckInLateTimePercent();

  Long getNotAttendancePercent();

  List<AttendanceLogModel> filterAttendance(String keyword, String attendanceStatus);
}
