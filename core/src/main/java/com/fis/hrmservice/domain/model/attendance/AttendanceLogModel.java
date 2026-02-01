package com.fis.hrmservice.domain.model.attendance;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.user.UserModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceLogModel extends BaseDomain {

    Long attendanceId;
    UserModel user;
    LocalDate workDate;
    LocalDateTime checkInTime;
    LocalDateTime checkOutTime;
    String attendanceStatus;
    String source;
}