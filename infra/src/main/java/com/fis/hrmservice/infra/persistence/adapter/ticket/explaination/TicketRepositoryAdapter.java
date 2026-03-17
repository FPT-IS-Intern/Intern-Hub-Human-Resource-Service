package com.fis.hrmservice.infra.persistence.adapter.ticket.explaination;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.model.user.PositionModel;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.infra.mapper.TicketMapper;
import com.fis.hrmservice.infra.persistence.adapter.user.UserRepositoryAdapter;
import com.fis.hrmservice.infra.persistence.entity.Ticket;
import com.fis.hrmservice.infra.persistence.entity.TicketType;
import com.fis.hrmservice.infra.persistence.repository.ticket.TicketRepository;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
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

  private final UserRepositoryAdapter userRepository;

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
    TicketModel model =
        ticketMapper.toModel(
            ticketRepository
                .findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found with id: " + ticketId)));
    hydrateRequesterFromTemp(model);
    return model;
  }

  @Override
  public List<TicketModel> filterRegistrationTicket(String keyword, String ticketStatus) {
    System.out.println("CALLING FILTER TICKETS");
    return ticketRepository.filterTickets(keyword, ticketStatus).stream()
        .map(ticketMapper::toModel)
        .peek(this::hydrateRequesterFromTemp)
        .toList();
  }

  @Override
  public PaginatedData<TicketModel> filterRegistrationTicketPaged(
      FilterRegistrationTicketCommand command, int page, int size) {

    Page<Ticket> ticketPage =
        ticketRepository.filterRegistrationTicketPaged(
            command.getKeyword(),
            command.getTicketStatus() == null ? null : command.getTicketStatus().name(),
            PageRequest.of(page, size));

    List<TicketModel> models =
        ticketPage.getContent().stream()
            .map(ticketMapper::toModel)
            .peek(this::hydrateRequesterFromTemp)
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
        .peek(this::hydrateRequesterFromTemp)
        .toList();
  }

  @Override
  public TicketModel getDetailRegistrationTicket(Long ticketId) {
    TicketModel model = ticketMapper.toModel(ticketRepository.getDetailRegistrationTicket(ticketId));
    hydrateRequesterFromTemp(model);
    return model;
  }

  @Override
  @Transactional
  public TicketModel updateRegistrationTicketStatus(Long ticketId, TicketStatus ticketStatus) {

    // findById already throws NotFoundException if not found, no need for null check
    TicketModel ticket = findById(ticketId);

    int updatedRows = ticketRepository.updateRegistrationTicketStatus(ticketStatus, ticketId);

    if (updatedRows <= 0) {
      throw new ConflictDataException("Cannot update ticket status");
    }

    // ✅ Update in-memory ticket
    ticket.setSysStatus(ticketStatus);

    UserModel user = ticket.getRequester();

    if (user == null || user.getUserId() == null || userRepository.findById(user.getUserId()).isEmpty()) {
      return ticket;
    }

    UserStatus newUserStatus = switch (ticket.getSysStatus()) {
      case APPROVED -> UserStatus.APPROVED;
      case REJECTED -> UserStatus.REJECTED;
      case SUSPENDED -> UserStatus.SUSPENDED;
      case PENDING -> UserStatus.PENDING;
      default -> throw new ConflictDataException("Unsupported ticket status for user update: " + ticketStatus);
    };

    user.setSysStatus(newUserStatus);

    log.info("user status after ticket update: userId={}, newStatus={}", user.getUserId(), user.getSysStatus());
    // Use targeted update to avoid overwriting unrelated user fields with stale data
    userRepository.updateStatus(user.getUserId(), newUserStatus);

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

  private void hydrateRequesterFromTemp(TicketModel ticket) {
    if (ticket == null || ticket.getRequester() != null || ticket.getUserInfoTemp() == null) {
      return;
    }

    Map<String, Object> temp = ticket.getUserInfoTemp();
    UserModel.UserModelBuilder builder = UserModel.builder();

    if (temp.get("userId") != null) {
      builder.userId(toLong(temp.get("userId")));
    }
    if (temp.get("fullName") != null) {
      builder.fullName(String.valueOf(temp.get("fullName")));
    }
    if (temp.get("companyEmail") != null) {
      builder.companyEmail(String.valueOf(temp.get("companyEmail")));
    }
    if (temp.get("phoneNumber") != null) {
      builder.phoneNumber(String.valueOf(temp.get("phoneNumber")));
    }
    if (temp.get("idNumber") != null) {
      builder.idNumber(String.valueOf(temp.get("idNumber")));
    }
    if (temp.get("address") != null) {
      builder.address(String.valueOf(temp.get("address")));
    }
    if (temp.get("dateOfBirth") != null) {
      builder.dateOfBirth(toLocalDate(temp.get("dateOfBirth")));
    }
    if (temp.get("internshipStartDate") != null) {
      builder.internshipStartDate(toLocalDate(temp.get("internshipStartDate")));
    }
    if (temp.get("internshipEndDate") != null) {
      builder.internshipEndDate(toLocalDate(temp.get("internshipEndDate")));
    }
    if (temp.get("avatarUrl") != null) {
      builder.avatarUrl(String.valueOf(temp.get("avatarUrl")));
    }
    if (temp.get("cvUrl") != null) {
      builder.cvUrl(String.valueOf(temp.get("cvUrl")));
    }

    if (temp.get("positionCode") != null) {
      builder.position(
          PositionModel.builder().name(String.valueOf(temp.get("positionCode"))).build());
    }

    ticket.setRequester(builder.build());
  }

  private Long toLong(Object value) {
    if (value instanceof Number number) {
      return number.longValue();
    }
    return Long.parseLong(String.valueOf(value));
  }

  private LocalDate toLocalDate(Object value) {
    try {
      return LocalDate.parse(String.valueOf(value));
    } catch (DateTimeParseException ex) {
      return null;
    }
  }
}
