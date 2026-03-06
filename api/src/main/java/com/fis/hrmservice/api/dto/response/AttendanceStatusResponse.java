package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
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
  private boolean canCheckIn;
  private boolean canCheckOut;
  private boolean sessionOpen;
  private UUID openSessionBranchId;
  private String statusMessage;
}
