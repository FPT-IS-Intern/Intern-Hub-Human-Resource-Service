package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.response.AttendanceFilterResponse;
import com.fis.hrmservice.api.dto.response.AttendanceInWeekApiResponse;
import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.implement.attendance.AttendanceUseCaseImpl;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = AttendanceUseCaseImpl.class)
public interface AttendanceApiMapper {

  AttendanceStatusResponse toStatusResponse(AttendanceStatusModel status);

  CheckInCommand toCheckInCommand(
      Long userId, long checkInTime, String clientIp, Double latitude, Double longitude);

  @Mapping(
      target = "message",
      expression =
          "java(AttendanceUseCaseImpl.generateCheckInMessage(attendance.getCheckInTime()))")
  AttendanceResponse toCheckInResponseFromLog(AttendanceLogModel attendance);

  CheckOutCommand toCheckOutCommand(
      Long userId, long checkOutTime, String clientIp, Double latitude, Double longitude);

  @Mapping(
          target = "message",
          expression =
                  "java(AttendanceUseCaseImpl.generateCheckOutMessage(attendance.getCheckOutTime()))")
  AttendanceResponse toCheckOutResponseFromLog(AttendanceLogModel attendance);

  @Mapping(target = "fullName", source = "user.fullName")
  @Mapping(target = "companyEmail", source = "user.companyEmail")
  @Mapping(target = "department", source = "user.department")
  @Mapping(target = "attendanceDate", source = "workDate")
  @Mapping(target = "checkInTime", expression = "java(convertToLocalTime(model.getCheckInTime()))")
  @Mapping(target = "checkOutTime", expression = "java(convertToLocalTime(model.getCheckOutTime()))")
  @Mapping(target = "workingMethod", source = "source")
  @Mapping(target = "status", expression = "java(model.getAttendanceStatus().getValue())")
  AttendanceFilterResponse toFilterResponseItem(AttendanceLogModel model);

  List<AttendanceFilterResponse> toFilterResponseItem(List<AttendanceLogModel> model);

  default LocalTime convertToLocalTime(long timestamp) {
    if (timestamp == 0) return null;
    return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalTime();
  }

  AttendanceInWeekApiResponse toApiResponse(AttendanceInWeekCommand command);

}
