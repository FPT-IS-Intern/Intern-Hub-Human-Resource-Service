package com.fis.hrmservice.domain.usecase.command.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AttendanceInWeekCommand {
    String dayOfWeek;
    String status;
}
