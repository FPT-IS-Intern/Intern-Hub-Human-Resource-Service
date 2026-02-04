package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;

public interface TicketTypeRepositoryPort {
  TicketTypeModel findTicketTypeById(Long ticketTypeId);

  TicketTypeModel findTicketTypeByCode(String ticketTypeCode);
}
