package com.fis.hrmservice.domain.usecase.implement.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import com.fis.hrmservice.domain.model.ticket.*;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.feign.CreateAuthIdentityPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketApprovalRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.leaveticket.LeaveRequestRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.RemoteRequestRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.WorkLocationRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.FilterRegistrationTicketCommand;
import com.fis.hrmservice.domain.utils.helper.DateValidationHelper;
import com.intern.hub.library.common.dto.PaginatedData;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TicketUseCaseImpl {

  private final TicketRepositoryPort ticketRepositoryPort;

  private final LeaveRequestRepositoryPort leaveRequestRepositoryPort;

  private final TicketTypeRepositoryPort ticketTypeRepositoryPort;

  private final RemoteRequestRepositoryPort remoteRequestRepositoryPort;

  private final WorkLocationRepositoryPort workLocationRepositoryPort;

  private final UserRepositoryPort userRepositoryPort;

  private final TicketApprovalRepositoryPort ticketApprovalRepositoryPort;

  private final CreateAuthIdentityPort createAuthIdentityPort;

  private final Snowflake snowflake;

  /* ================= BASE TICKET ================= */

  private TicketModel createBaseTicket(CreateTicketCommand command, Long userId) {

    UserModel requester =
        userRepositoryPort
            .findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId));

    DateValidationHelper.validateDate(command.getFromDate(), command.getToDate());

    TicketTypeModel ticketTypeModel =
        ticketTypeRepositoryPort.findTicketTypeByCode(command.getTicketType());

    if (ticketTypeModel == null) {
      throw new NotFoundException("Ticket type not found: " + command.getTicketType());
    }

    TicketModel ticket =
        TicketModel.builder()
            .ticketId(snowflake.next())
            .requester(requester)
            .ticketType(ticketTypeModel)
            .startAt(command.getFromDate())
            .endAt(command.getToDate())
            .reason(command.getReason())
            .sysStatus(TicketStatus.PENDING)
            .build();

    return ticketRepositoryPort.save(ticket);
  }

  public PaginatedData<TicketModel> listRegistrationTicketPaged(
      FilterRegistrationTicketCommand command, int page, int size) {
    return ticketRepositoryPort.filterRegistrationTicketPaged(command, page, size);
  }

  public List<TicketModel> firstThreeRegistrationTicket() {
    return ticketRepositoryPort.firstThreeRegistrationTicket();
  }

  public TicketModel getDetailRegistrationTicket(Long ticketId) {
    log.info("=== Getting detail for ticketId: {} ===", ticketId);

    TicketModel ticket = ticketRepositoryPort.getDetailRegistrationTicket(ticketId);

    log.info("Query result: ticket={}", ticket);

    if (ticket == null) {
      log.warn("Ticket not found with id: {} and type REGISTRATION", ticketId);
      throw new NotFoundException("Ticket not found: " + ticketId);
    }

    log.info(
        "Ticket found: id={}, requester={}, type={}",
        ticket.getTicketId(),
        ticket.getRequester() != null ? ticket.getRequester().getFullName() : "null",
        ticket.getTicketType() != null ? ticket.getTicketType().getTypeName() : "null");

    return ticket;
  }

  @Transactional
  public TicketModel approveRegistrationTicketByTicketId(Long ticketId, String roleId) {

    // 1. Validate roleId
    Long roleIdParsed;
    try {
      roleIdParsed = Long.parseLong(roleId);
    } catch (NumberFormatException e) {
      throw new ConflictDataException("Role ID must be a number");
    }

    // 2. Update ticket status
    TicketModel ticket =
            ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, TicketStatus.APPROVED);

    if (ticket == null) {
      throw new NotFoundException("Ticket not found: " + ticketId);
    }

    UserModel user = ticket.getRequester();

    if (user == null) {
      throw new NotFoundException("Requester user not found for ticket: " + ticketId);
    }

    if (user.getSysStatus() != UserStatus.APPROVED) {
      throw new ConflictDataException("User is not in APPROVED state");
    }

    Long userId = user.getUserId();
    String email = user.getCompanyEmail();

    log.info("Approving registration ticket {} for userId {}", ticketId, userId);

    TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCommit() {
                try {
                  createAuthIdentityPort.createAuthIdentity(userId, email);
                  log.info("Auth identity created for userId {}", userId);

                  createAuthIdentityPort.setUserRole(userId, roleIdParsed);
                  log.info("Role {} assigned to userId {}", roleIdParsed, userId);

                } catch (Exception e) {
                  log.error("Failed to sync user {} to auth service", userId, e);
                }
              }
            }
    );

    return ticket;
  }

  public TicketModel rejectRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, TicketStatus.REJECTED);
  }

  public TicketModel suspendRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, TicketStatus.SUSPENDED);
  }

  public int allRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicket();
  }

  public int allApprovedRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketApproved();
  }

  public int allRejectedRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketRejected();
  }

  public int allPendingRegistrationTicket() {
    return ticketRepositoryPort.getAllRegistrationTicketPending();
  }
}
