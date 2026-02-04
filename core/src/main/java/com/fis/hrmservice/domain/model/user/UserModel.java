package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import com.fis.hrmservice.domain.model.constant.UserStatus;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel extends BaseDomain {

  Long userId;
  String fullName;
  String companyEmail;
  String phoneNumber;
  String idNumber;
  LocalDate dateOfBirth;

  String address;
  UserStatus sysStatus;

  LocalDate internshipStartDate;
  LocalDate internshipEndDate;

  PositionModel position;
  UserModel mentor;

  String avatarUrl;
  String cvUrl;
}
