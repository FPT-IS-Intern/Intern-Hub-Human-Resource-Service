package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Data;

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
}
