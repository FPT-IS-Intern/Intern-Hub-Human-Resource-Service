package com.fis.hrmservice.domain.usecase.command.attendance;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckInCommand {
  private Long userId;
  private long checkInTime;
}
