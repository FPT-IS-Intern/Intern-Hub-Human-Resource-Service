package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.AttendanceFilterRequest;
import com.fis.hrmservice.api.dto.response.AttendanceFilterResponse;
import com.fis.hrmservice.api.dto.response.AttendanceInWeekApiResponse;
import com.fis.hrmservice.api.dto.response.AttendanceResponse;
import com.fis.hrmservice.api.dto.response.AttendanceStatusResponse;
import com.fis.hrmservice.domain.model.attendance.AttendanceLogModel;
import com.fis.hrmservice.domain.model.attendance.AttendanceStatusModel;
import com.fis.hrmservice.domain.model.constant.AttendanceStatus;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.attendance.AttendanceInWeekCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckInCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.CheckOutCommand;
import com.fis.hrmservice.domain.usecase.command.attendance.FilterAttendanceCommand;
import com.fis.hrmservice.domain.usecase.implement.attendance.AttendanceUseCaseImpl;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-22T17:23:39+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AttendanceApiMapperImpl implements AttendanceApiMapper {

    @Override
    public AttendanceStatusResponse toStatusResponse(AttendanceStatusModel status) {
        if ( status == null ) {
            return null;
        }

        AttendanceStatusResponse.AttendanceStatusResponseBuilder attendanceStatusResponse = AttendanceStatusResponse.builder();

        attendanceStatusResponse.canCheckIn( status.isCanCheckIn() );
        attendanceStatusResponse.canCheckOut( status.isCanCheckOut() );
        attendanceStatusResponse.canResetByBranchChange( status.isCanResetByBranchChange() );
        if ( status.getCheckInMessage() != null ) {
            attendanceStatusResponse.checkInMessage( status.getCheckInMessage() );
        }
        if ( status.getCheckInTime() != null ) {
            attendanceStatusResponse.checkInTime( status.getCheckInTime() );
        }
        if ( status.getCheckOutMessage() != null ) {
            attendanceStatusResponse.checkOutMessage( status.getCheckOutMessage() );
        }
        if ( status.getCheckOutTime() != null ) {
            attendanceStatusResponse.checkOutTime( status.getCheckOutTime() );
        }
        if ( status.getCurrentBranchId() != null ) {
            attendanceStatusResponse.currentBranchId( status.getCurrentBranchId() );
        }
        if ( status.getOpenSessionBranchId() != null ) {
            attendanceStatusResponse.openSessionBranchId( status.getOpenSessionBranchId() );
        }
        attendanceStatusResponse.sessionOpen( status.isSessionOpen() );
        if ( status.getStatusMessage() != null ) {
            attendanceStatusResponse.statusMessage( status.getStatusMessage() );
        }
        if ( status.getWorkDate() != null ) {
            attendanceStatusResponse.workDate( status.getWorkDate() );
        }

        return attendanceStatusResponse.build();
    }

    @Override
    public FilterAttendanceCommand toCommand(AttendanceFilterRequest request) {
        if ( request == null ) {
            return null;
        }

        FilterAttendanceCommand.FilterAttendanceCommandBuilder filterAttendanceCommand = FilterAttendanceCommand.builder();

        if ( request.getNameOrEmail() != null ) {
            filterAttendanceCommand.nameOrEmail( request.getNameOrEmail() );
        }
        if ( request.getStartDate() != null ) {
            filterAttendanceCommand.startDate( request.getStartDate() );
        }
        if ( request.getEndDate() != null ) {
            filterAttendanceCommand.endDate( request.getEndDate() );
        }
        if ( request.getAttendanceStatus() != null ) {
            filterAttendanceCommand.attendanceStatus( Enum.valueOf( AttendanceStatus.class, request.getAttendanceStatus() ) );
        }

        return filterAttendanceCommand.build();
    }

    @Override
    public CheckInCommand toCheckInCommand(Long userId, long checkInTime, String clientIp, Double latitude, Double longitude) {
        if ( userId == null && clientIp == null && latitude == null && longitude == null ) {
            return null;
        }

        CheckInCommand.CheckInCommandBuilder checkInCommand = CheckInCommand.builder();

        if ( userId != null ) {
            checkInCommand.userId( userId );
        }
        checkInCommand.checkInTime( checkInTime );
        if ( clientIp != null ) {
            checkInCommand.clientIp( clientIp );
        }
        if ( latitude != null ) {
            checkInCommand.latitude( latitude );
        }
        if ( longitude != null ) {
            checkInCommand.longitude( longitude );
        }

        return checkInCommand.build();
    }

    @Override
    public AttendanceResponse toCheckInResponseFromLog(AttendanceLogModel attendance) {
        if ( attendance == null ) {
            return null;
        }

        AttendanceResponse.AttendanceResponseBuilder attendanceResponse = AttendanceResponse.builder();

        attendanceResponse.attendanceId( attendance.getAttendanceId() );
        if ( attendance.getAttendanceStatus() != null ) {
            attendanceResponse.attendanceStatus( attendance.getAttendanceStatus().name() );
        }
        if ( attendance.getCheckInBranchId() != null ) {
            attendanceResponse.checkInBranchId( attendance.getCheckInBranchId() );
        }
        attendanceResponse.checkInTime( attendance.getCheckInTime() );
        if ( attendance.getCheckOutBranchId() != null ) {
            attendanceResponse.checkOutBranchId( attendance.getCheckOutBranchId() );
        }
        attendanceResponse.checkOutTime( attendance.getCheckOutTime() );

        attendanceResponse.message( AttendanceUseCaseImpl.generateCheckInMessage(attendance.getCheckInTime()) );

        return attendanceResponse.build();
    }

    @Override
    public CheckOutCommand toCheckOutCommand(Long userId, long checkOutTime, String clientIp, Double latitude, Double longitude) {
        if ( userId == null && clientIp == null && latitude == null && longitude == null ) {
            return null;
        }

        CheckOutCommand.CheckOutCommandBuilder checkOutCommand = CheckOutCommand.builder();

        if ( userId != null ) {
            checkOutCommand.userId( userId );
        }
        checkOutCommand.checkOutTime( checkOutTime );
        if ( clientIp != null ) {
            checkOutCommand.clientIp( clientIp );
        }
        if ( latitude != null ) {
            checkOutCommand.latitude( latitude );
        }
        if ( longitude != null ) {
            checkOutCommand.longitude( longitude );
        }

        return checkOutCommand.build();
    }

    @Override
    public AttendanceResponse toCheckOutResponseFromLog(AttendanceLogModel attendance) {
        if ( attendance == null ) {
            return null;
        }

        AttendanceResponse.AttendanceResponseBuilder attendanceResponse = AttendanceResponse.builder();

        attendanceResponse.attendanceId( attendance.getAttendanceId() );
        if ( attendance.getAttendanceStatus() != null ) {
            attendanceResponse.attendanceStatus( attendance.getAttendanceStatus().name() );
        }
        if ( attendance.getCheckInBranchId() != null ) {
            attendanceResponse.checkInBranchId( attendance.getCheckInBranchId() );
        }
        attendanceResponse.checkInTime( attendance.getCheckInTime() );
        if ( attendance.getCheckOutBranchId() != null ) {
            attendanceResponse.checkOutBranchId( attendance.getCheckOutBranchId() );
        }
        attendanceResponse.checkOutTime( attendance.getCheckOutTime() );

        attendanceResponse.message( AttendanceUseCaseImpl.generateCheckOutMessage(attendance.getCheckOutTime()) );

        return attendanceResponse.build();
    }

    @Override
    public AttendanceFilterResponse toFilterResponseItem(AttendanceLogModel model) {
        if ( model == null ) {
            return null;
        }

        AttendanceFilterResponse.AttendanceFilterResponseBuilder attendanceFilterResponse = AttendanceFilterResponse.builder();

        String fullName = modelUserFullName( model );
        if ( fullName != null ) {
            attendanceFilterResponse.fullName( fullName );
        }
        String companyEmail = modelUserCompanyEmail( model );
        if ( companyEmail != null ) {
            attendanceFilterResponse.companyEmail( companyEmail );
        }
        if ( model.getWorkDate() != null ) {
            attendanceFilterResponse.attendanceDate( model.getWorkDate() );
        }
        attendanceFilterResponse.checkInTime( toLocalTime( model.getCheckInTime() ) );
        attendanceFilterResponse.checkOutTime( toLocalTime( model.getCheckOutTime() ) );
        if ( model.getSource() != null ) {
            attendanceFilterResponse.workingMethod( model.getSource() );
        }
        String value = modelAttendanceStatusValue( model );
        if ( value != null ) {
            attendanceFilterResponse.status( value );
        }

        return attendanceFilterResponse.build();
    }

    @Override
    public List<AttendanceFilterResponse> toFilterResponseList(List<AttendanceLogModel> models) {
        if ( models == null ) {
            return null;
        }

        List<AttendanceFilterResponse> list = new ArrayList<AttendanceFilterResponse>( models.size() );
        for ( AttendanceLogModel attendanceLogModel : models ) {
            list.add( toFilterResponseItem( attendanceLogModel ) );
        }

        return list;
    }

    @Override
    public AttendanceInWeekApiResponse toApiResponse(AttendanceInWeekCommand command) {
        if ( command == null ) {
            return null;
        }

        AttendanceInWeekApiResponse.AttendanceInWeekApiResponseBuilder attendanceInWeekApiResponse = AttendanceInWeekApiResponse.builder();

        if ( command.getDayOfWeek() != null ) {
            attendanceInWeekApiResponse.dayOfWeek( command.getDayOfWeek() );
        }
        if ( command.getStatus() != null ) {
            attendanceInWeekApiResponse.status( command.getStatus() );
        }

        return attendanceInWeekApiResponse.build();
    }

    private String modelUserFullName(AttendanceLogModel attendanceLogModel) {
        UserModel user = attendanceLogModel.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getFullName();
    }

    private String modelUserCompanyEmail(AttendanceLogModel attendanceLogModel) {
        UserModel user = attendanceLogModel.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getCompanyEmail();
    }

    private String modelAttendanceStatusValue(AttendanceLogModel attendanceLogModel) {
        AttendanceStatus attendanceStatus = attendanceLogModel.getAttendanceStatus();
        if ( attendanceStatus == null ) {
            return null;
        }
        return attendanceStatus.getValue();
    }
}
