package com.fis.hrmservice.infra.persistence.adapter.ticket.explaination;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.infra.mapper.TicketMapper;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TicketRepositoryAdapter implements TicketRepositoryPort {

  private final TicketRepository ticketRepository;

  private final TicketMapper ticketMapper;

  private final EntityManager entityManager;

  @Override
  @Transactional
  public TicketModel save(TicketModel ticket) {

    Ticket ticketEntity = ticketMapper.toEntity(ticket);

    // ================= FIX TRANSIENT TicketType =================
    if (ticket.getTicketType() != null) {
      TicketType managedType = entityManager.getReference(TicketType.class, ticket.getTicketType().getTicketTypeId());

      ticketEntity.setTicketType(managedType);
    }
    // ============================================================

    Ticket saved = ticketRepository.save(ticketEntity);

    return ticketMapper.toModel(saved);
  }

  @Override
  public TicketModel findById(Long ticketId) {
    TicketModel model = ticketMapper.toModel(
        ticketRepository
            .findById(ticketId)
            .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + ticketId)));
    return model;
  }

  @Override
  public List<TicketModel> filterRegistrationTicket(String keyword, String ticketStatus) {
    System.out.println("CALLING FILTER TICKETS");
    return ticketRepository.filterTickets(keyword, ticketStatus).stream()
        .map(ticketMapper::toModel)
        .toList();
  }

  @Override
  public PaginatedData<TicketModel> filterRegistrationTicketPaged(
      FilterRegistrationTicketCommand command, int page, int size) {

    Page<Ticket> ticketPage = ticketRepository.filterRegistrationTicketPaged(
        command.getKeyword(),
        command.getTicketStatus() == null ? null : command.getTicketStatus().name(),
        PageRequest.of(page, size));

    List<TicketModel> models = ticketPage.getContent().stream()
        .map(ticketMapper::toModel)
        .toList();

    return PaginatedData.<TicketModel>builder()
        .items(models)
        .totalItems(ticketPage.getTotalElements())
        .totalPages(ticketPage.getTotalPages())
        .build();
  }

  @Override
  public List<TicketModel> findAll() {
    return ticketRepository.findAll().stream().map(ticketMapper::toModel).toList();
  }

  @Override
  public List<TicketModel> firstThreeRegistrationTicket() {
    return ticketRepository.firstThreeRegistrationTicket().stream()
        .map(ticketMapper::toModel)
        .toList();
  }

  @Override
  public TicketModel getDetailRegistrationTicket(Long ticketId) {
    return ticketMapper.toModel(ticketRepository.getDetailRegistrationTicket(ticketId));
  }

  @Override
  @Transactional
  public TicketModel updateRegistrationTicketStatus(Long ticketId, TicketStatus ticketStatus) {

    // findById already throws NotFoundException if not found, no need for null
    // check
    TicketModel ticket = findById(ticketId);

    int updatedRows = ticketRepository.updateRegistrationTicketStatus(ticketStatus, ticketId);

    if (updatedRows <= 0) {
      throw new ConflictDataException("Cannot update ticket status");
    }
    return ticket;
  }

  @Override
  public int getAllRegistrationTicket() {
    return ticketRepository.allRegistrationCount();
  }

  @Override
  public int getAllRegistrationTicketApproved() {
    return ticketRepository.allApprovedRegistrationCount();
  }

  @Override
  public int getAllRegistrationTicketRejected() {
    return ticketRepository.allRejectedRegistrationCount();
  }

  @Override
  public int getAllRegistrationTicketPending() {
    return ticketRepository.allPendingRegistrationCount();
  }

  @Override
  public boolean existsApprovedTicketByUserIdAndDate(Long userId, LocalDate date) {
    return ticketRepository.existsApprovedTicketByUserIdAndDate(userId, date);
  }
}
