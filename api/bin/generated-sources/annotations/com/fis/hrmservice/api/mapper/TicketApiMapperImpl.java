package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.FilterRegistrationRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.FirstThreeRegistrationResponse;
import com.fis.hrmservice.api.dto.response.ListRegistrationResponse;
import com.fis.hrmservice.api.dto.response.RegistrationDetailResponse;
import com.fis.hrmservice.api.dto.response.TicketResponse;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.TicketType;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.domain.model.user.AvatarModel;
import com.fis.hrmservice.domain.model.user.CvModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T15:33:10+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class TicketApiMapperImpl implements TicketApiMapper {

    @Override
    public CreateTicketRequest toTicketRequest(CreateTicketCommand command) {
        if ( command == null ) {
            return null;
        }

        CreateTicketRequest createTicketRequest = new CreateTicketRequest();

        createTicketRequest.setEvidence( command.getEvidence() );
        createTicketRequest.setFromDate( command.getFromDate() );
        createTicketRequest.setReason( command.getReason() );
        createTicketRequest.setTicketType( command.getTicketType() );
        createTicketRequest.setToDate( command.getToDate() );

        return createTicketRequest;
    }

    @Override
    public CreateTicketCommand toTicketCommand(CreateTicketRequest request) {
        if ( request == null ) {
            return null;
        }

        CreateTicketCommand.CreateTicketCommandBuilder createTicketCommand = CreateTicketCommand.builder();

        createTicketCommand.ticketType( request.getTicketType() );
        createTicketCommand.fromDate( request.getFromDate() );
        createTicketCommand.toDate( request.getToDate() );
        createTicketCommand.reason( request.getReason() );
        createTicketCommand.evidence( request.getEvidence() );

        return createTicketCommand.build();
    }

    @Override
    public TicketResponse toTicketResponse(LeaveRequestModel leaveModel) {
        if ( leaveModel == null ) {
            return null;
        }

        TicketResponse ticketResponse = new TicketResponse();

        ticketResponse.setTicketId( leaveModelTicketTicketId( leaveModel ) );
        TicketType typeName = leaveModelTicketTicketTypeTypeName( leaveModel );
        if ( typeName != null ) {
            ticketResponse.setTicketType( typeName.name() );
        }
        ticketResponse.setTicketStatus( map( leaveModelTicketSysStatus( leaveModel ) ) );
        ticketResponse.setCreateDate( leaveModelTicketStartAt( leaveModel ) );

        return ticketResponse;
    }

    @Override
    public TicketResponse toTicketResponse(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        TicketResponse ticketResponse = new TicketResponse();

        ticketResponse.setTicketId( ticketModel.getTicketId() );
        TicketType typeName = ticketModelTicketTypeTypeName( ticketModel );
        if ( typeName != null ) {
            ticketResponse.setTicketType( typeName.name() );
        }
        ticketResponse.setTicketStatus( map( ticketModel.getSysStatus() ) );
        ticketResponse.setCreateDate( ticketModel.getStartAt() );

        return ticketResponse;
    }

    @Override
    public TicketResponse toTicketResponse(RemoteRequestModel requestModel) {
        if ( requestModel == null ) {
            return null;
        }

        TicketResponse ticketResponse = new TicketResponse();

        ticketResponse.setTicketId( requestModelTicketTicketId( requestModel ) );
        ticketResponse.setTicketType( map( requestModel.getRemoteType() ) );
        ticketResponse.setTicketStatus( map( requestModelTicketSysStatus( requestModel ) ) );
        ticketResponse.setCreateDate( requestModelTicketStartAt( requestModel ) );

        return ticketResponse;
    }

    @Override
    public RemoteRequestCommand toRemoteRequestCommand(RemoteTicketRequest leaveRequest) {
        if ( leaveRequest == null ) {
            return null;
        }

        RemoteRequestCommand.RemoteRequestCommandBuilder remoteRequestCommand = RemoteRequestCommand.builder();

        remoteRequestCommand.startTime( leaveRequest.getStartTime() );
        remoteRequestCommand.endTime( leaveRequest.getEndTime() );
        remoteRequestCommand.location( leaveRequest.getLocation() );

        return remoteRequestCommand.build();
    }

    @Override
    public ListRegistrationResponse toRegistrationResponse(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        ListRegistrationResponse listRegistrationResponse = new ListRegistrationResponse();

        listRegistrationResponse.setFullName( ticketModelRequesterFullName( ticketModel ) );
        listRegistrationResponse.setCompanyEmail( ticketModelRequesterCompanyEmail( ticketModel ) );
        listRegistrationResponse.setTicketId( ticketModel.getTicketId() );

        listRegistrationResponse.setTicketTypeName( ticketModel.getTicketType().getTypeName().name() );
        listRegistrationResponse.setTicketStatus( ticketModel.getSysStatus().name() );

        return listRegistrationResponse;
    }

    @Override
    public FirstThreeRegistrationResponse toFirstThreeRegistrationResponse(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        FirstThreeRegistrationResponse firstThreeRegistrationResponse = new FirstThreeRegistrationResponse();

        firstThreeRegistrationResponse.setSenderFullName( ticketModelRequesterFullName( ticketModel ) );
        firstThreeRegistrationResponse.setRegistrationDate( ticketModel.getStartAt() );

        firstThreeRegistrationResponse.setTicketStatus( ticketModel.getSysStatus().name() );

        return firstThreeRegistrationResponse;
    }

    @Override
    public RegistrationDetailResponse toRegistrationDetailResponse(TicketModel ticketModel) {
        if ( ticketModel == null ) {
            return null;
        }

        RegistrationDetailResponse registrationDetailResponse = new RegistrationDetailResponse();

        Long userId = ticketModelRequesterUserId( ticketModel );
        if ( userId != null ) {
            registrationDetailResponse.setUserId( String.valueOf( userId ) );
        }
        registrationDetailResponse.setAvatarUrl( ticketModelRequesterAvatarAvatarUrl( ticketModel ) );
        registrationDetailResponse.setPositionName( ticketModelRequesterPositionName( ticketModel ) );
        registrationDetailResponse.setCvUrl( ticketModelRequesterCvCvUrl( ticketModel ) );
        registrationDetailResponse.setInternshipStartDate( ticketModelRequesterInternshipStartDate( ticketModel ) );
        registrationDetailResponse.setInternshipEndDate( ticketModelRequesterInternshipEndDate( ticketModel ) );
        registrationDetailResponse.setFullName( ticketModelRequesterFullName( ticketModel ) );
        registrationDetailResponse.setIdNumber( ticketModelRequesterIdNumber( ticketModel ) );
        registrationDetailResponse.setDateOfBirth( ticketModelRequesterDateOfBirth( ticketModel ) );
        registrationDetailResponse.setPhoneNumber( ticketModelRequesterPhoneNumber( ticketModel ) );
        registrationDetailResponse.setCompanyEmail( ticketModelRequesterCompanyEmail( ticketModel ) );
        registrationDetailResponse.setAddress( ticketModelRequesterAddress( ticketModel ) );
        registrationDetailResponse.setSysStatus( map( ticketModel.getSysStatus() ) );

        return registrationDetailResponse;
    }

    @Override
    public FilterRegistrationTicketCommand toCommand(FilterRegistrationRequest request) {
        if ( request == null ) {
            return null;
        }

        FilterRegistrationTicketCommand.FilterRegistrationTicketCommandBuilder filterRegistrationTicketCommand = FilterRegistrationTicketCommand.builder();

        filterRegistrationTicketCommand.keyword( request.getKeyword() );

        filterRegistrationTicketCommand.ticketStatus( toTicketStatus(request.getTicketStatus()) );

        return filterRegistrationTicketCommand.build();
    }

    @Override
    public List<ListRegistrationResponse> toRegistrationResponseList(List<TicketModel> ticketModels) {
        if ( ticketModels == null ) {
            return null;
        }

        List<ListRegistrationResponse> list = new ArrayList<ListRegistrationResponse>( ticketModels.size() );
        for ( TicketModel ticketModel : ticketModels ) {
            list.add( toRegistrationResponse( ticketModel ) );
        }

        return list;
    }

    private Long leaveModelTicketTicketId(LeaveRequestModel leaveRequestModel) {
        TicketModel ticket = leaveRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getTicketId();
    }

    private TicketType leaveModelTicketTicketTypeTypeName(LeaveRequestModel leaveRequestModel) {
        TicketModel ticket = leaveRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        TicketTypeModel ticketType = ticket.getTicketType();
        if ( ticketType == null ) {
            return null;
        }
        return ticketType.getTypeName();
    }

    private TicketStatus leaveModelTicketSysStatus(LeaveRequestModel leaveRequestModel) {
        TicketModel ticket = leaveRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getSysStatus();
    }

    private LocalDate leaveModelTicketStartAt(LeaveRequestModel leaveRequestModel) {
        TicketModel ticket = leaveRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getStartAt();
    }

    private TicketType ticketModelTicketTypeTypeName(TicketModel ticketModel) {
        TicketTypeModel ticketType = ticketModel.getTicketType();
        if ( ticketType == null ) {
            return null;
        }
        return ticketType.getTypeName();
    }

    private Long requestModelTicketTicketId(RemoteRequestModel remoteRequestModel) {
        TicketModel ticket = remoteRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getTicketId();
    }

    private TicketStatus requestModelTicketSysStatus(RemoteRequestModel remoteRequestModel) {
        TicketModel ticket = remoteRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getSysStatus();
    }

    private LocalDate requestModelTicketStartAt(RemoteRequestModel remoteRequestModel) {
        TicketModel ticket = remoteRequestModel.getTicket();
        if ( ticket == null ) {
            return null;
        }
        return ticket.getStartAt();
    }

    private String ticketModelRequesterFullName(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getFullName();
    }

    private String ticketModelRequesterCompanyEmail(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getCompanyEmail();
    }

    private Long ticketModelRequesterUserId(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getUserId();
    }

    private String ticketModelRequesterAvatarAvatarUrl(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        AvatarModel avatar = requester.getAvatar();
        if ( avatar == null ) {
            return null;
        }
        return avatar.getAvatarUrl();
    }

    private String ticketModelRequesterPositionName(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        PositionModel position = requester.getPosition();
        if ( position == null ) {
            return null;
        }
        return position.getName();
    }

    private String ticketModelRequesterCvCvUrl(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        CvModel cv = requester.getCv();
        if ( cv == null ) {
            return null;
        }
        return cv.getCvUrl();
    }

    private LocalDate ticketModelRequesterInternshipStartDate(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getInternshipStartDate();
    }

    private LocalDate ticketModelRequesterInternshipEndDate(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getInternshipEndDate();
    }

    private String ticketModelRequesterIdNumber(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getIdNumber();
    }

    private LocalDate ticketModelRequesterDateOfBirth(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getDateOfBirth();
    }

    private String ticketModelRequesterPhoneNumber(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getPhoneNumber();
    }

    private String ticketModelRequesterAddress(TicketModel ticketModel) {
        UserModel requester = ticketModel.getRequester();
        if ( requester == null ) {
            return null;
        }
        return requester.getAddress();
    }
}
