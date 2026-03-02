package com.fis.hrmservice.api.dto.request;

import lombok.Data;

@Data
public class AttendanceRequest {
  private Long userId;
  private Double latitude;
  private Double longitude;
}
