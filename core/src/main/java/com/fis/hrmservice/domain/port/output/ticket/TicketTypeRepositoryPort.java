package com.fis.hrmservice.domain.port.output.ticket;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import java.util.Optional;

public interface TicketTypeRepositoryPort {
  Optional<TicketTypeModel> findTicketTypeById(Long ticketTypeId);

  TicketTypeModel findTicketTypeByCode(String ticketTypeCode);
}
