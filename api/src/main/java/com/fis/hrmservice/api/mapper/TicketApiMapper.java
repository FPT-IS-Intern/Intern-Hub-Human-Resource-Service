package com.fis.hrmservice.api.mapper;

import com.fis.hrmservice.api.dto.request.CreateTicketRequest;
import com.fis.hrmservice.api.dto.request.RemoteTicketRequest;
import com.fis.hrmservice.api.dto.response.TicketResponse;
import com.fis.hrmservice.domain.model.constant.RemoteType;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
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
  TicketResponse toTicketResponse(LeaveRequestModel leaveModel);

  @Mapping(source = "ticketId", target = "ticketId")
  @Mapping(source = "ticketType.name", target = "ticketType")
  @Mapping(source = "sysStatus", target = "ticketStatus")
  @Mapping(source = "startAt", target = "createDate")
  TicketResponse toTicketResponse(TicketModel ticketModel);

  @Mapping(source = "ticket.ticketId", target = "ticketId")
  @Mapping(source = "remoteType", target = "ticketType")
  @Mapping(source = "ticket.sysStatus", target = "ticketStatus")
  @Mapping(source = "ticket.startAt", target = "createDate")
  TicketResponse toTicketResponse(RemoteRequestModel requestModel);

  default String map(RemoteType type) {
    return type == null ? null : type.name();
  }

  default String map(TicketStatus status) {
    return status == null ? null : status.name();
  }


  RemoteRequestCommand toRemoteRequestCommand(RemoteTicketRequest leaveRequest);
}
