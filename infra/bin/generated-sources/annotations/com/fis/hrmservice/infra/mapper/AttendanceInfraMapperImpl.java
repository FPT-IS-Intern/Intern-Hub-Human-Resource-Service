package com.fis.hrmservice.infra.mapper;

import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.infra.model.AttendanceInWeekResponse;
import com.fis.hrmservice.infra.persistence.entity.AttendanceLog;
import com.fis.hrmservice.infra.persistence.entity.Position;
import com.fis.hrmservice.infra.persistence.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-25T11:23:30+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class AttendanceInfraMapperImpl implements AttendanceInfraMapper {

    @Override
    public AttendanceInWeekCommand toAttendanceInWeekCommand(AttendanceInWeekResponse response) {
        if ( response == null ) {
            return null;
        }

        AttendanceInWeekCommand.AttendanceInWeekCommandBuilder attendanceInWeekCommand = AttendanceInWeekCommand.builder();

        attendanceInWeekCommand.dayOfWeek( response.getDayOfWeek() );
        attendanceInWeekCommand.status( response.getStatus() );

        return attendanceInWeekCommand.build();
    }

    @Override
    public AttendanceLogModel toModel(AttendanceLog attendanceLog) {
        if ( attendanceLog == null ) {
            return null;
        }

        AttendanceLogModel.AttendanceLogModelBuilder attendanceLogModel = AttendanceLogModel.builder();

        if ( attendanceLog.getId() != null ) {
            attendanceLogModel.attendanceId( attendanceLog.getId() );
        }
        attendanceLogModel.checkInTime( toEpoch( attendanceLog.getCheckInTime() ) );
        attendanceLogModel.checkOutTime( toEpoch( attendanceLog.getCheckOutTime() ) );
        attendanceLogModel.user( userToUserModel( attendanceLog.getUser() ) );
        attendanceLogModel.workDate( attendanceLog.getWorkDate() );
        attendanceLogModel.attendanceStatus( attendanceLog.getAttendanceStatus() );
        attendanceLogModel.source( attendanceLog.getSource() );
        attendanceLogModel.checkInBranchId( attendanceLog.getCheckInBranchId() );
        attendanceLogModel.checkOutBranchId( attendanceLog.getCheckOutBranchId() );

        return attendanceLogModel.build();
    }

    @Override
    public UserModel userToUserModel(User user) {
        if ( user == null ) {
            return null;
        }

        UserModel.UserModelBuilder userModel = UserModel.builder();

        userModel.fullName( user.getFullName() );
        userModel.companyEmail( user.getCompanyEmail() );
        userModel.phoneNumber( user.getPhoneNumber() );
        userModel.idNumber( user.getIdNumber() );
        userModel.dateOfBirth( user.getDateOfBirth() );
        userModel.address( user.getAddress() );
        userModel.sysStatus( user.getSysStatus() );
        userModel.internshipStartDate( user.getInternshipStartDate() );
        userModel.internshipEndDate( user.getInternshipEndDate() );
        userModel.position( positionToPositionModel( user.getPosition() ) );
        userModel.department( map( user.getDepartment() ) );
        userModel.isFaceRegistry( user.getIsFaceRegistry() );
        userModel.avatarUrl( user.getAvatarUrl() );
        userModel.cvUrl( user.getCvUrl() );

        return userModel.build();
    }

    protected PositionModel positionToPositionModel(Position position) {
        if ( position == null ) {
            return null;
        }

        PositionModel.PositionModelBuilder positionModel = PositionModel.builder();

        positionModel.name( position.getName() );
        positionModel.description( position.getDescription() );

        return positionModel.build();
    }
}
