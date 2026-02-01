package com.fis.hrmservice.domain.model.user;

import com.fis.hrmservice.domain.model.common.BaseDomain;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    String sysStatus;

    LocalDate internshipStartDate;
    LocalDate internshipEndDate;

    PositionModel position;
    UserModel mentor;

    String avatarUrl;
    String cvUrl;
}