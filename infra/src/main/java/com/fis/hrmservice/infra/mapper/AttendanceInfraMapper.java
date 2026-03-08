package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.CvModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
import com.fis.hrmservice.infra.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
@Mapper(componentModel = "spring")
public interface AttendanceInfraMapper {

    AttendanceInWeekCommand toAttendanceInWeekCommand(AttendanceInWeekResponse response);

    @Mapping(target = "attendanceId", source = "id")
    @Mapping(target = "checkInTime", source = "checkInTime", qualifiedByName = "toEpoch")
    @Mapping(target = "checkOutTime", source = "checkOutTime", qualifiedByName = "toEpoch")
    AttendanceLogModel toModel(AttendanceLog attendanceLog);

    // FIX mentor recursion
    @Mapping(target = "mentor", ignore = true)
    UserModel userToUserModel(User user);

    // FIX Avatar recursion
    @Mapping(target = "user", ignore = true)
    AvatarModel avatarToAvatarModel(Avatar avatar);

    // FIX Cv recursion
    @Mapping(target = "user", ignore = true)
    CvModel cvToCvModel(Cv cv);

    @Named("toEpoch")
    default long toEpoch(LocalDateTime time) {
        if (time == null) return 0L;
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    default String map(Department department) {
        if (department == null) return null;
        return department.getName();
    }
}