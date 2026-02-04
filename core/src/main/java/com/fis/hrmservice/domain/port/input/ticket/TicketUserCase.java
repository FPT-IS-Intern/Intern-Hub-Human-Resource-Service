package com.fis.hrmservice.domain.port.input.ticket;

import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.LeaveRequestCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequest;

public interface TicketUserCase {
  Long createTicket(CreateTicketCommand createTicketCommand);

  LeaveRequestModel createLeaveRequest(
      CreateTicketCommand createTicketCommand, LeaveRequestCommand leaveRequestCommand);

  RemoteRequestModel createRemoteRequest(
      CreateTicketCommand createTicketCommand, RemoteRequest remoteRequest);

  TicketModel createExplanationTicket(CreateTicketCommand createTicketCommand, Long ticketId);
}
