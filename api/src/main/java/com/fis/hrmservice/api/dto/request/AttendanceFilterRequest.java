package com.fis.hrmservice.api.dto.request;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceFilterRequest {
    String nameOrEmail;
    LocalDate startDate;
    LocalDate endDate;
    String attendanceStatus;
}

