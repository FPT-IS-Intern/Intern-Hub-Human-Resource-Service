package com.fis.hrmservice.domain.usecase.command.ticket;

import com.fis.hrmservice.domain.model.constant.TicketStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterRegistrationTicketCommand {
  String keyword;
  TicketStatus ticketStatus;
}
