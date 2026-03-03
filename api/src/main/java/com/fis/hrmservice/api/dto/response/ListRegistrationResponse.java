package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
