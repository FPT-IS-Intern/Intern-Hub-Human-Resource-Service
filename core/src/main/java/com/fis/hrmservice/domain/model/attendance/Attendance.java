package com.fis.hrmservice.domain.model.attendance;

import com.fis.hrmservice.domain.model.contant.AttendanceStatus;
import com.fis.hrmservice.domain.model.contant.SourceAttendance;

import java.time.LocalDateTime;

public class Attendance {
    Long attendanceId;
    Long userId;
    LocalDateTime workDate;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
    AttendanceStatus status;
    SourceAttendance source;
}