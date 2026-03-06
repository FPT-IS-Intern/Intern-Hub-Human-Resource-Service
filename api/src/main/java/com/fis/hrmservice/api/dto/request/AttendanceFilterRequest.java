package com.fis.hrmservice.api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceFilterRequest {
    String nameOrEmail;
    String attendanceStatus;
}

