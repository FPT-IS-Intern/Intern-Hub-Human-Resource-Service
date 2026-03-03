package com.fis.hrmservice.api.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ListRegistrationResponse {
  int no;

  @JsonSerialize(using = ToStringSerializer.class)
  Long ticketId;

  String fullName;
  String companyEmail;
  String departmentName;
  String ticketTypeName;
  String ticketStatus;
}
