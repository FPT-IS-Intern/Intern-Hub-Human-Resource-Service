package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

@Data
@Builder
public class FilterResponse {
  Integer no;

  @JsonSerialize(using = ToStringSerializer.class)
  Long userId;

  String avatarUrl;
  String fullName;
  String sysStatus;
  String email;
  String role;
  String position;

  String authIdentityStatus;
}
