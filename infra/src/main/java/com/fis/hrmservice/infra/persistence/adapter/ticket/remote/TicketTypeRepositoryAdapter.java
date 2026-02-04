package com.fis.hrmservice.infra.persistence.adapter.ticket.remote;

import com.fis.hrmservice.domain.model.ticket.TicketTypeModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.infra.mapper.TicketTypeMapper;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TicketTypeRepositoryAdapter implements TicketTypeRepositoryPort {

  @Autowired private TicketTypeRepository ticketTypeRepository;

  @Autowired private TicketTypeMapper ticketTypeMapper;

  @Override
  public TicketTypeModel findTicketTypeById(Long ticketTypeId) {
    return ticketTypeMapper.toModel(ticketTypeRepository.findById(ticketTypeId).get());
  }

  @Override
  public TicketTypeModel findTicketTypeByCode(String ticketTypeCode) {
    return ticketTypeMapper.toModel(ticketTypeRepository.findByTypeName(ticketTypeCode));
  }
}
