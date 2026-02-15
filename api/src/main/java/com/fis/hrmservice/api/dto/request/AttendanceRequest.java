package com.fis.hrmservice.api.dto.request;

import lombok.Data;

@Data
public class AttendanceRequest {
  private Long userId;
  private long checkInTime;
  private long checkOutTime;
}
