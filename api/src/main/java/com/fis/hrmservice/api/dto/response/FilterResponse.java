package com.fis.hrmservice.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilterResponse {
  Integer no;
  String avatarUrl;
  String fullName;
  String sysStatus;
  String email;
  String role;
  String position;
}
