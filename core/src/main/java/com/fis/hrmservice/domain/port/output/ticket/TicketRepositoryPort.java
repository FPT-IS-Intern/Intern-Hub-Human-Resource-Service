package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.intern.hub.library.common.dto.PaginatedData;
import java.util.List;

public interface TicketRepositoryPort {
  TicketModel save(TicketModel ticket);

  TicketModel findById(Long ticketId);

  List<TicketModel> filterRegistrationTicket(String keyword, String ticketStatus);

  PaginatedData<TicketModel> filterRegistrationTicketPaged(
      FilterRegistrationTicketCommand command, int page, int size);

  List<TicketModel> findAll();

  List<TicketModel> firstThreeRegistrationTicket();

  TicketModel getDetailRegistrationTicket(Long ticketId);

  TicketModel updateRegistrationTicketStatus(Long ticketId, TicketStatus ticketStatus);

  int getAllRegistrationTicket();

  int getAllRegistrationTicketApproved();

  int getAllRegistrationTicketRejected();

  int getAllRegistrationTicketPending();
}
