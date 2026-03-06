package com.fis.hrmservice.domain.usecase.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.intern.hub.library.common.dto.PaginatedData;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceUseCase {
  AttendanceStatusModel getAttendanceStatus(
      Long userId, LocalDate workDate, String clientIp, Double latitude, Double longitude);

  AttendanceLogModel checkIn(CheckInCommand command);

  AttendanceLogModel checkOut(CheckOutCommand command);

  PaginatedData<AttendanceLogModel> filterAttendance(String nameOrEmail, String attendanceStatus, int page, int size);

  int autoCheckoutOpenAttendances(long checkOutTimeMillis);

  List<AttendanceInWeekCommand> getAttendanceInWeekByUserId(Long userId);
}
