package com.fis.hrmservice.api.controller.attendance;

import com.fis.hrmservice.api.dto.request.AttendanceRequest;
import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.api.dto.response.WiFiInfoResponse;
import com.fis.hrmservice.api.mapper.AttendanceApiMapper;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.implement.attendance.AttendanceUseCaseImpl;
import com.fis.hrmservice.infra.service.NetworkCheckService;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm-service/attendance")
@EnableGlobalExceptionHandler
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4207"})
@Tag(name = "Attendance Management", description = "APIs for attendance check-in and check-out")
public class AttendanceController {

  @Autowired private AttendanceUseCaseImpl attendanceUseCase;
  @Autowired private AttendanceApiMapper attendanceApiMapper;
  @Autowired private NetworkCheckService networkCheckService;

  /** Get current attendance status for a user */
  @GetMapping("/status")
//  test tạm ko dùng cái này  -> @RequestParam Long userId
  public ResponseApi<AttendanceStatusResponse> getAttendanceStatus(@RequestParam Long userId) {
    log.info("GET /attendance/status - userId: {}", userId);

    LocalDate today = LocalDate.now();
    AttendanceStatusModel status = attendanceUseCase.getAttendanceStatus(userId, today);
    AttendanceStatusResponse response = attendanceApiMapper.toStatusResponse(status);

    return ResponseApi.ok(response);
  }

  /** Process check-in */
  @PostMapping("/check-in")
  public ResponseApi<AttendanceResponse> checkIn(@RequestBody AttendanceRequest request) {
    log.info("POST /attendance/check-in - userId: {}", request.getUserId());

    CheckInCommand command = attendanceApiMapper.toCheckInCommand(request);
    AttendanceLogModel attendance = attendanceUseCase.checkIn(command);
    AttendanceResponse response = attendanceApiMapper.toCheckInResponseFromLog(attendance);

    return ResponseApi.ok(response);
  }

  /** Process check-out */
  @PostMapping("/check-out")
  public ResponseApi<AttendanceResponse> checkOut(@RequestBody AttendanceRequest request) {
    log.info("POST /attendance/check-out - userId: {}", request.getUserId());

    CheckOutCommand command = attendanceApiMapper.toCheckOutCommand(request);
    AttendanceLogModel attendance = attendanceUseCase.checkOut(command);
    AttendanceResponse response = attendanceApiMapper.toCheckOutResponseFromLog(attendance);

    return ResponseApi.ok(response);
  }

  /**
   * Check if user is on company network based on IP address Validates against company IP ranges
   * from bo-portal
   */
  @GetMapping("/network-check")
  public ResponseApi<WiFiInfoResponse> checkNetwork(HttpServletRequest request) {
    log.info("GET /attendance/network-check - checking client IP");

    String clientIp = getClientIpAddress(request);
    boolean isCompanyNetwork = networkCheckService.isCompanyIpAddress(clientIp);

    WiFiInfoResponse response =
        WiFiInfoResponse.builder()
            .wifiName(isCompanyNetwork ? "FPT-Network" : "External-Network")
            .isCompanyWifi(isCompanyNetwork)
            .build();

    log.info("Network check result - IP: {}, isCompanyNetwork: {}", clientIp, isCompanyNetwork);
    return ResponseApi.ok(response);
  }

  // ==================== Helper Methods ====================

  /**
   * Extract client IP address from request headers. Checks multiple headers in order of priority to
   * handle various proxy configurations.
   */
  private String getClientIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    // Handle multiple IPs in X-Forwarded-For
    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0].trim();
    }

    return ip;
  }
}
