package com.fis.hrmservice.api.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InternalUserProfileResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  Long userId;
  String idNumber;
  String companyEmail;
}
