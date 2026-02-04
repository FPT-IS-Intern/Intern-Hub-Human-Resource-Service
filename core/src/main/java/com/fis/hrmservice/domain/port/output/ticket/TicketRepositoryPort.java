package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketModel;

public interface TicketRepositoryPort {
  TicketModel save(TicketModel ticket);

  TicketModel findById(Long ticketId);
}
