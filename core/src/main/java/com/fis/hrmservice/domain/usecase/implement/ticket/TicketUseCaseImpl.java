package com.fis.hrmservice.domain.usecase.implement.ticket;

import com.fis.hrmservice.common.utils.DateValidationHelper;
import com.fis.hrmservice.domain.model.constant.RemoteType;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.*;
import com.fis.hrmservice.domain.model.user.UserModel;
import com.fis.hrmservice.domain.port.output.ticket.TicketApprovalRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.leaveticket.LeaveRequestRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.RemoteRequestRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.remoteticket.WorkLocationRepositoryPort;
import com.fis.hrmservice.domain.port.output.user.UserRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.LeaveRequestCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequestCommand;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.exception.NotFoundException;
import com.intern.hub.library.common.utils.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TicketUseCaseImpl {

  @Autowired private TicketRepositoryPort ticketRepositoryPort;

  @Autowired private LeaveRequestRepositoryPort leaveRequestRepositoryPort;

  @Autowired private TicketTypeRepositoryPort ticketTypeRepositoryPort;

  @Autowired private RemoteRequestRepositoryPort remoteRequestRepositoryPort;

  @Autowired private WorkLocationRepositoryPort workLocationRepositoryPort;

  @Autowired private UserRepositoryPort userRepositoryPort;

  @Autowired private TicketApprovalRepositoryPort ticketApprovalRepositoryPort;

  @Autowired private Snowflake snowflake;

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

  /* ================= LEAVE ================= */

  public LeaveRequestModel createLeaveRequest(
      CreateTicketCommand ticketCommand, LeaveRequestCommand leaveCommand, Long userId) {

    // 1. Calculate total days
    // Assuming inclusive: from 2024-01-01 to 2024-01-01 is 1 day
    int totalDays =
        (int)
            (java.time.temporal.ChronoUnit.DAYS.between(
                    ticketCommand.getFromDate(), ticketCommand.getToDate())
                + 1);

    if (totalDays <= 0) {
      throw new ConflictDataException("Total days must be greater than 0");
    }

    TicketModel ticket = createBaseTicket(ticketCommand, userId);

    LeaveRequestModel model =
        LeaveRequestModel.builder().ticket(ticket).totalDays(totalDays).build();

    LeaveRequestModel savedModel = leaveRequestRepositoryPort.save(model);

    // 2. Set up approval flow
    // Level 1: Mentor/Leader (always)
    if (ticket.getRequester().getMentor() != null) {
      ticketApprovalRepositoryPort.save(
          TicketApprovalModel.builder()
              .approvalId(snowflake.next())
              .ticket(ticket)
              .approver(ticket.getRequester().getMentor())
              .status("WAITING")
              .build());
    }

    // Level 2: Management/Admin (if > 5 days)
    if (totalDays > 5) {
      java.util.List<UserModel> admins =
          userRepositoryPort.filterUser(
              com.fis.hrmservice.domain.usecase.command.user.FilterUserCommand.builder()
                  .positions(java.util.List.of("ADMIN", "MANAGER")) // Assuming these position names
                  .build());

      if (!admins.isEmpty()) {
        // For simplicity, pick the first one or assign to a generic one
        ticketApprovalRepositoryPort.save(
            TicketApprovalModel.builder()
                .approvalId(snowflake.next())
                .ticket(ticket)
                .approver(admins.get(0))
                .status("PENDING") // Level 2 starts as PENDING until Level 1 is WAITING?
                // Actually, Level 1 should be WAITING, Level 2 should be PENDING.
                .build());
      }
    }

    return savedModel;
  }

  /* ================= REMOTE ================= */

  public RemoteRequestModel createRemoteRequest(
      CreateTicketCommand ticketCommand, RemoteRequestCommand remoteCommand, Long userId) {

    RemoteType remoteType;

    try {
      remoteType = RemoteType.valueOf(ticketCommand.getTicketType().toUpperCase());
    } catch (Exception e) {
      throw new ConflictDataException("Invalid remote type");
    }

    TicketModel ticket = createBaseTicket(ticketCommand, userId);

    return switch (remoteType) {
      case WFH ->
          remoteRequestRepositoryPort.save(
              RemoteRequestModel.builder().ticket(ticket).remoteType(RemoteType.WFH).build());

      case ONSITE -> {
        DateValidationHelper.validateHour(remoteCommand.getStartTime(), remoteCommand.getEndTime());

        if (!workLocationRepositoryPort.existByLocationName(remoteCommand.getLocation())) {
          throw new ConflictDataException("Location not found");
        }

        yield remoteRequestRepositoryPort.save(
            RemoteRequestModel.builder()
                .ticket(ticket)
                .remoteType(RemoteType.ONSITE)
                .workLocation(
                    workLocationRepositoryPort.findByLocationName(remoteCommand.getLocation()))
                .startTime(remoteCommand.getStartTime())
                .endTime(remoteCommand.getEndTime())
                .build());
      }
    };
  }

  /* ================= EXPLANATION ================= */

  public TicketModel createExplanationTicket(CreateTicketCommand command, Long userId) {

    TicketModel ticket = createBaseTicket(command, userId);

    ticket.setEndAt(null);

    // KHÔNG save lại ticket lần 2
    return ticket;
  }


  public List<TicketModel> listAllRegistrationTicket(String keyword, String ticketStatus){
    if ((keyword == null || keyword.isEmpty()) && (ticketStatus == null || ticketStatus.isEmpty())) {
      return ticketRepositoryPort.findAll();
    }
    return ticketRepositoryPort.filterRegistrationTicket(keyword, ticketStatus);
  }

  public List<TicketModel> firstThreeRegistrationTicket(){
    return ticketRepositoryPort.firstThreeRegistrationTicket();
  }

  public TicketModel getDetailRegistrationTicket(Long ticketId){

    TicketModel ticket = ticketRepositoryPort.getDetailRegistrationTicket(ticketId);

    if (ticket == null) {
      throw new NotFoundException("Ticket not found: " + ticketId);
    }

    return ticket;
  }

  public TicketModel approveRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, "APPROVED");
  }

  public TicketModel rejectRegistrationTicketByTicketId(Long ticketId) {
    return ticketRepositoryPort.updateRegistrationTicketStatus(ticketId, "REJECTED");
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
