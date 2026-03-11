package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.AttendanceFilterRequest;
import com.fis.hrmservice.api.dto.response.AttendanceFilterResponse;
import com.fis.hrmservice.api.dto.response.AttendanceInWeekApiResponse;
import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.fis.hrmservice.domain.usecase.implement.attendance.AttendanceUseCaseImpl;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    imports = AttendanceUseCaseImpl.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AttendanceApiMapper {

  AttendanceStatusResponse toStatusResponse(AttendanceStatusModel status);

  FilterAttendanceCommand toCommand(AttendanceFilterRequest request);

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
  @Mapping(target = "attendanceDate", source = "workDate")
  @Mapping(target = "checkInTime", source = "checkInTime", qualifiedByName = "toLocalTime")
  @Mapping(target = "checkOutTime", source = "checkOutTime", qualifiedByName = "toLocalTime")
  @Mapping(target = "workingMethod", source = "source")
  @Mapping(target = "status", source = "attendanceStatus.value")
  @Mapping(target = "workLocation", ignore = true)
  @Mapping(target = "no", ignore = true)
  AttendanceFilterResponse toFilterResponseItem(AttendanceLogModel model);


  @Named("toLocalTime")
  default LocalTime toLocalTime(long epochMillis) {
    if (epochMillis == 0) {
      return null;
    }
    return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalTime();
  }

  List<AttendanceFilterResponse> toFilterResponseList(List<AttendanceLogModel> models);

  default LocalTime convertToLocalTime(long timestamp) {
    if (timestamp <= 0) return null;
    return Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
            .toLocalTime();
  }

  AttendanceInWeekApiResponse toApiResponse(AttendanceInWeekCommand command);

}
