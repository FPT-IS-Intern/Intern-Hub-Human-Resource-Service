package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.LeaveTicketRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.CreateTicketResponse;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.LeaveRequestCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketApiMapper {

    CreateTicketRequest toTicketRequest(CreateTicketCommand command);
    CreateTicketCommand toTicketCommand(CreateTicketRequest request);

    @Mapping(source = "ticket.ticketId", target = "ticketId")
    @Mapping(source = "ticket.ticketType.typeName", target = "ticketType")
    @Mapping(source = "ticket.sysStatus", target = "ticketStatus")
    @Mapping(source = "ticket.startAt", target = "createDate")
    CreateTicketResponse toTicketResponse(LeaveRequestModel leaveModel);

    RemoteRequestCommand toRemoteRequestCommand(RemoteTicketRequest leaveRequest);
}