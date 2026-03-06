package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class AttendanceResponse {
  private Long attendanceId;
  private String attendanceStatus;
  private long checkInTime;
  private long checkOutTime;
  private boolean isCheckInValid;
  private boolean isCheckOutValid;
  private UUID checkInBranchId;
  private UUID checkOutBranchId;
  private String message;
}
