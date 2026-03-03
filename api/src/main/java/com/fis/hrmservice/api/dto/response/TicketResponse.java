package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  Long ticketId;

  String ticketType;
  String ticketStatus;
  LocalDate createDate;
}
