package com.fis.hrmservice.domain.usecase.implement.attendance;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.attendance.AttendanceRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.intern.hub.library.common.exception.BadRequestException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AttendanceUseCaseImpl {

  @Autowired
  private AttendanceRepositoryPort attendanceRepository;
  @Autowired
  private UserRepositoryPort userRepository;
  @Autowired
  private Snowflake snowflake;

  private static final int EARLIEST_CHECK_IN_HOUR = 0;
  private static final int EARLIEST_CHECK_IN_MINUTE = 0;
  private static final int STANDARD_CHECK_IN_HOUR = 8;
  private static final int STANDARD_CHECK_IN_MINUTE = 45;
  private static final int STANDARD_CHECK_OUT_HOUR = 17;
  private static final int STANDARD_CHECK_OUT_MINUTE = 15;
  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  /** Get attendance status for a user on a specific date */
  public AttendanceStatusModel getAttendanceStatus(Long userId, LocalDate workDate) {
    log.info("Getting attendance status for userId: {} on date: {}", userId, workDate);

    Optional<AttendanceLogModel> attendanceOpt = attendanceRepository.findByUserIdAndDate(userId, workDate);

    if (attendanceOpt.isEmpty()) {
      // No attendance record for today
      return AttendanceStatusModel.builder()
          .workDate(workDate)
          .checkInTime(null)
          .checkOutTime(null)
          .isCheckInValid(false)
          .isCheckOutValid(false)
          .checkInMessage(null)
          .checkOutMessage(null)
          .build();
    }

    AttendanceLogModel attendance = attendanceOpt.get();

    LocalTime checkInTime = attendance.getCheckInTime() > 0 ? convertToLocalTime(attendance.getCheckInTime()) : null;
    LocalTime checkOutTime = attendance.getCheckOutTime() > 0 ? convertToLocalTime(attendance.getCheckOutTime()) : null;

    boolean isCheckInValid = checkInTime != null
        && !checkInTime.isAfter(LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE));
    boolean isCheckOutValid = checkOutTime != null
        && !checkOutTime.isBefore(LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE));

    return AttendanceStatusModel.builder()
        .workDate(workDate)
        .checkInTime(checkInTime)
        .checkOutTime(checkOutTime)
        .isCheckInValid(isCheckInValid)
        .isCheckOutValid(isCheckOutValid)
        .checkInMessage(generateCheckInMessage(attendance.getCheckInTime()))
        .checkOutMessage(generateCheckOutMessage(attendance.getCheckOutTime()))
        .build();
  }

  /** Process check-in */
  public AttendanceLogModel checkIn(CheckInCommand command) {
    log.info("Processing check-in for userId: {}", command.getUserId());

    // Validate user exists
    UserModel user = userRepository
        .findById(command.getUserId())
        .orElseThrow(() -> new NotFoundException("User không tồn tại"));

    long checkInTimestamp = command.getCheckInTime();
    if (checkInTimestamp <= 0) {
      checkInTimestamp = System.currentTimeMillis();
    }

    // Validate 7:30 AM Constraint
    // LocalTime checkInTime = convertToLocalTime(checkInTimestamp);
    // LocalTime earliestTime = LocalTime.of(EARLIEST_CHECK_IN_HOUR,
    // EARLIEST_CHECK_IN_MINUTE);
    // if (checkInTime.isBefore(earliestTime)) {
    // throw new BadRequestException("Chưa đến giờ điểm danh (7:30)");
    // }

    LocalDate workDate = convertToLocalDate(checkInTimestamp);

    // Check if already checked in today
    Optional<AttendanceLogModel> existingAttendance = attendanceRepository.findByUserIdAndDate(command.getUserId(),
        workDate);

    if (existingAttendance.isPresent() && existingAttendance.get().getCheckInTime() > 0) {
      throw new BadRequestException("Bạn đã check-in hôm nay rồi");
    }

    // Validate check-in time (Standard 8:30)
    boolean isValid = validateCheckInTime(checkInTimestamp);

    AttendanceLogModel attendance = AttendanceLogModel.builder()
        .attendanceId(snowflake.next())
        .user(user)
        .workDate(workDate)
        .checkInTime(checkInTimestamp)
        .checkOutTime(0)
        .isCheckInValid(isValid)
        .isCheckOutValid(false)
        .attendanceStatus("CHECKED_IN")
        .source("WEB")
        .build();
    attendance = attendanceRepository.save(attendance);

    log.info("Check-in successful for userId: {}, isValid: {}", command.getUserId(), isValid);
    return attendance;
  }

  /** Process check-out */
  public AttendanceLogModel checkOut(CheckOutCommand command) {
    log.info("Processing check-out for userId: {}", command.getUserId());

    long checkOutTime = command.getCheckOutTime();
    if (checkOutTime <= 0) {
      checkOutTime = System.currentTimeMillis();
    }

    LocalDate workDate = convertToLocalDate(checkOutTime);

    // Find today's attendance record
    AttendanceLogModel attendance = attendanceRepository
        .findByUserIdAndDate(command.getUserId(), workDate)
        .orElseThrow(() -> new BadRequestException("Bạn chưa check-in hôm nay"));

    if (attendance.getCheckInTime() == 0) {
      throw new BadRequestException("Bạn cần check-in trước khi check-out");
    }

    if (attendance.getCheckOutTime() > 0) {
      throw new BadRequestException("Bạn đã check-out hôm nay rồi");
    }

    // Validate check-out time
    boolean isValid = validateCheckOutTime(checkOutTime);

    attendance.setCheckOutTime(checkOutTime);
    attendance.setCheckOutValid(isValid);
    attendance.setAttendanceStatus("COMPLETED");

    attendance = attendanceRepository.update(attendance);

    log.info("Check-out successful for userId: {}, isValid: {}", command.getUserId(), isValid);
    return attendance;
  }

  // ==================== Helper Methods ====================

  private boolean validateCheckInTime(long checkInTimeMillis) {
    LocalTime checkIn = convertToLocalTime(checkInTimeMillis);
    LocalTime standardTime = LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE);
    return !checkIn.isAfter(standardTime);
  }

  private boolean validateCheckOutTime(long checkOutTimeMillis) {
    LocalTime checkOut = convertToLocalTime(checkOutTimeMillis);
    LocalTime standardTime = LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE);
    return !checkOut.isBefore(standardTime);
  }

  public static LocalTime convertToLocalTime(long millis) {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalTime();
  }

  private LocalDate convertToLocalDate(long millis) {
    return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  // --- Static Helper Methods for Message Generation (used by Mapper) ---

  public static String generateCheckInMessage(long checkInTimeMillis) {
    if (checkInTimeMillis <= 0) {
      return null;
    }
    LocalTime checkInTime = convertToLocalTime(checkInTimeMillis);
    String timeStr = checkInTime.format(TIME_FORMATTER);
    LocalTime standardTime = LocalTime.of(STANDARD_CHECK_IN_HOUR, STANDARD_CHECK_IN_MINUTE);
    boolean isValid = !checkInTime.isAfter(standardTime);

    if (isValid) {
      return String.format("Check in thành công (%s) đúng giờ", timeStr);
    } else {
      return String.format("Check in thành công (%s) trễ hơn 8:45", timeStr);
    }
  }

  public static String generateCheckOutMessage(long checkOutTimeMillis) {
    if (checkOutTimeMillis <= 0) {
      return null;
    }
    LocalTime checkOutTime = convertToLocalTime(checkOutTimeMillis);
    String timeStr = checkOutTime.format(TIME_FORMATTER);
    LocalTime standardTime = LocalTime.of(STANDARD_CHECK_OUT_HOUR, STANDARD_CHECK_OUT_MINUTE);
    boolean isValid = !checkOutTime.isBefore(standardTime);

    if (isValid) {
      return String.format("Check out thành công (%s) đúng giờ", timeStr);
    } else {
      return String.format("Check out thành công (%s) sớm hơn 17:15", timeStr);
    }
  }
}
