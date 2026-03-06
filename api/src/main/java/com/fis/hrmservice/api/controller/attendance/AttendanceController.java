package com.fis.hrmservice.api.controller.attendance;

import com.fis.hrmservice.api.dto.request.AttendanceFilterRequest;
import com.fis.hrmservice.api.dto.response.*;
import com.fis.hrmservice.api.mapper.AttendanceApiMapper;
import com.fis.hrmservice.api.util.UserContext;
import com.fis.hrmservice.api.util.WebUtils;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.model.constant.CoreConstant;
import com.fis.hrmservice.domain.port.output.network.NetworkCheckPort;
import com.fis.hrmservice.domain.usecase.attendance.AttendanceUseCase;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.intern.hub.library.common.annotation.EnableGlobalExceptionHandler;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.dto.ResponseApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hrm/attendance")
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
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      HttpServletRequest servletRequest) {
    Long userId = UserContext.requiredUserId(); // dùng cái này đi cha
    log.info("POST /attendance/check-in - userId: {}", userId);

    String clientIp = WebUtils.getClientIpAddress(servletRequest);
    CheckInCommand command = attendanceApiMapper.toCheckInCommand(userId, 0L, clientIp, latitude, longitude);
    AttendanceLogModel attendanceLogModel = attendanceUseCase.checkIn(command);
    AttendanceResponse response = attendanceApiMapper.toCheckInResponseFromLog(attendanceLogModel);

    return ResponseApi.ok(response);
  }

  /** Process check-out */
  @PostMapping("/check-out")
  public ResponseApi<AttendanceResponse> checkOut(
      @RequestParam Long userId,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      HttpServletRequest servletRequest) {
    log.info("POST /attendance/check-out - userId: {}", userId);

    String clientIp = WebUtils.getClientIpAddress(servletRequest);
    CheckOutCommand command = attendanceApiMapper.toCheckOutCommand(userId, 0L, clientIp, latitude, longitude);
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
    UUID branchIdFromIp = networkCheckPort.resolveCompanyIpBranchId(clientIp).orElse(null);
    UUID branchIdFromLocation = networkCheckPort.resolveCompanyLocationBranchId(latitude, longitude).orElse(null);
    boolean isCompanyNetwork = branchIdFromIp != null;
    boolean isAtLocation = branchIdFromLocation != null;
    UUID resolvedBranchId = branchIdFromIp != null ? branchIdFromIp : branchIdFromLocation;

    boolean isValid = isCompanyNetwork || isAtLocation;

    WiFiInfoResponse response =
        WiFiInfoResponse.builder()
            .wifiName(isCompanyNetwork ? "FPT-Network" : (isAtLocation ? "Office-GPS" : "External"))
            .isCompanyWifi(isValid)
            .branchId(resolvedBranchId)
            .build();

    log.info(
        "Check-point result - IP: {}, GPS: {}, isValid: {}", clientIp, (latitude != null), isValid);
    return ResponseApi.ok(response);
  }

  @PostMapping("/filter")
  public ResponseApi<PaginatedData<AttendanceFilterResponse>> filterAttendanceLogs(
      @RequestBody AttendanceFilterRequest request,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    FilterAttendanceCommand command = attendanceApiMapper.toCommand(request);

    PaginatedData<AttendanceLogModel> logs =
        attendanceUseCase.filterAttendance(command.getNameOrEmail(), command.getAttendanceStatus(), page, size);

    AtomicInteger counter = new AtomicInteger(page * size + 1);
    List<AttendanceFilterResponse> items = attendanceApiMapper
        .toFilterResponseList((List<AttendanceLogModel>) logs.getItems())
        .stream()
        .peek(item -> item.setNo(counter.getAndIncrement()))
        .toList();

    return ResponseApi.ok(
        PaginatedData.<AttendanceFilterResponse>builder()
            .items(items)
            .totalItems(logs.getTotalItems())
            .totalPages(logs.getTotalPages())
            .build());
  }

  @GetMapping("/attendance-in-week")
  public ResponseApi<List<AttendanceInWeekApiResponse>> getAttendanceInWeekByUserId() {
    Long userId = UserContext.requiredUserId();
    return ResponseApi.ok(attendanceUseCase.getAttendanceInWeekByUserId(userId).stream().map(attendanceApiMapper::toApiResponse).toList());
  }
}
