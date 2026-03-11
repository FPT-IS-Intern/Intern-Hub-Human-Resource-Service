package com.fis.hrmservice.api.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterAttendanceRequest {
    @JsonAlias({"nameOrKeyword", "nameOrEmail"})
    String nameOrEmail;
    LocalDate startDate;
    LocalDate endDate;
    @JsonAlias({"status", "attendanceStatus"})
    String attendanceStatus;
}
