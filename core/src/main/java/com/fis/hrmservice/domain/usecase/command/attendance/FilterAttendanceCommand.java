package com.fis.hrmservice.domain.usecase.command.attendance;

import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
@Setter
public class FilterAttendanceCommand {
    String nameOrEmail;
    LocalDate startDate;
    LocalDate endDate;
    AttendanceStatus attendanceStatus;
}
