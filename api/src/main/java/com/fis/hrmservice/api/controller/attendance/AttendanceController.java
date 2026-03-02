package com.fis.hrmservice.api.controller.attendance;

import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.api.dto.response.WiFiInfoResponse;
import com.fis.hrmservice.api.mapper.AttendanceApiMapper;
import com.fis.hrmservice.api.util.WebUtils;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.port.output.network.NetworkCheckPort;
import com.fis.hrmservice.domain.usecase.attendance.AttendanceUseCase;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm/attendance")
@CrossOrigin(origins = "*")
@EnableGlobalExceptionHandler
@Slf4j
@Tag(name = "Attendance Management", description = "APIs for attendance check-in and check-out")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AttendanceController {

  AttendanceUseCase attendanceUseCase;
  AttendanceApiMapper attendanceApiMapper;
  NetworkCheckPort networkCheckPort;

  /** Get current attendance status for a user */
  @GetMapping("/status")
  public ResponseApi<AttendanceStatusResponse> getAttendanceStatus(@RequestParam Long userId) {
    log.info("GET /attendance/status - userId: {}", userId);

    LocalDate today = LocalDate.now(CoreConstant.VIETNAM_ZONE);
    AttendanceStatusModel status = attendanceUseCase.getAttendanceStatus(userId, today);
    AttendanceStatusResponse response = attendanceApiMapper.toStatusResponse(status);

    return ResponseApi.ok(response);
  }

  /** Process check-in */
  @PostMapping("/check-in")
  public ResponseApi<AttendanceResponse> checkIn(
      @RequestParam Long userId,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      HttpServletRequest servletRequest) {
    log.info("POST /attendance/check-in - userId: {}", userId);

    String clientIp = WebUtils.getClientIpAddress(servletRequest);
    long now = System.currentTimeMillis();
    CheckInCommand command =
        attendanceApiMapper.toCheckInCommand(userId, now, clientIp, latitude, longitude);
    AttendanceLogModel attendance = attendanceUseCase.checkIn(command);
    AttendanceResponse response = attendanceApiMapper.toCheckInResponseFromLog(attendance);

    return ResponseApi.ok(response);
  }

  /** Process check-out */
  @PostMapping("/check-out")
  public ResponseApi<AttendanceResponse> checkOut(@RequestParam Long userId) {
    log.info("POST /attendance/check-out - userId: {}", userId);

    long now = System.currentTimeMillis();
    CheckOutCommand command = attendanceApiMapper.toCheckOutCommand(userId, now);
    AttendanceLogModel attendance = attendanceUseCase.checkOut(command);
    AttendanceResponse response = attendanceApiMapper.toCheckOutResponseFromLog(attendance);

    return ResponseApi.ok(response);
  }

  /** Unified check-point for attendance eligibility (IP or GPS) */
  @GetMapping("/check-point")
  public ResponseApi<WiFiInfoResponse> checkPoint(
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      HttpServletRequest request) {
    log.info("GET /attendance/check-point - checking eligibility");

    String clientIp = WebUtils.getClientIpAddress(request);
    boolean isCompanyNetwork = networkCheckPort.isCompanyIpAddress(clientIp);
    boolean isAtLocation = networkCheckPort.isAtCompanyLocation(latitude, longitude);

    boolean isValid = isCompanyNetwork || isAtLocation;

    WiFiInfoResponse response =
        WiFiInfoResponse.builder()
            .wifiName(isCompanyNetwork ? "FPT-Network" : (isAtLocation ? "Office-GPS" : "External"))
            .isCompanyWifi(isValid)
            .build();

    log.info(
        "Check-point result - IP: {}, GPS: {}, isValid: {}", clientIp, (latitude != null), isValid);
    return ResponseApi.ok(response);
  }
}
