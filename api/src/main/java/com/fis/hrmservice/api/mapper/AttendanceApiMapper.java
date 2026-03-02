package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.implement.attendance.AttendanceUseCaseImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = AttendanceUseCaseImpl.class)
public interface AttendanceApiMapper {

  AttendanceStatusResponse toStatusResponse(AttendanceStatusModel status);

  CheckInCommand toCheckInCommand(Long userId, long checkInTime, String clientIp, Double latitude, Double longitude);

  @Mapping(target = "message", expression = "java(AttendanceUseCaseImpl.generateCheckInMessage(attendance.getCheckInTime()))")
  AttendanceResponse toCheckInResponseFromLog(AttendanceLogModel attendance);

  CheckOutCommand toCheckOutCommand(Long userId, long checkOutTime);

  @Mapping(target = "message", expression = "java(AttendanceUseCaseImpl.generateCheckOutMessage(attendance.getCheckOutTime()))")
  AttendanceResponse toCheckOutResponseFromLog(AttendanceLogModel attendance);
}
