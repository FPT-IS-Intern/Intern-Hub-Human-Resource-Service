package com.fis.hrmservice.api.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterAttendanceRequest {
    String nameOrEmail;
    LocalDate startDate;
    LocalDate endDate;
    String attendanceStatus;
}
