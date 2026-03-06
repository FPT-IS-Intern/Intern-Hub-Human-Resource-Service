package com.fis.hrmservice.domain.model.attendance;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceStatusModel {
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
  private UUID currentBranchId;
  private boolean canResetByBranchChange;
  private String statusMessage;
}
