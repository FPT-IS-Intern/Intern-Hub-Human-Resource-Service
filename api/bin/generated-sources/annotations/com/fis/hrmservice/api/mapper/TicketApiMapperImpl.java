package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.CreateTicketResponse;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.TicketType;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-10T10:06:38+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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
    public CreateTicketResponse toTicketResponse(LeaveRequestModel leaveModel) {
        if ( leaveModel == null ) {
            return null;
        }

        CreateTicketResponse createTicketResponse = new CreateTicketResponse();

        createTicketResponse.setTicketId( leaveModelTicketTicketId( leaveModel ) );
        TicketType typeName = leaveModelTicketTicketTypeTypeName( leaveModel );
        if ( typeName != null ) {
            createTicketResponse.setTicketType( typeName.name() );
        }
        TicketStatus sysStatus = leaveModelTicketSysStatus( leaveModel );
        if ( sysStatus != null ) {
            createTicketResponse.setTicketStatus( sysStatus.name() );
        }
        createTicketResponse.setCreateDate( leaveModelTicketStartAt( leaveModel ) );

        return createTicketResponse;
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
}
