package com.fis.hrmservice.domain.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel {
    Long userId;
    Long positionId;
    Long mentorId;
    String fullName;
    String idNumber;
    LocalDate dateOfBirth;
    String emailFpt;
    String phoneNumber;
    String address;
    LocalDate internshipStartDate;
    LocalDate internshipEndDate;
}
