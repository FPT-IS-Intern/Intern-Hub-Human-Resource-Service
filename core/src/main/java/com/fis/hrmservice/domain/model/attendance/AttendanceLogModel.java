package com.fis.hrmservice.domain.model.attendance;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.user.UserModel;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceLogModel extends BaseDomain {

  long attendanceId;
  UserModel user;
  LocalDate workDate;
  long checkInTime;
  long checkOutTime;
  String attendanceStatus;
  String source;
}
