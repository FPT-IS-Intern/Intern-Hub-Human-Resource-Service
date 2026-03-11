package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class AttendanceFilterResponse {
    Integer no;
    String fullName;
    String companyEmail;
    LocalDate attendanceDate;
    LocalTime checkInTime;
    LocalTime checkOutTime;
    String workingMethod;
    String status;
    String workLocation;
}
