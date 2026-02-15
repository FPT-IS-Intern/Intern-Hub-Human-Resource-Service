package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceResponse {
  private Long attendanceId;
  private String attendanceStatus;
  private String message;
}
