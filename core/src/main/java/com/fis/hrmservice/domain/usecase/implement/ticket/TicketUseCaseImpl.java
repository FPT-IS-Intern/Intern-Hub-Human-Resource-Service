package com.fis.hrmservice.domain.usecase.implement.ticket;

import com.fis.hrmservice.common.utils.DateValidationHelper;
import com.fis.hrmservice.domain.model.constant.RemoteType;
import com.fis.hrmservice.domain.model.constant.TicketStatus;
import com.fis.hrmservice.domain.model.ticket.LeaveRequestModel;
import com.fis.hrmservice.domain.model.ticket.RemoteRequestModel;
import com.fis.hrmservice.domain.model.ticket.TicketModel;
import com.fis.hrmservice.domain.port.input.ticket.TicketUserCase;
import com.fis.hrmservice.domain.port.output.ticket.TicketRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.TicketTypeRepositoryPort;
import com.fis.hrmservice.domain.port.output.ticket.leaveticket.LeaveRequestRepositoryPort;
import com.fis.hrmservice.domain.usecase.command.ticket.CreateTicketCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.LeaveRequestCommand;
import com.fis.hrmservice.domain.usecase.command.ticket.RemoteRequest;
import com.intern.hub.library.common.exception.ConflictDataException;
import com.intern.hub.library.common.utils.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TicketUseCaseImpl implements TicketUserCase {

  @Autowired private TicketRepositoryPort ticketRepositoryPort;

  @Autowired private LeaveRequestRepositoryPort leaveRequestRepositoryPort;

  @Autowired private TicketTypeRepositoryPort ticketTypeRepositoryPort;

  @Autowired private Snowflake snowflake;

  @Override
  public Long createTicket(CreateTicketCommand command) {
    /*
       TODO: khi nào api gateway làm xong mới dùng được
    */

    //        AuthContext context = AuthContextHolder.get()
    //                .orElseThrow(() -> new NotFoundException("Not authenticated"));
    //
    //        long userId = context.userId();

    // check date

    DateValidationHelper.validateDate(command.getFromDate(), command.getToDate());

    // check evidence
    String contentType = command.getEvidence().getContentType();
    if (contentType != null) {
      String lowerContentType = contentType.toLowerCase();
      boolean isValidFormat =
          lowerContentType.endsWith("png")
              || lowerContentType.endsWith("jpg")
              || lowerContentType.endsWith("jpeg")
              || lowerContentType.contains("image/png")
              || lowerContentType.contains("image/jpeg");

      if (!isValidFormat || command.getEvidence().getSize() > 2097152) {
        throw new ConflictDataException(
            "File evidence must be in .png, .jpeg, .jpg format and size must be less than 2MB");
      }
    }

    TicketModel ticketModel =
        TicketModel.builder()
            .ticketId(snowflake.next())
            .requester(null)
            .ticketType(ticketTypeRepositoryPort.findTicketTypeByCode(command.getTicketType()))
            .startAt(command.getFromDate())
            .endAt(command.getToDate())
            .reason(command.getReason())
            .sysStatus(TicketStatus.PENDING)
            .build();

    return ticketRepositoryPort.save(ticketModel).getTicketId();
  }

  @Override
  public LeaveRequestModel createLeaveRequest(
      CreateTicketCommand createTicketCommand, LeaveRequestCommand leaveRequestCommand) {
    Long ticketId = createTicket(createTicketCommand);

    if (leaveRequestCommand.getTotalDays() <= 0) {
      throw new ConflictDataException("Total days must be greater than 0");
    }

    LeaveRequestModel leaveRequestModel =
        LeaveRequestModel.builder().ticket(ticketRepositoryPort.findById(ticketId)).build();
    return leaveRequestRepositoryPort.save(leaveRequestModel);
  }

  @Override
  public RemoteRequestModel createRemoteRequest(
      CreateTicketCommand createTicketCommand, RemoteRequest remoteRequest) {

    RemoteType remoteType = remoteRequest.getRemoteType();

    switch (remoteType) {
      case RemoteType.WFH -> {}

      case RemoteType.ONSITE -> {
        DateValidationHelper.validateHour(remoteRequest.getStartTime(), remoteRequest.getEndTime());
      }
    }

    return null;
  }

  @Override
  public TicketModel createExplanationTicket(
      CreateTicketCommand createTicketCommand, Long ticketId) {
    return null;
  }
}
