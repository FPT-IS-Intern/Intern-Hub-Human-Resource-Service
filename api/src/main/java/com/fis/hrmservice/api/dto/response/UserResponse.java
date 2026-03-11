package com.fis.hrmservice.api.dto.response;

import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

/** Response DTO for user data. Mapping from UserModel is handled by MapStruct in UserApiMapper. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  @JsonSerialize(using = ToStringSerializer.class)
  Long userId;

  /** Thông tin chung */
  String email;

  String fullName;
  String idNumber;
  String phoneNumber;
  String address;
  LocalDate dateOfBirth;
  String avatarUrl;

  /** Công việc */
  String positionCode;

  String role;
  String cvUrl;
  String superVisorId;
  LocalDate internshipStartDate;
  LocalDate internshipEndDate;

  /** Trạng thái người dùng */
  String sysStatus;
}
