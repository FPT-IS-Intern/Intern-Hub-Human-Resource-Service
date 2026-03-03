package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  Long ticketId;
  String ticketType;
  String ticketStatus;
  LocalDate createDate;
}
