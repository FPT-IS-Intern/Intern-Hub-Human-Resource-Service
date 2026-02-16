package com.fis.hrmservice.infra.persistence.adapter.ticket.explaination;

import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.infra.mapper.TicketMapper;
import com.fis.hrmservice.infra.persistence.adapter.user.UserRepositoryAdapter;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TicketRepositoryAdapter implements TicketRepositoryPort {

  @Autowired private TicketRepository ticketRepository;

  @Autowired private TicketMapper ticketMapper;

  @Autowired private EntityManager entityManager;

  @Autowired
  private UserRepositoryAdapter userRepository;

  @Override
  @Transactional
  public TicketModel save(TicketModel ticket) {

    Ticket ticketEntity = ticketMapper.toEntity(ticket);

    // ================= FIX TRANSIENT TicketType =================
    if (ticket.getTicketType() != null) {
      TicketType managedType =
          entityManager.getReference(TicketType.class, ticket.getTicketType().getTicketTypeId());

      ticketEntity.setTicketType(managedType);
    }
    // ============================================================

    Ticket saved = ticketRepository.save(ticketEntity);

    return ticketMapper.toModel(saved);
  }

  @Override
  public TicketModel findById(Long ticketId) {
    return ticketMapper.toModel(ticketRepository.findById(ticketId).orElseThrow());
  }

  @Override
  public List<TicketModel> filterRegistrationTicket(String keyword, String ticketStatus) {
      return ticketRepository.filterTickets(keyword, ticketStatus).stream().map(ticketMapper::toModel).toList();
  }

  @Override
  public List<TicketModel> findAll() {
    return ticketRepository.findAll().stream().map(ticketMapper::toModel).toList();
  }

  @Override
  public List<TicketModel> firstThreeRegistrationTicket() {
    return ticketRepository.firstThreeRegistrationTicket().stream().map(ticketMapper::toModel).toList();
  }

  @Override
  public TicketModel getDetailRegistrationTicket(Long ticketId) {
    return ticketMapper.toModel(ticketRepository.getDetailRegistrationTicket(ticketId));
  }

  @Override
  public TicketModel updateRegistrationTicketStatus(Long ticketId, String ticketStatus) {

    TicketModel ticket = findById(ticketId);
    if (ticket == null) {
      throw new NotFoundException("Ticket not found with id: " + ticketId);
    }

    int updatedRows = ticketRepository.updateRegistrationTicketStatus(ticketStatus, ticketId);
    if (updatedRows <= 0) {
      throw new ConflictDataException("Cannot update ticket status");
    }

    UserModel user = ticket.getRequester();

    if (ticketStatus.equals("APPROVED")) {
      user.setSysStatus(UserStatus.APPROVED);
    }
    else if (ticketStatus.equals("REJECTED")){
      user.setSysStatus(UserStatus.REJECTED);
    }
    userRepository.save(user);
    return ticket;
  }

}
