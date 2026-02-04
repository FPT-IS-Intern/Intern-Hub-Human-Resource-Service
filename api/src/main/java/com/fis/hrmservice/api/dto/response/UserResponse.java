package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Response DTO for user data. Mapping from UserModel is handled by MapStruct in UserApiMapper. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Long userId;
  String fullName;
  String email;
  String phoneNumber;
  String address;
  LocalDate dateOfBirth;
  String positionCode;
  LocalDate internshipStartDate;
  LocalDate internshipEndDate;
  String status;
}
