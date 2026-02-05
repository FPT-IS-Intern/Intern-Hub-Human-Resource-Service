package com.fis.hrmservice.infra.persistence.adapter.ticket.explaination;

import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.infra.mapper.TicketMapper;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ExplanationRepositoryAdapter implements TicketRepositoryPort {

  @Autowired private TicketRepository ticketRepository;

  @Autowired private TicketMapper ticketMapper;

  @Override
  @Transactional
  public TicketModel save(TicketModel ticket) {
    Ticket ticketEntity = ticketMapper.toEntity(ticket);
    ticketRepository.save(ticketEntity);
    return ticketMapper.toModel(ticketEntity);
  }

  @Override
  public TicketModel findById(Long ticketId) {
    return ticketMapper.toModel(ticketRepository.findById(ticketId).get());
  }
}
