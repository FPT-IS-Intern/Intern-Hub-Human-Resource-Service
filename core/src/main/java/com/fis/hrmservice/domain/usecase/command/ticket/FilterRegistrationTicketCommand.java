package com.fis.hrmservice.domain.usecase.command.ticket;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FilterRegistrationTicketCommand {
  String keyword;
  String ticketStatus;
}
