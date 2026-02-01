package com.fis.hrmservice.domain.usecase.command;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserCommand {

    String email;
    String fullName;
    String idNumber;
    LocalDate birthDate;
    String address;
    String phoneNumber;
    String positionCode;

    LocalDate internshipStartDate;
    LocalDate internshipEndDate;

    String cvFileName;
    String cvContentType;
    long cvSize;

    String avatarFileName;
    String avatarContentType;
    long avatarSize;

    public boolean isInternRegistration() {
        return positionCode != null && positionCode.toUpperCase().contains("INTERN");
    }
}
