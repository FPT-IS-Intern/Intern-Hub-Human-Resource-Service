package com.fis.hrmservice.api.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class InternalUserResponse {
  String fullName;
  String avatarUrl;
  String email;
  String role;
  Long roleId;
  Boolean isFaceRegistry;
}
