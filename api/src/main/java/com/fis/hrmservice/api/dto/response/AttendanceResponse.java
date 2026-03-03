package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  private Long attendanceId;
  private String attendanceStatus;
  private long checkInTime;
  private long checkOutTime;
  private boolean isCheckInValid;
  private boolean isCheckOutValid;
  private String message;
}
