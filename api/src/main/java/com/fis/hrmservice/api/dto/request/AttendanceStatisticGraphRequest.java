package com.fis.hrmservice.api.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceStatisticGraphRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
}
