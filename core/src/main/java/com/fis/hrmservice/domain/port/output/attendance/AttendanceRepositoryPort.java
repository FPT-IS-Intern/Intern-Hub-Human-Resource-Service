package com.fis.hrmservice.domain.port.output.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import java.time.LocalDate;
import java.util.Optional;

public interface AttendanceRepositoryPort {
  AttendanceLogModel save(AttendanceLogModel attendanceLog);

  Optional<AttendanceLogModel> findByUserIdAndDate(Long userId, LocalDate workDate);

  AttendanceLogModel update(AttendanceLogModel attendanceLog);
}
