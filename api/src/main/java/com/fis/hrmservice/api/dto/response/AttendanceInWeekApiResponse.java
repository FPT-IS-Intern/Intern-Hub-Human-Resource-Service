package com.fis.hrmservice.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class AttendanceInWeekApiResponse {
    String dayOfWeek;
    String status;
}
