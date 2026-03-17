package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FirstThreeRegistrationResponse {
  String senderFullName;
  LocalDate registrationDate;
  String ticketStatus;
}
