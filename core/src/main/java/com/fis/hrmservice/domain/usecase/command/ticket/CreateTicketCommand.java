package com.fis.hrmservice.domain.usecase.command.ticket;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTicketCommand {
  String ticketType;
  LocalDate fromDate;
  LocalDate toDate;
  String reason;
  MultipartFile evidence;
}
