package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttendanceInfraMapper {

    AttendanceInWeekCommand toAttendanceInWeekCommand (AttendanceInWeekResponse response);
}
