package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketModel;

import java.util.List;

public interface TicketRepositoryPort {
  TicketModel save(TicketModel ticket);

  TicketModel findById(Long ticketId);

  List<TicketModel> filterRegistrationTicket(String keyword, String ticketStatus);

  List<TicketModel> findAll();

  List<TicketModel> firstThreeRegistrationTicket();

  TicketModel getDetailRegistrationTicket(Long ticketId);

  TicketModel updateRegistrationTicketStatus(Long ticketId, String ticketStatus);
}
