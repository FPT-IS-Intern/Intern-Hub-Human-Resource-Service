package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttendanceStatusResponse {
  private LocalDate workDate;
  private LocalTime checkInTime;
  private LocalTime checkOutTime;
  private boolean isCheckInValid;
  private boolean isCheckOutValid;
  private String checkInMessage;
  private String checkOutMessage;
}
