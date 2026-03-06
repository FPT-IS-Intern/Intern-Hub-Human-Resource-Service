package com.fis.hrmservice.infra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AttendanceInWeekResponse {
    String dayOfWeek;
    String status;
}
