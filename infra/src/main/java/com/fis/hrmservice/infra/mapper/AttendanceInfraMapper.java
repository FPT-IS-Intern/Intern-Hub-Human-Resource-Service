package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
import com.fis.hrmservice.infra.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceInfraMapper {

    AttendanceInWeekCommand toAttendanceInWeekCommand(AttendanceInWeekResponse response);

    @Mapping(target = "attendanceId", source = "id")
    @Mapping(target = "isCheckInValid", ignore = true)
    @Mapping(target = "isCheckOutValid", ignore = true)
    AttendanceLogModel toModel(AttendanceLog attendanceLog);

    // FIX mentor recursion
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "departmentId", ignore = true)
    @Mapping(target = "departmentCode", ignore = true)
    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authIdentityStatus", ignore = true)
    @Mapping(target = "children", ignore = true)
    UserModel userToUserModel(User user);

    default String map(Department department) {
        if (department == null) return null;
        return department.getName();
    }
}
