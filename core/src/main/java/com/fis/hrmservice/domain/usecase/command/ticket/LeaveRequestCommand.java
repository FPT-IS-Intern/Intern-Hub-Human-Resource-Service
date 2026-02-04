package com.fis.hrmservice.domain.usecase.command.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestCommand {
  int totalDays;
}
